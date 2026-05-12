package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联表
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-27 08:11:20
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {
    //显示所有元素
    PageUtils queryPage(Map<String, Object> params);

    //保存pojo类的所有属性
    void saveAll(CategoryBrandRelationEntity categoryBrandRelationEntity);

    //根据categoryId(所属商品分类ID)返回对应的brandId(品牌ID)
    List<BrandEntity> getBrandByCategoryId(Long categoryId);
}

