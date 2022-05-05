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
    // in caso di operatore IN
    List<String> values;
    List<Filter> filters;
}
