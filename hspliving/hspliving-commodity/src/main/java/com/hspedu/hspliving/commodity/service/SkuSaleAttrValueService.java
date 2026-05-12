package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * sku的销售属性/值表
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-07 12:18:46
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

