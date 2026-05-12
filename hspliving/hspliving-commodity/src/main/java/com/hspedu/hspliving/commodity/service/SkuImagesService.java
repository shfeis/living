package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-07 09:41:34
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

