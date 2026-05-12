package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu基本属性值
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 14:31:25
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //用于保存商品基本属性
    void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntities);
}

