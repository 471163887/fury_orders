package com.brilliant.fury.core.base;

import static com.brilliant.fury.core.base.BaseError.INTERNAL_SERVER_ERROR;
import static com.brilliant.fury.core.base.BaseError.IO_ERROR;
import static com.brilliant.fury.core.base.BaseError.PARAM_WRONG;
import static com.brilliant.fury.core.base.BaseError.SUCCESS;
import static com.brilliant.fury.core.base.BaseError.TIME_OUT;

import com.brilliant.fury.core.util.GuavaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author by fury.
 * version 2018/4/9.
 */
public abstract class BaseController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String EMPTY_STRING = "";

    protected Object dataJson() {
        return dataJson("ok", EMPTY_STRING);
    }

    protected Object dataJson(Object value) {
        return dataJson(EMPTY_STRING, value);
    }

    protected Object dataJson(String message, Object value) {
        return dataJson(SUCCESS.getCode(), message, value);
    }

    protected Object errorJson(String message) {
        return errorJson(INTERNAL_SERVER_ERROR.getCode(), message);
    }

    protected Object errorJson(int errCode, String errMsg) {
        return dataJson(errCode, errMsg, EMPTY_STRING);
    }

    protected Object dataJson(int errCode, String errMsg, Object result) {
        return new JsonResp<>(errCode, errMsg, result);
    }

    protected String toJson(Object object) {
        return JsonUtil.toJson(object);
    }

    @ExceptionHandler(Exception.class)
    protected Object exceptionHandler(Exception ex) {
        String simpleName = ex.getClass().getSimpleName();
        String errMsg = ex.getMessage();
        log.error("[BaseController_exceptionHandler_name]:{} msg:{}", simpleName, errMsg, ex);
        switch (simpleName) {
            case "IllegalArgumentException":
                return errorJson(PARAM_WRONG.getCode(),
                    GuavaUtil.defaultIfEmpty(errMsg, PARAM_WRONG.getMessage()));
            case "DataAccessException":
                BaseError failDbOperation = BaseError.FAIL_DB_OPERATION;
                return errorJson(failDbOperation.getCode(), failDbOperation.getMessage());
            case "IOException":
                return errorJson(IO_ERROR.getCode(), IO_ERROR.getMessage());
            case "HttpException":
                return errorJson(BaseError.UNKNOWN.getCode(), BaseError.UNKNOWN.getMessage());
            case "TimeoutException":
                return errorJson(TIME_OUT.getCode(), TIME_OUT.getMessage());
            case "UnsupportedEncodingException":
                return errorJson(TIME_OUT.getCode(), TIME_OUT.getMessage());
            default:
                return errorJson(INTERNAL_SERVER_ERROR.getCode(), errMsg);
        }
    }

}
