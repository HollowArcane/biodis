package com.toolkit.spring.util;

import org.springframework.http.ResponseEntity;

public class APIResponse
{

    private String status;
    private int code;
    private Object data;
    private Object error;

    private APIResponse(String status, int code, Object data, Object error)
    {
        this.status = status;
        this.code = code;
        this.data = data;
        this.error = error;
    }

    public static ResponseEntity<APIResponse> success(int code, Object data)
    { return ResponseEntity.status(code).body(new APIResponse("success", code, data, null)); } 

    public static <T> ResponseEntity<APIResponse> error(int code, T error)
    { return ResponseEntity.status(code).body(new APIResponse("error", code, null, error)); } 

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public Object getError() {
        return error;
    }
    
}
