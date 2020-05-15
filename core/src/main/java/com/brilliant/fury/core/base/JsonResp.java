package com.brilliant.fury.core.base;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * errcode = 0 代表成功
 * errmsg 失败时的错误信息, 可能会改变, 不可作为调用成功的依据.
 *
 * @author fury, version 2017/11/8.
 */
public class JsonResp<T> {

    @JSONField(name = "errcode")
    public final int errcode;
    @JSONField(name = "errmsg")
    public final String errmsg;
    @JSONField(name = "resp")
    public final T resp;

    /**
     * @param errcode 错误码
     * @param errmsg  错误信息
     * @param resp    返回数据.
     */
    @JSONCreator
    public JsonResp(@JSONField(name = "errcode") int errcode,
        @JSONField(name = "errmsg") String errmsg, @JSONField(name = "resp") T resp) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.resp = resp;
    }
}
