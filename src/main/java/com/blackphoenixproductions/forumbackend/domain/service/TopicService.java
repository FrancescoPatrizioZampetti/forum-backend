package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.ports.ITopicService;
import com.blackphoenixproductions.forumbackend.domain.ports.IUserService;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.CustomException;
import com.blackphoenixproductions.forumbackend.domain.model.Topic;
import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.enums.Roles;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.config.security.KeycloakUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


@Service
public class TopicService implements ITopicService {

    private final TopicRepository topicRepository;
    private final IUserService userService;

    @Autowired
    public TopicService(TopicRepository topicRepository, IUserService userService) {
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

    @Transactional(readOnly=true)
    @Override
    public Long getTotalTopics() {
        return topicRepository.count();
    }

    @Transactional
    @Override
    public Topic createTopic(Topic topic, String email) {
        User userTopic = userService.retriveUser(email);
        if (userTopic == null){
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        topic.setUser(userTopic);
        topic.setCreateDate(LocalDateTime.now());
        return topicRepository.save(topic);
    }

    @Transactional
    @Override
    public Topic editTopic(Topic topic, Set<String> roles) {
        if(!roles.contains(Roles.ROLE_STAFF.getValue())){
            throw new CustomException("Utente non autorizzato alla modifica di un topic.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Topic> topicDB = topicRepository.findById(topic.getId());
        if(!topicDB.isPresent()){
            throw new CustomException("Topic con id: " + topic.getId() + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        Topic topicToEdit = topicDB.get();
        topicToEdit.setEditDate(LocalDateTime.now());
        topicToEdit.setMessage(topic.getMessage());
        return topicRepository.save(topicToEdit);
    }

    @Transactional(readOnly=true)
    @Override
    public Topic getTopic(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if(!topic.isPresent()){
            throw new CustomException("Topic con id: " + id + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        return topic.get();
    }


}
