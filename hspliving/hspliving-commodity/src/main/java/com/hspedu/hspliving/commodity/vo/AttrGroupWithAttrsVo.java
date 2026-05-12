package com.hspedu.hspliving.commodity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hspedu.hspliving.commodity.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * 当一个Entity类的属性是多个Entity类的属性时，就需要VO层（view Object）
 * VO类就是把多个Entity类的属性组合到一起产生的新实体
 *
 * 该类是：属性组里面可以存放属性元素
 */
@Data
public class AttrGroupWithAttrsVo {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 属性组名
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 说明
     */
    private String description;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类 id
     */
    private Long categoryId;
    /**
     * 一个属性组下可以有多个属性
     */
    private List<AttrEntity> attrs;
}
