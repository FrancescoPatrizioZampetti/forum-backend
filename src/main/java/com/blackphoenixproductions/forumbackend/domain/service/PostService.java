package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.IPostService;
import com.blackphoenixproductions.forumbackend.domain.IUserService;
import com.blackphoenixproductions.forumbackend.domain.IEmailSender;
import com.blackphoenixproductions.forumbackend.domain.exception.CustomException;
import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import com.blackphoenixproductions.forumbackend.domain.entity.Topic;
import com.blackphoenixproductions.forumbackend.domain.entity.User;
import com.blackphoenixproductions.forumbackend.domain.enums.Roles;
import com.blackphoenixproductions.forumbackend.domain.repository.PostRepository;
import com.blackphoenixproductions.forumbackend.domain.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


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
    public Post createPost(Post post, String email) {
        User user = userService.retriveUser(email);
        if (user == null) {
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        Optional<Topic> topic = topicRepository.findById(post.getTopic().getId());
        if (!topic.isPresent()){
            throw new CustomException("Topic con id: " + post.getTopic().getId() + " non trovato.", HttpStatus.NOT_FOUND);
        }
        post.setUser(user);
        post.setTopic(topic.get());
        post.setCreateDate(LocalDateTime.now());
        Post savedPost = postRepository.saveAndFlush(post);
        if (!isTopicCreatorPost(savedPost) && savedPost.getTopic().isEmailUser()){
            emailSender.sendTopicReplyEmail(savedPost.getTopic().getUser(), savedPost.getUser(), savedPost) ;
        }
        return savedPost;
    }
    private static boolean isTopicCreatorPost(Post savedPost) {
        return savedPost.getUser().equals(savedPost.getTopic().getUser());
    }

    @Transactional
    @Override
    public Post editPost(Post post, String email, Set<String> roles) {
        Optional<Post> dbPost = postRepository.findById(post.getId());
        if(!dbPost.isPresent()){
            throw new CustomException("Post con id: " + post.getId() + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        if(!roles.contains(Roles.ROLE_STAFF.getValue())
                && !dbPost.get().getUser().getEmail().equals(email)){
            throw new CustomException("Utente non autorizzato alla modifica del post.", HttpStatus.UNAUTHORIZED);
        }
        Post postToEdit = dbPost.get();
        postToEdit.setEditDate(LocalDateTime.now());
        postToEdit.setMessage(post.getMessage());
        return postRepository.save(postToEdit);
    }

}
