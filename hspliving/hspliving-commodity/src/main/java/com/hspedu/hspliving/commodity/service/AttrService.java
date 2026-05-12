package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.hspedu.hspliving.commodity.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性表
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-30 08:38:59
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //保存商品基本属性且保存商品属性和属性组的关联关系
    void saveAttrAndRelation(AttrEntity attrEntity);

    //增加方法，可以根据 查询关键字、categoryId(所属分类ID)、分页等条件来有选择的显示基本属性元素
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId);

    //增加方法，可以根据 查询关键字、categoryId(所属分类ID)、分页等条件来有选择的显示销售属性元素
    PageUtils querySaleAttrPage(Map<String, Object> params, Long categoryId);

    //增加方法，可以根据属性组ID返回该属性组关联的基本属性
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    //增加方法，可以批量删除 属性-属性组关联关系
    void deleteRelation(AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities);

    /*
     增加方法，获取某个属性组可以关联的基本属性列表
     如果某个属性已经和该属性组关联就不应该获取到，属性组可以关联的属性之间属于同一个所属分类ID
    */
    PageUtils getAllowRelationAttr(Map<String, Object> params, Long attrGroupId);
}

