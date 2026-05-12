package com.hspedu.hspliving.commodity.controller;

import com.hspedu.hspliving.commodity.service.SkuInfoService;
import com.hspedu.hspliving.commodity.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 前端用户检索页面
 */
@Controller
public class SearchController {
    @Resource
    private SkuInfoService skuInfoService;

    @RequestMapping("/list.html")
    public String searchList(@RequestParam Map<String, Object> params, Model model) {
        //根据检索条件分页查询
        SearchResult searchResult = skuInfoService.querySearchPageByCondition(params);
        model.addAttribute("result", searchResult);
        return "list";
    }
}
