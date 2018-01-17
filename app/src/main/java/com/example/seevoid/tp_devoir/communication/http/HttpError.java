package com.example.seevoid.tp_devoir.communication.http;

/**
 * Created by seevoid on 14/01/18.
 */

public class HttpError {

    private String message;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
}
