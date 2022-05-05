package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.mapping.TopicMapper;
import com.blackphoenixproductions.forumbackend.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.ITopicService;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import com.blackphoenixproductions.forumbackend.utility.DateUtility;
import dto.SimpleTopicDTO;
import dto.SimpleUserDTO;
import dto.TopicDTO;
import dto.openApi.exception.CustomException;
import dto.openApi.topic.EditTopicDTO;
import dto.openApi.topic.InsertTopicDTO;
import enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;



@Service
public class TopicService implements ITopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final IUserService userService;
    private final TopicMapper topicMapper;

    @Autowired
    public TopicService(TopicRepository topicRepository, UserRepository userRepository, IUserService userService, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.topicMapper = topicMapper;
    }

    @Transactional(readOnly=true)
    @Override
    public Long getTotalTopics() {
        return topicRepository.count();
    }


    @Transactional(readOnly=true)
    @Override
    public Page<SimpleTopicDTO> getPagedTopics(Pageable pageable, String title, String username) {
        Page<Topic> pagedTopics = null;
        if (username != null && !username.isEmpty()){
            User findedUser = userRepository.findByUsername(username);
            pagedTopics = topicRepository.findByDeleteDateIsNullAndTitleIgnoreCaseContainingAndUser(pageable, title, findedUser);
        } else {
            pagedTopics = topicRepository.findByDeleteDateIsNullAndTitleIgnoreCaseContaining(pageable, title);
        }
        return pagedTopics.map(p -> convertTopicToSimpleTopicDTO(p));
    }

    @Transactional
    @Override
    public SimpleTopicDTO createTopic(InsertTopicDTO insertTopicDTO) {
        User userTopic = userRepository.findByUsername(insertTopicDTO.getUsername());
        if (userTopic == null){
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        Topic topic = topicMapper.insertTopicDTOtoTopic(insertTopicDTO);
        topic.setUser(userTopic);
        topic.setCreateDate(new Date());
        Topic savedTopic = topicRepository.save(topic);
        return topicMapper.topicToSimpleTopicDTO(savedTopic);
    }

    @Transactional
    @Override
    public TopicDTO editTopic(EditTopicDTO topicDTO, HttpServletRequest req) {
        SimpleUserDTO simpleUserDTO = userService.getUserFromToken(req);
        if (!simpleUserDTO.getRole().equals(Roles.ROLE_STAFF)){
            throw new CustomException("Utente non autorizzato alla modifica di un topic.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Topic> topic = topicRepository.findById(topicDTO.getId());
        if(!topic.isPresent()){
            throw new CustomException("Topic con id: " + topicDTO.getId() + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        Topic topicToEdit = topic.get();
        topicToEdit.setEditDate(new Date());
        topicToEdit.setMessage(topicDTO.getMessage());
        Topic updatedTopic = topicRepository.save(topicToEdit);
        return topicMapper.topicToTopicDTO(updatedTopic);
    }

    @Transactional(readOnly=true)
    @Override
    public TopicDTO getTopic(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if(!topic.isPresent()){
            throw new CustomException("Topic con id: " + id + " non trovato.", HttpStatus.BAD_REQUEST);
        }
        TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic.get());
        setFormattedDate(topicDTO, topic.get());
        if (topic.get().getEditDate() != null) {
            setFormattedEditDate(topicDTO, topic.get());
        }
        return topicDTO;
    }

    private void setFormattedDate(TopicDTO topicDTO, Topic savedTopic) {
        SimpleDateFormat sdf = new SimpleDateFormat("'Inviato il' dd/MM/yy 'alle' HH:mm");
        topicDTO.setFormattedDate(sdf.format(savedTopic.getCreateDate()));
    }

    private void setFormattedEditDate(TopicDTO topicDTO, Topic savedTopic) {
        SimpleDateFormat sdf = new SimpleDateFormat("'Ultima modifica il' dd/MM/yy 'alle' HH:mm");
        topicDTO.setFormattedEditDate(sdf.format(savedTopic.getEditDate()));
    }


    private SimpleTopicDTO convertTopicToSimpleTopicDTO(Topic topic){
        SimpleTopicDTO simpleTopicDTO = topicMapper.topicToSimpleTopicDTO(topic);
        simpleTopicDTO.setPostsNumber(topic.getPosts().size());
        simpleTopicDTO.setTimeDifferenceFromNow(DateUtility.setTimeDifferenceFromNow(simpleTopicDTO.getCreateDate()));
        return simpleTopicDTO;
    }


}
