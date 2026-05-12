package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.vo.Catalog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品分类表
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-16 07:54:43
 */
public interface CategoryService extends IService<CategoryEntity> {
    //返回带有层级关系的元素
    List<CategoryEntity> listTree();

    PageUtils queryPage(Map<String, Object> params);

    //该方法返回AttrgroupEntity的级联ID
    Long[] getCascadedCategoryId(Long categoryId);
    //获取一级分类的元素
    List<CategoryEntity> getLevel1Categories();
    //获取二级分类和二级分类关联的三级分类的元素
    Map<String,List<Catalog2Vo>> getCatalogJson();
}

