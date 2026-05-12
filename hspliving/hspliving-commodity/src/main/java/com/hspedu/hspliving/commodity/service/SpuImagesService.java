package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu商品图片集
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 13:38:58
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //保存spu商品图片集
    void saveImages(Long id, List<String> images);
}

