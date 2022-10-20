package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.ports.IVTopicService;
import com.blackphoenixproductions.forumbackend.domain.ports.ISpecificationBuilder;
import com.blackphoenixproductions.forumbackend.domain.dto.Filter;
import com.blackphoenixproductions.forumbackend.domain.model.VTopic;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.VTopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class VTopicService implements IVTopicService {

    private final VTopicRepository vtopicRepository;
    private final ISpecificationBuilder specificationBuilder;
    private static final Logger logger = LoggerFactory.getLogger(VTopicRepository.class);

    @Autowired
    public VTopicService(VTopicRepository vtopicRepository, ISpecificationBuilder specificationBuilder) {
        this.vtopicRepository = vtopicRepository;
        this.specificationBuilder = specificationBuilder;
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
