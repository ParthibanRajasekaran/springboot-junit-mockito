package com.parthibanrajasekaran.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class CourseResponse {

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private String id;


}
