package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrService;
import com.hspedu.hspliving.commodity.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;
import com.hspedu.hspliving.commodity.dao.AttrgroupDao;
import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import com.hspedu.hspliving.commodity.service.AttrgroupService;

import javax.annotation.Resource;


@Service("attrgroupService")
public class AttrgroupServiceImpl extends ServiceImpl<AttrgroupDao, AttrgroupEntity> implements AttrgroupService {
    @Resource
    private AttrService attrService;

    //无过滤条件显示所有元素
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrgroupEntity> page = this.page(
                new Query<AttrgroupEntity>().getPage(params),
                new QueryWrapper<AttrgroupEntity>());

        return new PageUtils(page);
    }

    //使用有过滤条件显示元素
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        //先获取到用户输入的查询关键字
        String key = (String) params.get("key");
        //该类可以将携带的数据植入SQL语句中执行
        QueryWrapper<AttrgroupEntity> wrapper = new QueryWrapper<>();
        //判断用户是否输入了查询关键字
        if (StringUtils.isNotBlank(key)) {
            //如果用户是通过商品id查询就是id==key,通过商品名称查询就是模糊查询,SQL语句为: WHERE (id=key OR name like key)
            wrapper.and((obj) -> {
                obj.eq("id", key).or().like("name", key);
            });
        }
        if (categoryId != 0) { //如果为0就不需要加入categoryId(数据库表字段)过滤条件,直接返回
            //SQL语句为: WHERE (id=key OR name like key*) AND (category_id=categoryId)
            wrapper.eq("category_id", categoryId);
        }
        IPage<AttrgroupEntity> page = this.page(new Query<AttrgroupEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    //根据所属分类ID返回对应的属性组，和属性组关联的基本属性
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId) {
        //根据categoryId获取属性组列表
        List<AttrgroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrgroupEntity>().eq("category_id", categoryId));
        //根据属性组获取基本属性列表，并将属性组、基本属性存放到VO实体类
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrGroupEntities.stream().map(attrGroupEntitiy -> {
            //使用BeanUtils(由Spring提供)把 AttrGroupEntity 复制到 AttrGroupWithAttrsVo
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntitiy, attrGroupWithAttrsVo);
            //根据attGroupId获取属性列表
            Long attGroupId = attrGroupEntitiy.getId();
            List<AttrEntity> attrEntities = attrService.getRelationAttr(attGroupId);
            //把attrEntities设置到VO实体类中
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());

        return attrGroupWithAttrsVos;
    }

}