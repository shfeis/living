package com.hspedu.hspliving.commodity.web;

import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.CategoryService;
import com.hspedu.hspliving.commodity.vo.Catalog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用于将首页展示给用户
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;
    //响应用户请求首页
    @GetMapping(value = {"/","/index.html"})  //请求url: http://localhost:9090/ 或 http://localhost:9090/index.html
    public String indexPage(Model model){
        //获取一级分类的元素,存放到thymeleaf模板
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categories",categoryEntities);
        //请求转发到类路径下的index文件(classPath/templates/index.html)
        return "index";
    }

    //返回带层级关系的商品分类信息
    @GetMapping("/index/catalog.json")
    @ResponseBody //禁用视图，返回json格式的字符串
    public Map<String,List<Catalog2Vo>> getCatalogJson() {
        Map<String, List<Catalog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }
}
