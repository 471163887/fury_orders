package com.brilliant.fury.mecury.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brilliant.fury.mecury.mapper.BuyerAddrMapper;
import com.brilliant.fury.mecury.model.po.BuyerAddr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by fury.
 * version 2020/5/16.
 */
@Slf4j
@Service
public class BuyerAddrServiceImpl extends ServiceImpl<BuyerAddrMapper,
    BuyerAddr> implements IService<BuyerAddr> {

}
