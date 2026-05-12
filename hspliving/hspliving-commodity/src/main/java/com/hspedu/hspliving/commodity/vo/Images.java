package com.hspedu.hspliving.commodity.vo;

import lombok.Data;

/**
 * @author 韩顺平
 * @version 1.0
 */
@Data
public class Images {
    //图片路径
    private String imgUrl;
    //是否为默认图片，0是，1否
    private int defaultImg;
}
