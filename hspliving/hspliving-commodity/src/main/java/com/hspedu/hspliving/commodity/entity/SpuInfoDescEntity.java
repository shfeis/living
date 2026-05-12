package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品spu信息介绍
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 12:51:40
 */
@Data
@TableName("commodity_spu_info_desc")
public class SpuInfoDescEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId(type = IdType.INPUT) //表示该主键不是自动生成的/非自增长，而是从程序中获取
	private Long spuId;
	/**
	 * 商品介绍图片
	 */
	private String decript;

}
