package com.blackphoenixproductions.forumbackend.repository.specification;

import com.blackphoenixproductions.forumbackend.dto.Filter;
import com.blackphoenixproductions.forumbackend.enums.BooleanOperator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

public class SpecificationBuilder {

    public <T> Specification<T> getSpecification(Filter filter){
        Specification<T> specification = null;
        // figlio
        if (filter.getBooleanOperator() == null
                && !filter.getValue().isEmpty()) {
            return createSpecification(filter);
        // padre
        } else if (filter.getBooleanOperator() != null) {
            specification = where(getSpecification(filter.getFilters().remove(0)));
            if (!filter.getFilters().isEmpty()) {
                if (filter.getBooleanOperator().equals(BooleanOperator.AND)) {
                    specification = specification.and(getSpecification((filter.getFilters().remove(0))));
                } else if (filter.getBooleanOperator().equals(BooleanOperator.OR)) {
                    specification = specification.or(getSpecification((filter.getFilters().remove(0))));
                }
            }
        }
        return specification;
    }


    public <T> Specification<T> createSpecification(Filter input) {
        switch (input.getQueryOperator()){
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case NOT_EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                                (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                                (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()), "%"+input.getValue()+"%");
            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                                .value(castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues()));
            case BETWEEN:
                return (root, query, criteriaBuilder) ->
                        getBetweenPredicate(input, root, criteriaBuilder);
            case EQUALS_TRUNC_DATETIME:
                return (root, query, criteriaBuilder) -> {
                    LocalDate paramDate = LocalDate.parse(input.getValue());
                    LocalDateTime startDate = LocalDateTime.of(paramDate.getYear(), paramDate.getMonth(), paramDate.getDayOfMonth(), 0, 0, 0, 0);
                    LocalDateTime endDate = LocalDateTime.of(paramDate.getYear(), paramDate.getMonth(), paramDate.getDayOfMonth(), 23, 59, 59, 999999999);
                    return criteriaBuilder.between(root.get(input.getField()),
                                startDate,
                                endDate);
                };

            default:
                throw new RuntimeException("Operazione non supportata.");
        }
    }

    private <T> Predicate getBetweenPredicate(Filter input, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate resultPredicate = null;
        if(root.get(input.getField()).getJavaType().isAssignableFrom(LocalDateTime.class)) {
            resultPredicate =  criteriaBuilder.between(root.get(input.getField()),
                    LocalDateTime.parse(input.getValues().get(0)),
                    LocalDateTime.parse(input.getValues().get(1)));
        } else if (root.get(input.getField()).getJavaType().isAssignableFrom(LocalDate.class)){
            resultPredicate =  criteriaBuilder.between(root.get(input.getField()),
                    LocalDate.parse(input.getValues().get(0)),
                    LocalDate.parse(input.getValues().get(1)));
        } else if (root.get(input.getField()).getJavaType().isAssignableFrom(Integer.class)){
            resultPredicate =  criteriaBuilder.between(root.get(input.getField()),
                    Integer.valueOf(input.getValues().get(0)),
                    Integer.valueOf(input.getValues().get(1)));
        } else if (root.get(input.getField()).getJavaType().isAssignableFrom(Double.class)){
            resultPredicate =  criteriaBuilder.between(root.get(input.getField()),
                    Double.valueOf(input.getValues().get(0)),
                    Double.valueOf(input.getValues().get(1)));
        } else {
            throw new RuntimeException("Tipo non gestito per operazione BETWEEN.");
        }
        return resultPredicate;
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if(fieldType.isAssignableFrom(Double.class)){
            return Double.valueOf(value);
        }else if(fieldType.isAssignableFrom(Integer.class)){
            return Integer.valueOf(value);
        }else if(fieldType.isAssignableFrom(LocalDate.class)){
            return LocalDate.parse(value);
        }else if(fieldType.isAssignableFrom(LocalDateTime.class)){
            return LocalDateTime.parse(value);
        }
        return value;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}
