package com.hspedu.hspliving.commodity.dao;

import com.hspedu.hspliving.commodity.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品spu信息
 * 
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 09:54:40
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {
	//新增方法，可以修改commodity_spu_info表的publish_status字段的值
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("statusCode") int statusCode);
}
