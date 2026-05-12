package com.hspedu.hspliving.commodity.dao;

import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性和商品属性组的关联表
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-30 09:49:31
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {
	//增加批量删除 属性-属性组关联关系 的方法
    public void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
