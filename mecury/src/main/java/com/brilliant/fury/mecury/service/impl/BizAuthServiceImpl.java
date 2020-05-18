package com.brilliant.fury.mecury.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brilliant.fury.mecury.mapper.BizAuthMapper;
import com.brilliant.fury.core.model.po.BizAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by fury.
 * version 2020/5/15.
 */
@Slf4j
@Service
public class BizAuthServiceImpl extends ServiceImpl<BizAuthMapper, BizAuth> implements
    IService<BizAuth> {

    public BizAuth getByToken(String token) {
        QueryWrapper<BizAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        return getOne(queryWrapper);
    }
}
