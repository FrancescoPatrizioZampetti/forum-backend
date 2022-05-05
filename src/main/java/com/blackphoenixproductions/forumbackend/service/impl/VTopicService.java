package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.dto.Filter;
import com.blackphoenixproductions.forumbackend.dto.openApi.exception.CustomException;
import com.blackphoenixproductions.forumbackend.entity.VTopic;
import com.blackphoenixproductions.forumbackend.repository.VTopicRepository;
import com.blackphoenixproductions.forumbackend.repository.specification.SpecificationBuilder;
import com.blackphoenixproductions.forumbackend.service.IVTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class VTopicService implements IVTopicService {

    private final VTopicRepository vtopicRepository;
    private static final Logger logger = LoggerFactory.getLogger(VTopicRepository.class);

    @Autowired
    public VTopicService(VTopicRepository vtopicRepository) {
        this.vtopicRepository = vtopicRepository;
    }

    @Override
    public Page<VTopic> getPagedTopics(Pageable pageable, Filter filter) {
        SpecificationBuilder specificationBuilder = new SpecificationBuilder();
        Specification<VTopic> spec = null;
        if (filter != null && !filter.getFilters().isEmpty()) {
            try {
                spec = specificationBuilder.getSpecification(filter);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return vtopicRepository.findAll(spec, pageable);
    }
}
