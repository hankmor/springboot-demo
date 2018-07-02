package com.belonk.common.base;

import lombok.Data;

/**
 * 结果消息。
 * <p/>
 * Created by sun on 2016/9/10.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Data
public class ResultMsg<T> {
    //~ Static fields ==================================================================================================
    /**
     * 消息类型：成功
     */
    public static final String MESSAGE_TYPE_SUCCESS = "success";

    /**
     * 消息类型：信息
     */
    public static final String MESSAGE_TYPE_INFO = "info";

    /**
     * 消息类型：警告
     */
    public static final String MESSAGE_TYPE_WARNING = "warning";

    /**
     * 消息类型：错误
     */
    public static final String MESSAGE_TYPE_ERROR = "error";

    //~ Fields =========================================================================================================

    private String rtnCode;
    private String rtnMsg;
    private T data;
    private String type;

    //~ Methods ========================================================================================================

    private ResultMsg() {

    }

    private ResultMsg(String code, String msg, String type, T data) {
        this.rtnCode = code;
        this.rtnMsg = msg;
        this.type = type;
        this.data = data;
    }

    public static <T> ResultMsg<T> success(T data) {
        return new ResultMsg<>(MsgDefinition.SUCCESS.codeOf(), MsgDefinition.SUCCESS.msgOf(), MESSAGE_TYPE_SUCCESS, data);
    }

    public static <T> ResultMsg<T> success(T data, String msg) {
        return new ResultMsg<>(MsgDefinition.SUCCESS.codeOf(), msg, MESSAGE_TYPE_SUCCESS, data);
    }

    public static <T> ResultMsg<T> error(String code, String msg) {
        return new ResultMsg<>(code, msg, MESSAGE_TYPE_ERROR, null);
    }

    public static <T> ResultMsg<T> error(String code, String msg, T data) {
        return new ResultMsg<>(code, msg, MESSAGE_TYPE_ERROR, data);
    }

    public static <T> ResultMsg<T> error(MsgDefinition msgDefinition) {
        return new ResultMsg<>(msgDefinition.codeOf(), msgDefinition.msgOf(), MESSAGE_TYPE_ERROR, null);
    }

    public static <T> ResultMsg<T> error(MsgDefinition msgDefinition, T data) {
        return new ResultMsg<>(msgDefinition.codeOf(), msgDefinition.msgOf(), MESSAGE_TYPE_ERROR, data);
    }

    public static <T> ResultMsg<T> warning(String code, String msg, T data) {
        return new ResultMsg<>(code, msg, MESSAGE_TYPE_WARNING, data);
    }

    public static <T> ResultMsg<T> warning(String code, String msg) {
        return new ResultMsg<>(code, msg, MESSAGE_TYPE_WARNING, null);
    }

    public static <T> ResultMsg<T> warning(MsgDefinition msgDefinition, T data) {
        return new ResultMsg<>(msgDefinition.codeOf(), msgDefinition.msgOf(), MESSAGE_TYPE_WARNING, data);
    }

    public static <T> ResultMsg<T> info(String code, String msg) {
        return new ResultMsg<>(code, msg, MESSAGE_TYPE_INFO, null);
    }

    public static <T> ResultMsg<T> info(String code, String msg, T data) {
        return new ResultMsg<>(code, msg, MESSAGE_TYPE_INFO, data);
    }

    public static <T> ResultMsg<T> info(MsgDefinition msgDefinition, T data) {
        return new ResultMsg<>(msgDefinition.codeOf(), msgDefinition.msgOf(), MESSAGE_TYPE_INFO, data);
    }

    public static <T> ResultMsg<T> info(MsgDefinition msgDefinition) {
        return new ResultMsg<>(msgDefinition.codeOf(), msgDefinition.msgOf(), MESSAGE_TYPE_INFO, null);
    }
}
