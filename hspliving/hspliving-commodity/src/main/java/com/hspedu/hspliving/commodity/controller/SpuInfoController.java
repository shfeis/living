package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

import com.hspedu.hspliving.commodity.vo.SpuSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.SpuInfoEntity;
import com.hspedu.hspliving.commodity.service.SpuInfoService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;


/**
 * 商品spu信息
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-04-03 09:54:40
 */
@RestController
@RequestMapping("commodity/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存spu基本信息
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVO spuSaveVO) {
        //保存spuInfoEntity属性信息
        spuInfoService.saveSpuInfo(spuSaveVO);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 修改商品状态：上架
     */
    @RequestMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId) {
        spuInfoService.up(spuId);
        return R.ok();
    }

    /**
     * 修改商品状态：下架
     */
    @RequestMapping("/{spuId}/down")
    public R spuDown(@PathVariable("spuId") Long spuId) {
        spuInfoService.down(spuId);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
