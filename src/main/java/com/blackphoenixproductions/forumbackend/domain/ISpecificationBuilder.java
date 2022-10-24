package com.blackphoenixproductions.forumbackend.domain;

import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.Filter;
import org.springframework.data.jpa.domain.Specification;

public interface ISpecificationBuilder {
    <T> Specification<T> getSpecification(Filter filter);
}
