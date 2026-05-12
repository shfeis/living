package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

import com.hspedu.hspliving.commodity.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性表
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-30 08:38:59
 */
@RestController
@RequestMapping("commodity/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 通过过滤条件查询基本属性列表
     */
    @RequestMapping("/base/list/{categoryId}")
    public R baseAttrList(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId) {
        //PageUtils page = attrService.queryPage(params);
        PageUtils page = attrService.queryBaseAttrPage(params, categoryId);
        return R.ok().put("page", page);
    }

    /**
     * 通过过滤条件查询销售属性列表
     */
    @RequestMapping("/sale/list/{categoryId}")
    public R saleAttrList(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId) {
        PageUtils page = attrService.querySaleAttrPage(params, categoryId);
        return R.ok().put("page", page);
    }

    @Resource //将组件注入该容器
    private CategoryService categoryService;

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrEntity attr = attrService.getById(attrId);
        //获取到所属分类ID
        Long categoryId = attr.getCategoryId();
        //通过所属分类ID获取到级联ID
        Long[] cascadedCategoryId = categoryService.getCascadedCategoryId(categoryId);
        //把级联ID设置到pojo类中
        attr.setCascadedCategoryId(cascadedCategoryId);
        return R.ok().put("attr", attr);
    }

    /**
     * 添加元素 且 保存属性-属性组关联关系
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrEntity attr) {
//        attrService.save(attr); 该方法只能添加元素
        attrService.saveAttrAndRelation(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr) {
        attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
