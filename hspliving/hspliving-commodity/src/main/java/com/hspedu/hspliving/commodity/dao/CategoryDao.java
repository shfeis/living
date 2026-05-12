package com.hspedu.hspliving.commodity.dao;

import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类表
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-16 07:54:43
 */
@Mapper //如果没有该注解，就要在主启动类上使用@MapperScan指定Mapper类的地址
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
