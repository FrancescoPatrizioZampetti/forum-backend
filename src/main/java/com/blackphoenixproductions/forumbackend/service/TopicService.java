package com.blackphoenixproductions.forumbackend.service;

import com.blackphoenixproductions.forumbackend.api.ITopicService;
import com.blackphoenixproductions.forumbackend.api.IUserService;
import com.blackphoenixproductions.forumbackend.dto.openApi.exception.CustomException;
import com.blackphoenixproductions.forumbackend.dto.openApi.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.dto.openApi.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.enums.Roles;
import com.blackphoenixproductions.forumbackend.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.security.KeycloakUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;



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
    public Topic createTopic(InsertTopicDTO insertTopicDTO, HttpServletRequest req) {
        User userTopic = userService.retriveUser(KeycloakUtility.getAccessToken(req));
        if (userTopic == null){
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        Topic topic = getTopicEntityFromDTO(insertTopicDTO, userTopic);
        return topicRepository.save(topic);
    }

    private static Topic getTopicEntityFromDTO(InsertTopicDTO insertTopicDTO, User userTopic) {
        Topic topic = new Topic();
        topic.setTitle(insertTopicDTO.getTitle());
        topic.setMessage(insertTopicDTO.getMessage());
        topic.setEmailUser(insertTopicDTO.isEmailUser());
        topic.setPinned(insertTopicDTO.isPinned());
        topic.setUser(userTopic);
        topic.setCreateDate(LocalDateTime.now());
        return topic;
    }

    @Transactional
    @Override
    public Topic editTopic(EditTopicDTO topicDTO, HttpServletRequest req) {
        if(!KeycloakUtility.getRoles(req).contains(Roles.ROLE_STAFF.getValue())){
            throw new CustomException("Utente non autorizzato alla modifica di un topic.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Topic> topic = topicRepository.findById(topicDTO.getId());
        if(!topic.isPresent()){
            throw new CustomException("Topic con id: " + topicDTO.getId() + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        Topic topicToEdit = topic.get();
        topicToEdit.setEditDate(LocalDateTime.now());
        topicToEdit.setMessage(topicDTO.getMessage());
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
