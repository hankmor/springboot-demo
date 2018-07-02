package com.belonk.common.base;

import com.belonk.common.util.JsonUtil;

/**
 * 消息定义。
 * Created by sun on 2016/9/10.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class MsgDefinition {
    //~ Static fields ==================================================================================================

    public static final MsgDefinition SUCCESS = new MsgDefinition("0000", "请求成功");

    public static final MsgDefinition EMPTY_ARGUMENTS = new MsgDefinition("4001", "请求参数为空");
    public static final MsgDefinition ILLEGAL_ARGUMENTS = new MsgDefinition("4002", "请求采参数非法");
    public static final MsgDefinition FILE_SIZE_OVER_LIMIT = new MsgDefinition("4301", "文件大小超过限制");
    public static final MsgDefinition FILE_NUMBER_OVER_LIMIT = new MsgDefinition("4302", "文件数量超过限制");
    public static final MsgDefinition FILE_FORMAT_UNSUPPORTED = new MsgDefinition("4310", "文件格式不支持");

    public static final MsgDefinition UNKOWN_ERROR = new MsgDefinition("9999", "系统未知异常");

    private String code;
    private String msg;

    public MsgDefinition(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String codeOf() {
        return this.code;
    }

    public String msgOf() {
        return this.msg;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    //~ Fields =========================================================================================================


    //~ Methods ========================================================================================================
}
