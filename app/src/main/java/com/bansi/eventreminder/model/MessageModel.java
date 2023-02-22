package com.bansi.eventreminder.model;

public class   MessageModel {
    String msg;
    int category_id,msg_id;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }
    @Override
    public String toString() {
        return getMsg();
    }
}
