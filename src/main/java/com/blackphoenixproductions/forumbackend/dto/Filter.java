package com.blackphoenixproductions.forumbackend.dto;

import enums.BooleanOperator;
import enums.QueryOperator;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class Filter {
    BooleanOperator booleanOperator;
    QueryOperator queryOperator;
    String field;
    String value;
    // in caso di operatore IN
    List<String> values;
    List<Filter> filters;
}
