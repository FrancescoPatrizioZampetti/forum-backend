package com.blackphoenixproductions.forumbackend.repository.specification;

import dto.Filter;
import enums.BooleanOperator;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            default:
                throw new RuntimeException("Operazione non supportata.");
        }
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if(fieldType.isAssignableFrom(Double.class)){
            return Double.valueOf(value);
        }else if(fieldType.isAssignableFrom(Integer.class)){
            return Integer.valueOf(value);
        }else if(fieldType.isAssignableFrom(Date.class)){
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return simpleDateFormat.parse(value);
            } catch (ParseException e){
                throw new RuntimeException("Formato di data non supportato.");
            }
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
