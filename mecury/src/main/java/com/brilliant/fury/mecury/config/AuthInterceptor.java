package com.brilliant.fury.mecury.config;

import com.alibaba.fastjson.JSONObject;
import com.brilliant.fury.core.base.JsonResp;
import com.brilliant.fury.core.util.GuavaUtil;
import com.brilliant.fury.mecury.model.po.BizAuth;
import com.brilliant.fury.mecury.service.impl.BizAuthServiceImpl;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.Charsets;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author by fury.
 * version 2020/5/15.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static ThreadLocal<BizAuth> threadLocal = new ThreadLocal<>();

    @Resource
    private BizAuthServiceImpl bizAuthService;

    private static byte[] TOKEN_EMPTY = JSONObject.toJSONString(
        new JsonResp<>(HttpServletResponse.SC_BAD_REQUEST, "token不能为空", "")).getBytes(
        Charsets.UTF_8);
    private static byte[] TOKEN_UNAUTHORIZED = JSONObject.toJSONString(
        new JsonResp<>(HttpServletResponse.SC_UNAUTHORIZED, "token未通过授权", "")).getBytes(
        Charsets.UTF_8);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (GuavaUtil.isEmpty(token)) {
            response.addHeader("content-type", "application/json; charset=utf-8");
            response.getOutputStream().write(TOKEN_EMPTY);
            return false;
        }

        BizAuth bizId = bizAuthService.getByToken(token);

        if (null == bizId) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token未通过授权");
            response.addHeader("content-type", "application/json; charset=utf-8");
            response.getOutputStream().write(TOKEN_UNAUTHORIZED);
            return false;
        }
        threadLocal.set(bizId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        threadLocal.remove();
    }

    public static BizAuth getBizAuth() {
        return threadLocal.get();
    }
}
