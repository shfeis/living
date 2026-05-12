package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.dao.BrandDao;
import com.hspedu.hspliving.commodity.dao.CategoryDao;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.CategoryBrandRelationDao;
import com.hspedu.hspliving.commodity.entity.CategoryBrandRelationEntity;
import com.hspedu.hspliving.commodity.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandDao brandDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CategoryBrandRelationDao categoryBrandRelationDao;
    @Resource
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    //保存pojo类的所有属性
    @Override
    public void saveAll(CategoryBrandRelationEntity categoryBrandRelationEntity) {
        //获取前端提交的brandId 和 categoryId
        Long brandId = categoryBrandRelationEntity.getBrandId();
        Long categoryId = categoryBrandRelationEntity.getCategoryId();
        //通过ID，获取到pojo类
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);
        //通过pojo类获取到name
        String brandName = brandEntity.getName();
        String categoryName = categoryEntity.getName();
        //将名字保存到CategoryBrandRelationEntity类
        categoryBrandRelationEntity.setBrandName(brandName);
        categoryBrandRelationEntity.setCategoryName(categoryName);
        //4. 调用保存
        this.save(categoryBrandRelationEntity);
    }

    //根据categoryId(所属商品分类ID)返回对应的brand(品牌)
    @Override
    public List<BrandEntity> getBrandByCategoryId(Long categoryId) {
        //根据分类ID，获取到所属的 分类-品牌关联关系
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("category_id", categoryId));

        List<BrandEntity> brandEntities = categoryBrandRelationEntities.stream().map((item) -> {
            //根据 分类-品牌关联关系 获取品牌ID
            Long brandId = item.getBrandId();
            //根据品牌ID获取品牌信息
            return brandService.getById(brandId);
        }).collect(Collectors.toList());

        return brandEntities;
    }

}