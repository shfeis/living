package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 商品分类表
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-16 07:54:43
 */
@Data
@TableName("commodity_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 父分类 id
	 */
	private Long parentId;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 逻辑删除字段： 0 不显示，1 显示
	 * isShow为0时，该行元素就不在前端页面显示
	 * @TableLogic(value = "1", delval = "0") 设置了注解属性就不用到yaml文件中设置
	 */
	@TableLogic //表示该属性为逻辑字段，没有设置注解属性就要到yaml文件中设置
	private Integer isShow;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 统计单位
	 */
	private String proUnit;
	/**
	 * 商品数量
	 */
	private Integer proCount;

	/**
	 * 层级关系（临时属性）
	 */
	@TableField(exist = false) //增加一个临时属性，该属性不存在于数据库表
	@JsonInclude(JsonInclude.Include.NON_EMPTY) //如果当前层级为空就不返回
	private List<CategoryEntity> childrenCategories;

}
