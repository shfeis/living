package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.dao.AttrAttrgroupRelationDao;
import com.hspedu.hspliving.commodity.dao.AttrgroupDao;
import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.AttrDao;
import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );
        return new PageUtils(page);
    }

    @Resource //将组件注入该容器
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Resource
    private AttrgroupDao attrgroupDao;
    @Resource
    private AttrDao attrDao;

    /*
     1 保存商品基本属性且保存商品属性和属性组的关联关系
     2 因为this.save(attrEntity); attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);都是调用
      INSERT语句，故需要一个处理事务的注解
    */
    @Override
    @Transactional //开启事务处理
    public void saveAttrAndRelation(AttrEntity attrEntity) {
        //保存基本属性
        this.save(attrEntity);
        //保存属性-属性组关联关系
        if (attrEntity.getAttrType() == 1 && attrEntity.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId()); //设置属性id
            attrAttrgroupRelationEntity.setAttrGroupId(attrEntity.getAttrGroupId()); //设置属性组ID
            //调用DAO层的添加元素的方法，执行SQL语句
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    //根据 查询关键字、categoryId(所属分类ID)、分页等条件来有选择的显示基本属性元素
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId) {
        //先获取到用户输入的查询关键字
        String key = (String) params.get("key");
        //该类可以将携带的数据植入SQL语句中执行
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        //判断用户是否输入了查询关键字
        if (StringUtils.isNotBlank(key)) {
            //如果用户是通过商品id查询就是id==key,通过商品名称查询就是模糊查询,SQL语句为: WHERE id=key OR name like key
            wrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        if (categoryId != 0) { //如果为0就不需要加入categoryId(数据库表字段)过滤条件,直接返回
            //SQL语句为: WHERE (id=key OR name like key*) AND (category_id=categoryId)
            wrapper.eq("category_id", categoryId);
        }
        //无论是否有过滤条件，都必须携带 WHERE attr_type=1，即查询基本属性的元素
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper.eq("attr_type", 1));
        return new PageUtils(page);
    }

    //根据 查询关键字、categoryId(所属分类ID)、分页等条件来有选择的显示销售属性元素
    @Override
    public PageUtils querySaleAttrPage(Map<String, Object> params, Long categoryId) {
        //先获取到用户输入的查询关键字
        String key = (String) params.get("key");
        //该类可以将携带的数据植入SQL语句中执行
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        //判断用户是否输入了查询关键字
        if (StringUtils.isNotBlank(key)) {
            //如果用户是通过商品id查询就是id==key,通过商品名称查询就是模糊查询,SQL语句为: WHERE id=key OR name like key
            wrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        if (categoryId != 0) { //如果为0就不需要加入categoryId(数据库表字段)过滤条件,直接返回
            //SQL语句为: WHERE (id=key OR name like key*) AND (category_id=categoryId)
            wrapper.eq("category_id", categoryId);
        }
        //无论是否有过滤条件，都必须携带 WHERE attr_type=1，即查询销售属性的元素
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper.eq("attr_type", 0));
        return new PageUtils(page);
    }

    //增加方法，可以根据属性组ID返回该属性组关联的基本属性
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        //执行SQL语句：SELECT * FROM commodity_attr_attrgroup_relation WHERE attr_group_id=attrgroupId
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntitys =
                attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        //根据attrAttrgroupRelationEntitys(属性-属性组关联实体列表)得到attrId(基本属性ID)
        List<Long> attrIds = new ArrayList<>();
        attrAttrgroupRelationEntitys.forEach((itme) -> {
            attrIds.add(itme.getAttrId());
        });
        //根据attrId得到AttrEntity(基本属性)
        if (attrIds.isEmpty()) { //判断集合是否为空
            return null;
        }
        //this是当前对象,对应的表为commodity_attr；所以SQL语句：SELECT * FROM commodity_attr WHERE attr_id IN attrIds
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    //可以批量删除 属性-属性组关联关系
    @Override
    public void deleteRelation(AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities) {
        attrAttrgroupRelationDao.deleteBatchRelation(Arrays.asList(attrAttrgroupRelationEntities));
    }

    //获取某个属性组可以关联的基本属性列表(不获取已经关联的属性)
    @Override
    public PageUtils getAllowRelationAttr(Map<String, Object> params, Long attrGroupId) {
        //通过 属性组ID 获取到 所属分类ID
        AttrgroupEntity attrgroupEntity = attrgroupDao.selectById(attrGroupId);
        Long categoryId = attrgroupEntity.getCategoryId();
        //通过所属分类ID得到该分类下的所有属性组
        List<AttrgroupEntity> attrgroupEntities = attrgroupDao.selectList(new QueryWrapper<AttrgroupEntity>().eq("category_id", categoryId));
        //获取该分类ID下的所有属性组ID(这里使用流式计算获取，也可以使用forEach()获取)
        List<Long> attrGorupIds = attrgroupEntities.stream().map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());
        //通过属性组ID得到 属性-属性组关联关系
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities =
                attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGorupIds));
        //获取关联关系中所有属性ID(这里使用流式计算获取，也可以使用forEach()获取)
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //通过 所属分类ID 获取到 基本属性列表
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type", 1).eq("category_id", categoryId);
        //排除已经关联过的基本属性，不进行显示
        if (attrIds != null && attrIds.size() != 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        //因为前端实现了查询功能，所以会携带关键字来进行属性的搜索
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) { //判断用户是否输入查询关键字
            //如果用户是通过商品id查询就是id==key,通过商品名称查询就是模糊查询,SQL语句为: WHERE (id=key OR name like key)
            wrapper.and(obj -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        //进行分页查询
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }
}
