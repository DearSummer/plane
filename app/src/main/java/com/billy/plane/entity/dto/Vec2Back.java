package com.billy.plane.entity.dto;

import java.sql.Date;

public class Vec2Back {
    private boolean success;
    private long time;

    public Vec2Back() {
    }

    public Vec2Back(boolean success, long time) {
        this.success = success;
        this.time = time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
