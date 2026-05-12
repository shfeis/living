package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrAttrgroupRelationService;
import com.hspedu.hspliving.commodity.service.AttrService;
import com.hspedu.hspliving.commodity.service.CategoryService;
import com.hspedu.hspliving.commodity.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import com.hspedu.hspliving.commodity.service.AttrgroupService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;

import javax.annotation.Resource;

/**
 * 家居商品属性分组
 */
@RestController
@RequestMapping("commodity/attrgroup")
public class AttrgroupController {
    @Autowired //作用类似于@Resource,将组件注入该容器
    private AttrgroupService attrgroupService;
    @Resource
    private AttrService attrService;
    @Resource //将组件注入该容器
    private CategoryService categoryService;
    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    /**
     * 无过滤条件显示所有元素
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrgroupService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 有过滤条件显示元素
     */
    @RequestMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId) {
        PageUtils page = attrgroupService.queryPage(params, categoryId);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        AttrgroupEntity attrgroup = attrgroupService.getById(id);
        //获取到所属分类ID
        Long categoryId = attrgroup.getCategoryId();
        //通过所属分类ID获取到级联ID
        Long[] cascadedCategoryId = categoryService.getCascadedCategoryId(categoryId);
        //把级联ID设置到pojo类中
        attrgroup.setCascadedCategoryId(cascadedCategoryId);
        return R.ok().put("attrgroup", attrgroup);
    }

    /**
     * 根据属性组ID返回该属性组关联的基本属性
     */
    @RequestMapping("{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 通过属性组ID，返回该属性组可以关联的基本属性ID；但还没有进行关联
     */
    @RequestMapping("/{attrgroupId}/attr/allowrelation")
    public R attrAllowRelation(@RequestParam Map<String, Object> params, @PathVariable("attrgroupId") Long attrgroupId) {
        PageUtils page = attrService.getAllowRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }
    /**
     * 根据所属分类ID返回对应的属性组，和属性组关联的基本属性
     */
    @RequestMapping("/{catalogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catalogId") Long categoryId) {
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrgroupService.getAttrGroupWithAttrsByCategoryId(categoryId);
        return R.ok().put("data",attrGroupWithAttrsVos);
    }

    /**
     * 添加属性组
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrgroupEntity attrgroup) {
        attrgroupService.save(attrgroup);
        return R.ok();
    }


    /**
     * 添加/批量添加 属性-属性组关联关系
     */
    @RequestMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntity) {
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntity);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrgroupEntity attrgroup) {
        attrgroupService.updateById(attrgroup);
        return R.ok();
    }

    /**
     * 删除属性组
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        attrgroupService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 删除属性-属性组关联关系，但是属性组依然存在
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities) {
        attrService.deleteRelation(attrAttrgroupRelationEntities);
        return R.ok();
    }

}
