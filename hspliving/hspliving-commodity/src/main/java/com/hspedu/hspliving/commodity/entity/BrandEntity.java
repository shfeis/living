package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.hspedu.common.valid.EnumValidate;
import com.hspedu.common.valid.SaveGroup;
import com.hspedu.common.valid.UpdateGroup;
import com.hspedu.common.valid.UpdateIsShowGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 家居品牌
 */
@Data
@TableName("commodity_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id，自增长
     * @NotNull(groups = {UpdateGroup.class}) 该注解表示该属性不为空，groups指定对哪个组生效，这里对修改组生效
     * @Null(groups = {SaveGroup.class}) 该注解表示该属性为空，这里对新增组生效，因为新增元素时id自动生成不可指定
     */
    @TableId
    @NotNull(message = "修改时必须指定id", groups = {UpdateGroup.class, UpdateIsShowGroup.class})
    @Null(message = "添加时不能指定id", groups = {SaveGroup.class})
    private Long id;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {SaveGroup.class, UpdateGroup.class})
    //JSR303的注解，表示该属性不能为空，message为提示信息
    private String name;
    /**
     * logo商标
     */
    @NotBlank(message = "logo不能为空", groups = {SaveGroup.class})
    @URL(message = "logo不是一个合法的URL地址", groups = {SaveGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 说明
     */
    private String description;
    /**
     * 显示
     */
    @NotNull(message = "显示状态不能为空", groups = {SaveGroup.class, UpdateGroup.class, UpdateIsShowGroup.class})
    @EnumValidate(value = {0, 1}, message = "显示状态的值在【0-1】之间", groups = {SaveGroup.class, UpdateGroup.class, UpdateIsShowGroup.class}) //自定义数据校验器来限制只能输入0或1
    private Integer isshow;
    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不能为空", groups = {SaveGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是a-z或A-Z", groups = {SaveGroup.class, UpdateGroup.class})
    //限制输入a-z或A-Z
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(message = "排序值不能为空", groups = {SaveGroup.class})
    @Min(value = 0, message = "排序值要求大于等于0", groups = {SaveGroup.class, UpdateGroup.class}) //约束最小值
    private Integer sort;

}
