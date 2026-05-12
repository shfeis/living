package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.entity.*;
import com.hspedu.hspliving.commodity.service.*;
import com.hspedu.hspliving.commodity.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private AttrService attrService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    //返回所有元素
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );
        return new PageUtils(page);
    }

    //通过前端传入spuSaveVo来保存spu信息、spu商品介绍图片、spu图片集、spu基本属性、sku信息、sku图片、sku销售属性
    @Override
    @Transactional //开启事务处理，防止多次数据表操作产生脏数据
    public void saveSpuInfo(SpuSaveVO spuSaveVO) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        //spring的工具类，可以拷贝spuSaveVO属性数据到spuInfoEntity中，有选择的拷贝spu信息
        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
        //设置创建时间和修改时间
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        //添加spu商品信息，对commodity_spu_info表进行操作
        this.saveBaseSpuInfo(spuInfoEntity);
        //拷贝spu商品介绍图片
        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        if (decript.size() == 0) {  //为0说明没有上传商品介绍图片，设置默认图片路径
            spuInfoDescEntity.setDecript("http://localhost/default.jpg");
        } else {
            //将集合中的图片路径存放到字符串中，用,分隔
            spuInfoDescEntity.setDecript(String.join(",", decript));
        }
        //添加spu商品信息介绍，对commodity_spu_info_desc表进行操作
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //添加商品图集，对commodity_spu_images表进行操作
        List<String> images = spuSaveVO.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        //添加spu商品基本属性,对commodity_product_attr_value表进行操作
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setSpuId(spuInfoDescEntity.getSpuId());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setAttrSort(0);
            //通过属性id,获取到属性名
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(productAttrValueEntities);
        //添加sku信息，对commodity_sku_info表进行操作
        List<Skus> skus = spuSaveVO.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(sku -> {
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setSpuId(spuInfoDescEntity.getSpuId());
                skuInfoEntity.setSkuName(sku.getSkuName());
                //修改图片，如果用户没有上传就返回默认图片
                String defaultImg = "http://localhost/default.jpg";
                for (Images image : sku.getImages()) {
                    if (image.getDefaultImg() == 1) { //如果为1，就不是默认图片
                        defaultImg = image.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setPrice(sku.getPrice());
                skuInfoEntity.setSkuSubtitle(sku.getSkuSubtitle());
                skuInfoEntity.setSkuTitle(sku.getSkuTitle());
                skuInfoService.saveSkuInfo(skuInfoEntity);
                //添加sku图片，对commodity_sku_images表进行操作
                List<SkuImagesEntity> skuImagesEntities = sku.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setImgSort(0);
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuImagesEntity;
                }).filter(img -> {  //使用过滤器，过滤掉图片路径为空的元素
                    return StringUtils.isNotBlank(img.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);
                //添加sku销售属性，对commodity_sku_sale_attr_value表进行操作
                List<Attr> attrs = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                    skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                    skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                    skuSaleAttrValueEntity.setAttrSort(0);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
            });
        }
    }

    //使用SQL语句将Spu基本信息保存到commodity_spu_info表
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    //通过前端传入的检索条件进行分页查询
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        //获取检索关键字,增加过滤条件
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) { //判断用户是否输入了检索关键字
            //如果key是id就为=；如果key为名称就为like
            wrapper.and(obj -> {
                obj.eq("id", key).or().like("spu_name", key);
            });
        }
        //获取状态，增加过滤条件
        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) { //判断状态是否为空
            wrapper.eq("publish_status", status);
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
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    //用于修改商品为上架状态
    @Override
    public void up(Long spuId) {
        this.baseMapper.updateSpuStatus(spuId,1);
    }

    //用于修改商品为下架状态
    @Override
    public void down(Long spuId) {
        this.baseMapper.updateSpuStatus(spuId,2);
    }

}