package com.theherdonline.db.entity;

public class ResError {
    private String msg = "unknown";
    private String error = "unknown";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return msg + '\n' + error;
    }
}
