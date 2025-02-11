package org.example.Dto;

import lombok.Data;

@Data
public class ResponseDto {
    private Boolean success;
    private String message;
    private Object data;
}
