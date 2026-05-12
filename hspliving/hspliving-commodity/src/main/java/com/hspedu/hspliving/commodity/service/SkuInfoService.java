package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import com.hspedu.hspliving.commodity.vo.SearchResult;

import java.util.Map;

/**
 * sku信息
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-07 08:29:13
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //保存SKU的基本信息
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);
    //根据管理员的检索条件查询分页信息
    PageUtils queryPageByCondition(Map<String, Object> params);
    //根据用户的检索条件返回查询分页信息给list.html，再通过thymeleaf进行动态渲染
    SearchResult querySearchPageByCondition(Map<String, Object> params);
}

