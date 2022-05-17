package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.dto.openApi.exception.CustomException;
import com.blackphoenixproductions.forumbackend.dto.openApi.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.dto.openApi.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.email.EmailSender;
import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.enums.Roles;
import com.blackphoenixproductions.forumbackend.repository.PostRepository;
import com.blackphoenixproductions.forumbackend.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.security.KeycloakUtility;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final IUserService userService;
    private final TopicRepository topicRepository;
    private final EmailSender emailSender;


    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, IUserService userService, TopicRepository topicRepository, EmailSender emailSender) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional(readOnly=true)
    @Override
    public Long getTotalPosts() {
        return postRepository.count();
    }

    @Transactional(readOnly=true)
    @Override
    public Page<Post> getPagedPosts(Long topicId, Pageable pageable) {
        Topic topic = new Topic(topicId);
        return postRepository.findByTopicAndDeleteDateIsNull(topic, pageable);
    }


    @Transactional
    @Override
    public Post createPost(InsertPostDTO postDTO, HttpServletRequest req) {
        Post post = new Post();
        User user = userService.retriveUser(KeycloakUtility.getAccessToken(req));
        if (user == null) {
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        Optional<Topic> topic = topicRepository.findById(postDTO.getTopicId());
        if (!topic.isPresent()){
            throw new CustomException("Topic con id: " + postDTO.getTopicId() + " non trovato.", HttpStatus.NOT_FOUND);
        }
        post.setMessage(postDTO.getMessage());
        post.setTopic(topic.get());
        post.setUser(user);
        post.setCreateDate(new Date());
        Post savedPost = postRepository.save(post);
        // se la risposta non è del creatore del topic e se emailUser = true allora invia un email al creatore del topic
        if (!savedPost.getUser().equals(savedPost.getTopic().getUser()) && savedPost.getTopic().isEmailUser()){
            emailSender.sendTopicReplyEmail(savedPost.getTopic().getUser(), savedPost.getUser(), savedPost) ;
        }
        return savedPost;
    }

    @Transactional
    public Post editPost(EditPostDTO postDTO, HttpServletRequest req) {
        Optional<Post> post = postRepository.findById(postDTO.getId());
        if(!post.isPresent()){
            throw new CustomException("Post con id: " + postDTO.getId() + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        if(!KeycloakUtility.getRoles(req).contains(Roles.ROLE_STAFF.getValue())
                && !post.get().getUser().getEmail().equals(KeycloakUtility.getAccessToken(req).getEmail())){
            throw new CustomException("Utente non autorizzato alla modifica del post.", HttpStatus.UNAUTHORIZED);
        }
        Post postToEdit = post.get();
        postToEdit.setEditDate(new Date());
        postToEdit.setMessage(postDTO.getMessage());
        return postRepository.save(postToEdit);
    }


}
