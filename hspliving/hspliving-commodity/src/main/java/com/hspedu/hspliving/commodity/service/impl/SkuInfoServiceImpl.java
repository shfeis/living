package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.dao.SpuInfoDao;
import com.hspedu.hspliving.commodity.entity.SpuInfoEntity;
import com.hspedu.hspliving.commodity.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SkuInfoDao;
import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import com.hspedu.hspliving.commodity.service.SkuInfoService;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SpuInfoDao spuInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //保存SKU的基本信息
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    //根据管理员的检索条件查询分页信息---》人人快速开发平台
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        //获取检索关键字,增加过滤条件
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) { //判断管理员是否输入了检索关键字
            //如果key是id就为=；如果key为名称就为like
            wrapper.and(obj -> {
                obj.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        //获取所属分类，增加过滤条件
        String catalogId = (String) params.get("catalogId");
        if (StringUtils.isNotBlank(catalogId) && !catalogId.equals("0")) { //判断状态是否为空
            wrapper.eq("catalog_id", catalogId);
        }
        //获取品牌，增加过滤条件
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !brandId.equals("0")) { //判断状态是否为空
            wrapper.eq("brand_id", brandId);
        }
        //获取价格区间，增加过滤条件
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        if (StringUtils.isNotBlank(min)) {
            wrapper.ge("price", min); //大于等于最小值
        }
        if (StringUtils.isNotBlank(max)) {
            wrapper.le("price", max); //小于等于最大值
        }
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    //根据用户的检索条件返回查询分页信息给list.html，再通过thymeleaf进行动态渲染 ---》前端页
    @Override
    public SearchResult querySearchPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        //只显示上架的spu，添加过滤条件
        List<SpuInfoEntity> spuInfoEntities = spuInfoDao.selectList(new QueryWrapper<SpuInfoEntity>().eq("publish_status", 1));
        List<Long> spuInfoEntitiesId = spuInfoEntities.stream().map(spuInfoEntity -> {
            return spuInfoEntity.getId();
        }).collect(Collectors.toList());
        if (spuInfoEntitiesId != null && spuInfoEntitiesId.size() > 0) {
            wrapper.in("spu_id", spuInfoEntitiesId);
        } else { //没有任何上架的商品，直接返回
            SearchResult searchResult = new SearchResult();
            searchResult.setPageNum(1);
            searchResult.setTotalPages(0);
            searchResult.setPageNavs(new ArrayList<>());
            searchResult.setTotal(0);
            searchResult.setCommodity(new ArrayList<>());
            return searchResult;
        }
        //获取检索条件,添加过滤条件
        String keyword = (String) params.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            //如果keyword是id就为=；如果keyword为名称就为like
            wrapper.and(obj -> {
                obj.eq("sku_id", keyword).or().like("sku_name", keyword);
            });
        }
        //获取分类ID，添加过滤条件
        String catalog3Id = (String) params.get("catalog3Id");
        if (StringUtils.isNotBlank(catalog3Id) && !catalog3Id.equals("0")) {
            wrapper.eq("catalog_id", catalog3Id);
        }
        //获取品牌，添加过滤条件
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !catalog3Id.equals("0")) {
            wrapper.eq("brand_id", brandId);
        }
        params.put("limit", "4"); //每页显示4条记录
        PageUtils pageUtils = new PageUtils(this.page(new Query<SkuInfoEntity>().getPage(params), wrapper));

        //将查询信息存放到Vo对象
        SearchResult searchResult = new SearchResult();
        searchResult.setCommodity((List<SkuInfoEntity>) pageUtils.getList());
        searchResult.setTotal(pageUtils.getTotalCount());
        int totalPage = pageUtils.getTotalPage();
        searchResult.setTotalPages(totalPage);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            pageNavs.add(i);
        }
        searchResult.setPageNavs(pageNavs);
        searchResult.setPageNum(pageUtils.getCurrPage());
        //将用户检索关键字保存到SearchResult
        searchResult.setKeyword(keyword == null ? "" : keyword);
        return searchResult;
    }

}