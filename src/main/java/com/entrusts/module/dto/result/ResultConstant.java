package com.entrusts.module.dto.result;

public enum ResultConstant {

    SUCCESS(0L, "ok"),
    CLIENT_ERROR(1L, "客户端请求有误, 请开发人员检查代码"),
    PERMISSION_ERROR(2L, "操作越权"),
    INNER_ERROR(3L, "系统内部错误"),
    EMPTY_PARAM(4L, "缺少参数或参数错误"),
    EMPTY_ENTITY(5L, "请求的数据不存在"),
    SYSTEM_BUSY(6L, "系统繁忙, 请重试!"),
    REPEAT_REQUEST(7L,"重复请求, 已处理"),
    ACCOUNT_NOT_FOUND(1000L, "指定账户不存在"),
    ACCOUNT_MOBILE_NOT_SET(1001L, "此账号尚未绑定手机号"),
    ACCOUNT_OLD_PASSWORD_ERROR(1100L, "旧密码错误"),
    ACCOUNT_NEW_PASSWORD_ERROR(1101L, "新密码不符合规则"),
    ACCOUNT_RESET_ERROR(1102L, "重置密码失败"),
    REGISTER_MOBILE_ERROR(2000L, "用户手机号为空或格式不正确"),
    REGISTER_MOBILE_IS_USED(2001L, "该手机号已被注册"),
    REGISTER_COUNTRY_CODE_ERROR(2002L, "国家代码错误"),
    LOGIN_ACCESS_TOKEN_OVERDUE(3000L, "用户尚未登录或登录已过期"),
    LOGIN_PASSWORD_ERROR(3002L, "密码错误"),
    LOGIN_UNKNOWN_ERROR(3003L, "登录过程中发生未知错误, 请重新尝试"),
    CAPTCHA_NOT_SUCCESS(4000L, "获取验证码失败, 请重新尝试"),
    SMS_LIMIT_OVER(4001L, "短信配额已超出"),
    SMS_TYPE_ERROR(4002L, "短信类型错误"),
    SMS_EMPTY(4003L, "短信验证码不能为空"),
    SMS_ERROR(4004L, "短信验证码错误"),
    SMS_MOBILE_ERROR(4005L, "手机号为空或格式错误"),
    SMS_MOBILE_CODE_ERROR(4006L, "国际区号错误"),
    GRAPH_CODE_ERROR(4100L, "图形验证码错误"),
    PASSWORD_LEN_NOT_ENOUGH(5000L, "密码长度小于6");

    public final long code;
    public final String message;

    public long getFullCode() {
        return this.code != SUCCESS.code?10100000L + this.code:this.code;
    }

    private ResultConstant(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
