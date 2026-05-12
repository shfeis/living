package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

import com.hspedu.common.valid.SaveGroup;
import com.hspedu.common.valid.UpdateGroup;
import com.hspedu.common.valid.UpdateIsShowGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.service.BrandService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;

/**
 * 家居品牌
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-18 08:19:35
 */
@RestController
@RequestMapping("commodity/brand")
public class BrandController {
    @Autowired //作用类似于@Resource,将组件注入该容器
    private BrandService brandService;

    /**
     * 查询所有列表元素
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 查询指定id的列表元素
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		BrandEntity brand = brandService.getById(id);
        return R.ok().put("brand", brand);
    }

    /**
     * 新增方法
     * @Validated({SaveGroup.class}) 启用Entity类的属性数据校验，属于新增组
     */
    @RequestMapping("/save")
    public R save(@Validated({SaveGroup.class}) @RequestBody BrandEntity brand){
            brandService.save(brand);
            return R.ok();
    }

    /**
     * 修改元素的方法
     * @Validated({UpdateGroup.class}) 启用Entity类的属性数据校验，属于修改组
     */
    @RequestMapping("/update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand){
		brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 修改 isShow属性/switch开关 的方法
     * @Validated({UpdateIsShowGroup.class}) 启用Entity类的属性数据校验，属于switch开关修改组
     */
    @RequestMapping("/update/isshow")
    public R updateIsShow(@Validated({UpdateIsShowGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		brandService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
