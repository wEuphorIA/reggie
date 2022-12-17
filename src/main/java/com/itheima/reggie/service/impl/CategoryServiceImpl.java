package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @description: TODO
 * @date 2022/11/30 0:01
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /** 
     * @Description: 根据id删除分类 删除之前需要判断
     * @param: id 
     * @return: void
     * @date: 2022/11/30 8:46
     */ 
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件,根据分类id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联了一个，抛出一个异常
        if (count1 > 0){
            //已经关联了一个，抛出一个异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联了一个，抛出一个异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件,根据分类id查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            //已经关联了一个，抛出一个异常
            throw new CustomException("当前分类下关联了套餐不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
