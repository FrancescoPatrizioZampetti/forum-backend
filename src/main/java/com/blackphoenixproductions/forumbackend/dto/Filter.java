package com.blackphoenixproductions.forumbackend.dto;


import com.blackphoenixproductions.forumbackend.enums.BooleanOperator;
import com.blackphoenixproductions.forumbackend.enums.QueryOperator;
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
    List<String> values; // in caso di operatore IN e BETWEEN
    @Builder.Default List<Filter> filters;
}
