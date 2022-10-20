package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.ports.IPostService;
import com.blackphoenixproductions.forumbackend.domain.ports.IUserService;
import com.blackphoenixproductions.forumbackend.domain.ports.IEmailSender;
import com.blackphoenixproductions.forumbackend.domain.dto.CustomException;
import com.blackphoenixproductions.forumbackend.domain.dto.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.domain.dto.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import com.blackphoenixproductions.forumbackend.domain.model.Topic;
import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.enums.Roles;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.PostRepository;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.config.security.KeycloakUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final IUserService userService;
    private final TopicRepository topicRepository;
    private final IEmailSender emailSender;


    @Autowired
    public PostService(PostRepository postRepository, IUserService userService, TopicRepository topicRepository, IEmailSender emailSender) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.emailSender = emailSender;
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
        User user = userService.retriveUser(KeycloakUtility.getAccessToken(req));
        if (user == null) {
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        Optional<Topic> topic = topicRepository.findById(postDTO.getTopicId());
        if (!topic.isPresent()){
            throw new CustomException("Topic con id: " + postDTO.getTopicId() + " non trovato.", HttpStatus.NOT_FOUND);
        }
        Post post = getPostEntityFromDTO(postDTO, user, topic);
        Post savedPost = postRepository.save(post);
        // se la risposta non è del creatore del topic e se emailUser = true allora invia un email al creatore del topic
        if (!savedPost.getUser().equals(savedPost.getTopic().getUser()) && savedPost.getTopic().isEmailUser()){
            emailSender.sendTopicReplyEmail(savedPost.getTopic().getUser(), savedPost.getUser(), savedPost) ;
        }
        return savedPost;
    }

    private static Post getPostEntityFromDTO(InsertPostDTO postDTO, User user, Optional<Topic> topic) {
        Post post = new Post();
        post.setMessage(postDTO.getMessage());
        post.setTopic(topic.get());
        post.setUser(user);
        post.setCreateDate(LocalDateTime.now());
        return post;
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
        postToEdit.setEditDate(LocalDateTime.now());
        postToEdit.setMessage(postDTO.getMessage());
        return postRepository.save(postToEdit);
    }


}
