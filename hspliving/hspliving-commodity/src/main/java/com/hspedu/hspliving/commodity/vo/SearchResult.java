package com.hspedu.hspliving.commodity.vo;

import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * 用于存放用户输入检索条件后查询到的分页信息
 */
@Data
public class SearchResult {
    //检索查询到的商品信息
    private List<SkuInfoEntity> commodity;
    //检索的是第几页，当前页码
    private Integer pageNum;
    //总的记录数
    private Integer total;
    //总的页码数
    private Integer totalPages;
    //分页导航条，用于展示第几页应该显示什么内容
    private List<Integer> pageNavs;
    //用户输入的查询关键字
    private String keyword;
}
