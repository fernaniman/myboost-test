package org.example.Dto;

import lombok.Data;
import org.example.Entity.PoHeaderEntity;

@Data
public class ResponseDto {
    private Boolean success;
    private String message;
    private Object data;

    public ResponseDto() {}

    public ResponseDto(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
