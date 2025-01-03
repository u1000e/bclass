package com.kh.bclass.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDetail {
    private Date timestamp;
    private String message;
    private String details;

}
