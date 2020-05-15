package com.brilliant.fury.core.base;

import java.util.stream.Stream;

/**
 * @author by fury.
 * version 2017/11/6.
 */
public enum BaseError {

    // 常用 Error Code
    SUCCESS(0, "成功", "success", ""),
    UNKNOWN(-1, "网络异常,请稍后重试", "Network error, please try later.", "網絡異常，請稍後重試"),
    REQ_INVALID(-2, "请求无效", "request is invalid.", ""),
    PARAM_OVERFLOW(-3, "参数值溢出", "request param threshold overflow.", ""),
    PARAM_WRONG(2, "参数错误", "Param wrong", ""),
    SERVICE_ERROR(3, "服务异常", "Service error", ""),
    IO_ERROR(4, "网络传输异常", "IO exception", ""),
    TIME_OUT(5, "请求超时, 请稍后重试", "Time out exception", ""),
    APP_KEY_ILLEGAL(6, "非法的app key", "llegal app key", ""),
    CONDITION_ILLEGAL(7, "不满足执行条件", "Not satisfied with the execution conditions.", ""),
    NOT_LOGIN(9, "用户未登陆", "Not login", ""),
    NO_PERMISSION(14, "用户无操作权限", "User no permission", ""),
    PROCESS_OVER(20, "流程已结束", "The process is over", ""),
    // User register message
    USER_NOT_EXIST(100, "用户不存在", "User not exists", "用戶不存在"),
    USER_EXIST(101, "用户已存在，请直接登录", "User already exists, please login", "用戶已存在，請直接登錄"),
    USER_PHONE_EXIST(102, "该手机号码已经存在", "The phone number already exists.", "該手機號碼已經存在"),
    USER_EMAIL_EXIST(103, "该邮箱已经存在", "The email already exists.", "該郵箱已經存在"),
    USER_EMAIL_TYPE_UNSUPPORTED(104, "邮件类型不支持", "Email type not supported", "郵件類型不支持"),
    USER_THIRDPARTY_EXIST(105, "该第三方账号已经授权", "The account has already authorized", "該第三方賬號已經授權"),
    USER_INVALID_REQUEST_INFO(106, "请求参数错误", "Illegal request parameter", "請求參數錯誤"),
    USER_INVALID_ACCOUNT_TYPE(107, "未知账户类型错误", "Unknown account type", "未知賬戶類型錯誤"),
    USER_IMAGE_UPLOAD_FAIL(108, "上传图片失败", "Upload image fail", "上傳圖片失敗"),
    USER_THIRDPARTY_BOUND(
        109, "该第三方账号已经绑定其他账号", "The account has already bind to other account", "該第三方賬號已經綁定其他賬號"),
    USER_BIND_FAIL(
        110, "绑定失败，该邮箱或手机号已经存在", "Bind fail, phone or email already exist", "綁定失敗，該郵箱或手機號已經存在"),

    WRONG_PASSWORD(301, "用户名或者密码错误", "Username or password incorrect, please try again",
        "用戶名或者密碼錯誤"),
    CAPTCHA_ERROR(302, "验证码错误", "Captcha is invalid, please check and try again", "驗證碼錯誤"),
    CAPTCHA_EXPIRED(303, "验证码已经过期", "Expired captcha", "驗證碼已經過期"),
    EXCEEDS_LIMITS(
        304, "访问次数太频繁，请稍后再试", "Frequent attempts, please try later", "訪問次數太頻繁，請稍後再試"),
    IN_BLACK_LIST(305, "短信黑名单", "Not authorized SMS number", "短信黑名單"),
    SESSION_EXPIRED(
        306, "token不存在或者已经过期", "Token is expired or not exist", "token不存在或者已經過期"),
    CAPTCHA_REFUSE(
        307, "请求验证码频繁，请稍后再试", "Frequent requests, please try later", "請求驗證碼頻繁，請稍後再試"),
    SMS_FAILED(308, "短信发送失败，请稍后重试", "Failed to send SMS, please try later", "短信發送失敗，請稍後重試"),
    CAPTCHA_NOT_PROVIDED(309, "未提供验证码", "Please provide captcha", "未提供驗證碼"),
    CAPTCHA_TYPE_UNKNOWN(310, "未知验证码类型", "Unknown captcha type", "未知驗證碼類型"),
    NEW_PHONE_EXIST(
        311,
        "需要更换的手机号码已经注册",
        "The phone number has already been registered, please use another one",
        "需要更換的手機號碼已經註冊"),
    NEW_EMAIL_EXIST(
        314,
        "需要绑定的email已经存在",
        "The email has already been registered",
        "需要綁定的email已經存在"),
    PHONE_OR_EMAIL_BOUND(
        315,
        "该账号已经绑定phone或email",
        "The account has already bind phone or email",
        "該賬號已經綁定phone或email"),
    USERNAME_EXIST(317, "绑定失败，该用户名已经存在", "Username already exists", "綁定失敗，該用戶名已經存在"),
    ALREADY_BIND_EMAIL(318, "该账号已经绑定邮件", "The account has already bound to an email",
        "該賬號已經綁定郵件"),
    ALREADY_BIND_USERNAME(
        319, "该账号已经绑定用户名", "The account has already bound username", "該賬號已經綁定用戶名"),
    ALREADY_BIND_PHONE(320, "该账号已经绑定手机号码", "The account has already bound phone",
        "該賬號已經綁定手機號碼"),

    // Email
    EMAIL_TOKEN_EXPIRED(321, "token不存在或者已经过期", "Invalid token", "token不存在或者已經過期"),
    EMAIL_TOKEN_USAGE_ERROR(322, "token的用途不能识别", "Invalid token usage", "token的用途不能識別"),
    EMAIL_TOKEN_GENERATE_ERROR(323, "生成token失败", "Failed to generate token", "生成token失敗"),
    EMAIL_SEND_ERROR(
        324, "发送邮件失败，请稍后再试", "Failed to send email， please try again later", "發送郵件失敗，請稍後再試"),

    // Thirdparty oauth info
    THIRD_PARTY_OAUTH_ERROR(
        331, "访问第三方授权接口登录失败", "Failed to login with third-party account", "訪問第三方授權接口登錄失敗"),

    // -4XX Database fail
    FAIL_DB_OPERATION(-401, "数据库访问失败", "Internal error", "數據庫訪問失敗"),
    EMAIL_NOT_RECEIVED(-402, "邮件还未收到，请稍后重试", "Email not received, please try later",
        "郵件還未收到，請稍後重試"),
    DUPLICATE_KEY(-499, "数据已存在, 请勿重复添加.", "Duplicate key exception", ""),
    ID_NOT_EXIST(-498, "记录不存在", "record no exist", ""),
    ALREADY_UPDATE(-497, "请检查数据是否存在或已更新", "record already update", ""),

    // 5XX Server error
    INTERNAL_SERVER_ERROR(500, "内部服务异常, 请稍后重试", "Internal server error", "Internal server error"),
    REQUEST_PROCESSING(
        501, "请求响应中，请勿重新提交", "Request is processing, please don't resubmit", "请求响应中，请勿重新提交"),

    // Inner use do not feedback to user
    INNER_USER_EXIST_NEED_BIND(
        1101,
        "该第三方账号已经存在，但是需要绑定phone或email",
        "Third party account exists, but need bind phone or email",
        "該第三方賬號已經存在，但是需要綁定phone或email"),

    SUB_SESSION_SCOPE_ERROR(1201, "访问受限", "Visit restricted", "訪問受限"),

    ILLEGAL_KEY(58001, "非法秘钥", "Unauthorised secret key", ""),

    MESSAGE_ERROR(58002, "消息投递异常", "message delivery error", "");

    private int code;

    private String message;

    private String enMessage;

    private String chtMessage;

    BaseError(int code, String message, String enMessage, String chtMessage) {
        this.code = code;
        this.message = message;
        this.enMessage = enMessage;
        this.chtMessage = chtMessage;
    }

    /**
     * code -> BaseError.
     */
    public static BaseError codeof(int code) {
        return Stream.of(BaseError.values()).filter(type -> type.getCode() == code).findFirst()
            .orElse(UNKNOWN);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
