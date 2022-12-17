package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @description: 分类管理
 * @date 2022/11/30 0:03
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /** 
     * @Description:  新增分类
     * @param: category 
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @date: 2022/11/30 0:12
     */ 
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * @Description: 分页查询
     * @param: page
     * @param: pageSize
     * @return: com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     * @date: 2022/11/30 0:21
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * @Description:  根据id删除分类
     * @param: id
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @date: 2022/11/30 0:37
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，id：{}",ids);
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }
    /** 
     * @Description: 根据id修改分类信息 
     * @param: category 
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @date: 2022/11/30 9:23
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }
    /** 
     * @Description:  根据条件查询分类数据
     * @param: category 
     * @return: com.itheima.reggie.common.R<java.util.List<com.itheima.reggie.entity.Category>>
     * @date: 2022/12/10 11:07
     */ 
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}
