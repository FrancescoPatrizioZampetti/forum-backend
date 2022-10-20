package com.blackphoenixproductions.forumbackend.service;

import com.blackphoenixproductions.forumbackend.dto.Filter;
import org.springframework.data.jpa.domain.Specification;

public interface ISpecificationBuilder {
    <T> Specification<T> getSpecification(Filter filter);
}
