package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.adapters.api.dto.Filter;
import org.springframework.data.jpa.domain.Specification;

public interface ISpecificationBuilder {
    <T> Specification<T> getSpecification(Filter filter);
}
