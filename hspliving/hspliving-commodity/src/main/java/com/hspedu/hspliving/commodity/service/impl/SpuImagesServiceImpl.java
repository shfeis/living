package com.hspedu.hspliving.commodity.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SpuImagesDao;
import com.hspedu.hspliving.commodity.entity.SpuImagesEntity;
import com.hspedu.hspliving.commodity.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    //保存spu商品图片集
    @Override
    public void saveImages(Long id, List<String> images) {
        SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
        if (images.isEmpty()) { //若传入的图片集为空，就设置默认的图片路径
            spuImagesEntity.setSpuId(id);
            spuImagesEntity.setImgUrl("http://localhost/default1.jpg");
            spuImagesEntity.setDefaultImg(1);
            this.save(spuImagesEntity);
        } else {
            List<SpuImagesEntity> spuImagesEntities = images.stream().map(img -> {
                spuImagesEntity.setSpuId(id);
                spuImagesEntity.setImgUrl(img);
                return spuImagesEntity;
            }).collect(Collectors.toList());
            this.saveBatch(spuImagesEntities);
        }
    }

}