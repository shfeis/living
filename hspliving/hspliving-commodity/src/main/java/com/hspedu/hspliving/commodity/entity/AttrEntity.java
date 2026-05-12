package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品属性表
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-30 08:38:59
 */
@Data
@TableName("commodity_attr")
public class AttrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 属性 id
	 */
	@TableId
	private Long attrId;
	/**
	 * 属性名
	 */
	private String attrName;
	/**
	 * 是否需要检索[0-不需要，1-需要]
	 */
	private Integer searchType;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 可选值列表[用逗号分隔]
	 */
	private String valueSelect;
	/**
	 * 属性类型[0-销售属性，1-基本属性]
	 */
	private Integer attrType;
	/**
	 * 启用状态[0 - 禁用，1 - 启用]
	 */
	private Long enable;
	/**
	 * 所属分类
	 */
	private Long categoryId;
	/**
	 * 快速展示【是否展示在介绍上；0-否 1-是】
	 */
	private Integer showDesc;

	/**
	 * 增加临时属性，表示该基本属性关联的属性组ID
	 */
	@TableField(exist = false)
	private Long attrGroupId;

	/**
	 * 级联ID，保存当前分组从第一级到最后一级的所属分类Id [第一层分类ID, 第二层分类ID, 第三层分类ID]
	 */
	@TableField(exist = false) //增加一个临时属性，该属性不存在于数据库表
	private Long[] cascadedCategoryId;

}
