package com.hspedu.hspliving.commodity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级分类的Vo类
 */
@Data
@NoArgsConstructor //无参构造器
@AllArgsConstructor
public class Catalog2Vo {
    //二级分类的parent_id
    private String catalog1Id;
    //二级分类本身的信息
    private String id;
    private String name;

    //用于存放三级分类的集合
    private List<Category3Vo> catalog3List;

    //三级分类的信息
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category3Vo {
        //三级分类的parent_id
        private String catalog2Id;
        //该级分类本身的信息
        private String id;
        private String name;
    }
}
