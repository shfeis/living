package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * 商品spu信息介绍
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 12:51:40
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //保存商品介绍图片的方法
    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

