package com.netterex.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netterex.common.utils.PageUtils;
import com.netterex.common.utils.Query;

import com.netterex.gulimall.product.dao.CategoryDao;
import com.netterex.gulimall.product.entity.CategoryEntity;
import com.netterex.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 已经继承ServiceImpl，其中私有变量basemapper就是CategoryDao
        // 获取所有的分类
        List<CategoryEntity> categoryList = baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = categoryList.stream().filter((categoryEntity) -> {
            // 过滤出所有的一级分类
            return categoryEntity.getParentCid() == 0;
        }).map((menu) -> {
            // 递归查找子分类菜单
            menu.setChildren(getChildrenMenu(menu, categoryList));
            return menu;
        }).sorted((menu1, menu2) -> {
            // 排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeByMenuIds(List<Long> list) {
        //TODO 检查当前删除的菜单是否在别的位置被引用

        baseMapper.deleteBatchIds(list);
    }

    private List<CategoryEntity> getChildrenMenu(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> childrenMenu = all.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map((menu) -> {
            menu.setChildren(getChildrenMenu(menu, all));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return childrenMenu;
    }
}