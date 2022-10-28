package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.Filter;
import com.blackphoenixproductions.forumbackend.domain.entity.VTopic;
import com.blackphoenixproductions.forumbackend.domain.ISpecificationBuilder;
import com.blackphoenixproductions.forumbackend.domain.ITopicService;
import com.blackphoenixproductions.forumbackend.domain.IUserService;
import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.CustomException;
import com.blackphoenixproductions.forumbackend.domain.entity.Topic;
import com.blackphoenixproductions.forumbackend.domain.entity.User;
import com.blackphoenixproductions.forumbackend.domain.enums.Roles;
import com.blackphoenixproductions.forumbackend.domain.repository.TopicRepository;
import com.blackphoenixproductions.forumbackend.domain.repository.VTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final VTopicRepository vtopicRepository;
    private final ISpecificationBuilder specificationBuilder;

    @Autowired
    public TopicService(TopicRepository topicRepository, IUserService userService,
                        VTopicRepository vtopicRepository, ISpecificationBuilder specificationBuilder) {
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.vtopicRepository = vtopicRepository;
        this.specificationBuilder = specificationBuilder;
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

    @Override
    public Page<VTopic> getPagedTopics(Pageable pageable, Filter filter) {
        Specification<VTopic> spec = null;
        if (filter != null && filter.getFilters() != null && !filter.getFilters().isEmpty()) {
            spec = specificationBuilder.getSpecification(filter);
        }
        return vtopicRepository.findAll(spec, pageable);
    }


}
