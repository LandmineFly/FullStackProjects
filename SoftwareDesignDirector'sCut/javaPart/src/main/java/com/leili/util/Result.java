package com.leili.util;

public class Result<E> {
    public Integer code;
    public String message;
    public E data;
    public Integer totalCount;

    public Result(Integer code, String message, E data, Integer totalCount) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.totalCount = totalCount;
    }

    public static <E> Result<E> ok(E data, Integer totalCount) {
        return new Result<E>(200, "ok", data, totalCount);
    }

    public static <E> Result<E> error(String message, E data, Boolean whetherFindReason) {
        if (whetherFindReason) {
            message = findReason(message);
        }
        return new Result<E>(403, message, data, 0);
    }

    public static String findReason(String message) {
        if (message.contains("a foreign key constraint fails")) {
            return "存在外键引用了欲删除数据的主键，删除失败";
        } else if (message.contains("Duplicate entry")) {
            return "主键重复，添加失败";
        } else if (message.contains("iid_constraint")) {
            return "“编码”输入不符合规范";
        } else if (message.contains("sttpe_constraint")) {
            return "“测站类型”输入不符合规范";
        } else if (message.contains("cannot be null")) {
            return "存在未填的必填项";
        } else {
            return "未知错误";
        }
    }
}
