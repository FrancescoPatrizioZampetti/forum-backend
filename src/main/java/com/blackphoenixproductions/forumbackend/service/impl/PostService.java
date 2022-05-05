package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.email.EmailSender;
import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.mapping.PostMapper;
import com.blackphoenixproductions.forumbackend.repository.PostRepository;
import com.blackphoenixproductions.forumbackend.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import dto.PostDTO;
import dto.SimpleUserDTO;
import dto.openApi.exception.CustomException;
import dto.openApi.post.EditPostDTO;
import dto.openApi.post.InsertPostDTO;
import enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final IUserService userService;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;
    private final EmailSender emailSender;


    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, IUserService userService, TopicRepository topicRepository, PostMapper postMapper, EmailSender emailSender) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postMapper = postMapper;
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
    public Page<PostDTO> getPagedPosts(Long topicId, Pageable pageable) {
        Topic topic = new Topic(topicId);
        Page<Post> pagedPosts = postRepository.findByTopicAndDeleteDateIsNull(topic, pageable);
        Page<PostDTO> pagedPostsDTO = pagedPosts.map(p -> convertPostToPostDTO(p, new SimpleDateFormat("'Inviato il' dd/MM/yy 'alle' HH:mm"),
                 new SimpleDateFormat("'Ultima modifica il' dd/MM/yy 'alle' HH:mm")));
        return pagedPostsDTO;
    }


    @Transactional
    @Override
    public PostDTO createPost(InsertPostDTO postDTO) {
        Post post = postMapper.insertPostDTOtoPost(postDTO);
        User user = userRepository.findByUsername(postDTO.getUsername());
        if (user == null) {
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        Optional<Topic> topic = topicRepository.findById(postDTO.getTopicId());
        if (!topic.isPresent()){
            throw new CustomException("Topic con id: " + postDTO.getTopicId() + " non trovato.", HttpStatus.NOT_FOUND);
        }
        post.setTopic(topic.get());
        post.setUser(user);
        post.setCreateDate(new Date());
        Post savedPost = postRepository.save(post);
        // se la risposta non è del creatore del topic e se emailUser = true allora invia un email al creatore del topic
        if (!savedPost.getUser().equals(savedPost.getTopic().getUser()) && savedPost.getTopic().isEmailUser()){
            emailSender.sendTopicReplyEmail(savedPost.getTopic().getUser(), savedPost.getUser(), savedPost) ;
        }
        return postMapper.postToPostDTO(savedPost);
    }

    @Transactional
    @Override
    public PostDTO editPost(EditPostDTO postDTO, HttpServletRequest req) {
        Optional<Post> post = postRepository.findById(postDTO.getId());
        if(!post.isPresent()){
            throw new CustomException("Post con id: " + postDTO.getId() + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        SimpleUserDTO simpleUserDTO = userService.getUserFromToken(req);
        if (!simpleUserDTO.getRole().equals(Roles.ROLE_STAFF) && !simpleUserDTO.getUsername().equals(post.get().getUser().getUsername())){
            throw new CustomException("Utente non autorizzato alla modifica del post.", HttpStatus.UNAUTHORIZED);
        }
        Post postToEdit = post.get();
        postToEdit.setEditDate(new Date());
        postToEdit.setMessage(postDTO.getMessage());
        Post updatedPost = postRepository.save(postToEdit);
        return postMapper.postToPostDTO(updatedPost);
    }

    private PostDTO convertPostToPostDTO(Post post, SimpleDateFormat sdf, SimpleDateFormat sdf_edit){
        PostDTO postDTO = postMapper.postToPostDTO(post);
        postDTO.setFormattedCreateDate(sdf.format(post.getCreateDate()));
        if (post.getEditDate() != null){
            postDTO.setFormattedEditDate(sdf_edit.format(post.getEditDate()));
        }
        return postDTO;
    }

}
