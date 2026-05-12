package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.vo.Catalog2Vo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.CategoryDao;
import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    //返回带有层级关系的元素
    @Override
    public List<CategoryEntity> listTree() {
        //查询所以的元素(不考虑层级关系)
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //考虑层级关系，组装成树形结构
        List<CategoryEntity> categoryTree = entities.stream(
                //stream()将List转换成流对象，进行流式运算
        ).filter(categoryEntity -> {
            //过滤出父id为0的元素，即一级分类
            return categoryEntity.getParentId() == 0;
        }).map(category -> {
            //给每层分类添加对应的子分类
            category.setChildrenCategories(getChildrenCategories(category, entities));
            return category;
        }).sorted((category1, category2) -> {
            //根据sort属性进行排序（xx1-xx2是升序排列，xx2-xx1是降序排列）
            return (category1.getSort() == null ? 0 : category1.getSort()) - (category2.getSort() == null ? 0 : category2.getSort());
        }).collect(Collectors.toList());
        return categoryTree;
    }

    //通过递归对所有元素层级进行分类
    private List<CategoryEntity> getChildrenCategories(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            //过滤出一级分类下的二级分类，由于递归操作：一级找二级，二级找三级
            return categoryEntity.getParentId().equals(root.getId());
        }).map(categoryEntity -> {
            //给当前分类设置子分类，由于递归操作：一级找二级，二级找三级
            categoryEntity.setChildrenCategories(getChildrenCategories(categoryEntity, all));
            return categoryEntity;
        }).sorted((category1, category2) -> {
            //根据sort属性进行排序（xx1-xx2是升序排列，xx2-xx1是降序排列）
            return (category1.getSort() == null ? 0 : category1.getSort()) - (category2.getSort() == null ? 0 : category2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

    //该方法返回AttrgroupEntity的级联ID
    @Override
    public Long[] getCascadedCategoryId(Long categoryId) {
        //创建集合存放级联ID
        List<Long> cascadedCategoryId = new ArrayList<>();
        getParentCategoryId(categoryId, cascadedCategoryId);
        //翻转集合
        Collections.reverse(cascadedCategoryId);
        return cascadedCategoryId.toArray(new Long[cascadedCategoryId.size()]);
    }

    //获取一级分类的元素
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0); //父分类ID为0就表示这为一级分类
        return this.baseMapper.selectList(wrapper);
    }

    //获取二级分类和二级分类关联的三级分类的元素
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        //获取到所有分类信息
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);
        //获取到一级分类信息
        List<CategoryEntity> level1Categories = getParent_cid(selectList, 0L);
        //Collectors.toMap方法是将List转化成Map
        Map<String, List<Catalog2Vo>> categoryMap = level1Categories.stream().collect(Collectors.toMap(k -> {
            //相当于把二级分类的parent_id作为key
            return k.getId().toString();
        }, v -> {
            List<Catalog2Vo> catalog2Vos = new ArrayList<>();
            //获取到二级分类信息
            List<CategoryEntity> level2Categories = getParent_cid(selectList, v.getId());
            //将二级分类信息、三级分类信息保存起来
            if (level2Categories != null && level2Categories.size() > 0) {
                catalog2Vos = level2Categories.stream().map(l2 -> {
                    //这里保存了二级分类的信息，但是Vo层中的三级分类信息依然没有填写
                    Catalog2Vo catalog2Vo = new Catalog2Vo(l2.getParentId().toString(), l2.getId().toString(), l2.getName(), null);
                    //获取到三级分类信息,并保存
                    List<CategoryEntity> level3Categories = getParent_cid(selectList, l2.getId());
                    if (level3Categories != null && level3Categories.size() > 0) {
                        List<Catalog2Vo.Category3Vo> category3Vos = level3Categories.stream().map(l3 -> {
                            return new Catalog2Vo.Category3Vo(l3.getParentId().toString(), l3.getId().toString(), l3.getName());
                        }).collect(Collectors.toList());
                        catalog2Vo.setCatalog3List(category3Vos);
                    }
                    return catalog2Vo;
                }).collect(Collectors.toList());
            }
            return catalog2Vos;
        }));
        return categoryMap;
    }

    //指定parent_id(父分类ID)，获取关联的分类信息
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parentCId) {
        //如果parentId相等就返回true,就被categoryEntities收集
        List<CategoryEntity> categoryEntities = selectList.stream().filter(categoryEntity ->
                categoryEntity.getParentId().equals(parentCId)
        ).collect(Collectors.toList());
        return categoryEntities;
    }

    //编写方法，让所属分类id递归查找层级关系
    private List<Long> getParentCategoryId(Long categoryId, List<Long> cascadedCategoryId) {
        cascadedCategoryId.add(categoryId);
        //通过所属分类ID，得到javaBean
        CategoryEntity categoryEntity = this.getById(categoryId);
        if (categoryEntity.getParentId() != 0L) { //判断javaBean是否还有上级分层，Long类型的比较带L
            getParentCategoryId(categoryEntity.getParentId(), cascadedCategoryId); //不断向上迭代获取 各层所属分类ID/级联ID
        }
        return cascadedCategoryId;
    }

    //返回所有元素，无过滤条件
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>() //QueryWrapper类可以在SQL语句中添加Where条件
        );

        return new PageUtils(page);
    }

}