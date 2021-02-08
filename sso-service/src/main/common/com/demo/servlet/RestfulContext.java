package com.demo.servlet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestfulContext {

    private int httpStatus;

    private String responseInfo;
}
