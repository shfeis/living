/**
  * Copyright 2021 json.cn
  */
package com.hspedu.hspliving.commodity.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2021-10-13 23:25:51
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class SpuSaveVO {

    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    //商品介绍图片
    private List<String> decript;
    //商品图片集
    private List<String> images;
    private Bounds bounds;
    //商品基本属性
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;


}
