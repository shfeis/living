package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.CategoryBrandRelationEntity;
import com.hspedu.hspliving.commodity.service.CategoryBrandRelationService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;


/**
 * 品牌分类关联表
 *
 * @author hsp
 * @email hsp@gmail.com
 * @date 2026-03-27 08:11:20
 */
@RestController
@RequestMapping("commodity/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 返回所有元素列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据brandId,返回品牌和分类关联的记录
     */
    @RequestMapping("/brand/list")
    public R categoryBrandListByBrandId(@RequestParam("brandId") Long brandId) { //获取地址栏或表单提交的数据
        // QueryWrapper类可以给SQL语句添加过滤条件，eq()就是WHERE。 SELECT * FROM `commodity_category_brand_relation` WHERE brand_id=brandId
        List<CategoryBrandRelationEntity> data =
                categoryBrandRelationService.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        return R.ok().put("data", data);
    }

    /**
     * 根据id，返回元素
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 根据categoryID，返回品牌信息
     */
    @RequestMapping("/brands/list")
    public R relationBrandList(@RequestParam(value = "catId") Long categoryId) {
        List<BrandEntity> brandEntities = categoryBrandRelationService.getBrandByCategoryId(categoryId);
        return R.ok().put("data", brandEntities);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        /*
         service层的save方法只能添加brandId和categoryId
		 categoryBrandRelationService.save(categoryBrandRelation);
        */

        //使用saveAll方法添加所有属性
        categoryBrandRelationService.saveAll(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
