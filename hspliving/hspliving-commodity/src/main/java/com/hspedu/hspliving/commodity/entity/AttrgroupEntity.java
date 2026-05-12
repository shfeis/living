package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;

/**
 * 家居商品属性分组
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-25 12:51:20
 */
@Data //lombok注解，设置get、set、toString等方法
@TableName("commodity_attrgroup")
public class AttrgroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 组名
	 */
	private String name;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 说明
	 */
	private String description;
	/**
	 * 组图标
	 */
	private String icon;
	/**
	 * 所属分类 id
	 */
	private Long categoryId;
	/**
	 * 级联ID，保存当前分组从第一级到最后一级的所属分类Id [第一层分类ID, 第二层分类ID, 第三层分类ID]
	 */
	@TableField(exist = false) //增加一个临时属性，该属性不存在于数据库表
	private Long[] cascadedCategoryId;

}
