package com.theherdonline.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.theherdonline.db.entity.ResError;

import static com.theherdonline.api.Status.ERROR;
import static com.theherdonline.api.Status.LOADING;
import static com.theherdonline.api.Status.LOADINGMORE;
import static com.theherdonline.api.Status.NOCHANGE;
import static com.theherdonline.api.Status.STALE;
import static com.theherdonline.api.Status.SUCCESS;


/**
 * Created by freddie on 16/05/18.
 */

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public  Integer errorCode = 0;

    @Nullable
    public final T data;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message, @Nullable Integer errorCode) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg,  @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> error(Integer code, String msg,  @Nullable T data) {
        return new Resource<>(ERROR, data, msg,code);
    }

    public static <T> Resource<T> error(Integer code, ResError msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg.getError(),code);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public static <T> Resource<T> loading(@Nullable T data, String message) {
        return new Resource<>(LOADING, data, message);
    }

    public static <T> Resource<T> loadingMore(@Nullable T data) {
        return new Resource<>(LOADINGMORE, data, null);
    }

    public static <T> Resource<T> nochange(@Nullable T data) {
        return new Resource<>(NOCHANGE, data, null);
    }



    public static <T> Resource<T> stale(@Nullable T data) {
        return new Resource<>(STALE, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;

        if (status != resource.status) {
            return false;
        }
        if (message != null ? !message.equals(resource.message) : resource.message != null) {
            return false;
        }
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
