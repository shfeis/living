package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SpuInfoEntity;
import com.hspedu.hspliving.commodity.vo.SpuSaveVO;

import java.util.Map;

/**
 * 商品spu信息
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 09:54:40
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //用于保存Spu基本信息
    void saveSpuInfo(SpuSaveVO spuSaveVO);

    //用于执行SQL语句，将spu基本信息保存到数据库表
    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    //通过前端传入的检索条件进行分页查询
    PageUtils queryPageByCondition(Map<String, Object> params);
    //用于修改商品为上架状态
    void up(Long spuId);
    //用于修改商品为下架状态
    void down(Long spuId);
}

