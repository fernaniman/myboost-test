package org.example.Utils;

import org.example.Dto.ResponseDto;

public class ResponseUtils {

    public final static String SUCCESS_GET = "Success get ";
    public final static String SUCCESS_CREATE = "Success create ";
    public final static String SUCCESS_UPDATE = "Success update ";
    public final static String SUCCESS_DELETE = "Success delete ";
    public final static String NOT_FOUND = " not found!";
    public final static String EXISTS = " already exists!";

    public static ResponseDto getResponse(Boolean success, String message, Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(success);
        responseDto.setMessage(message);
        responseDto.setData(data);
        return responseDto;
    }

}
