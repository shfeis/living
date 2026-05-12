package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import com.hspedu.hspliving.commodity.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 家居商品属性分组
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-25 12:51:20
 */
public interface AttrgroupService extends IService<AttrgroupEntity> {
    //无过滤条件显示所有元素
    PageUtils queryPage(Map<String, Object> params);
    //增加方法，可以根据 查询关键字、categoryId(所属分类ID)、分页等条件来有选择的显示元素
    PageUtils queryPage(Map<String, Object> params,Long categoryId);
    //增加方法，根据所属分类ID返回对应的属性组，和属性组关联的基本属性
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId);

}

