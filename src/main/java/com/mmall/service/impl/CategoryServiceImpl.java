package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Autowired
	private CategoryMapper categoryMapper;

	public ServerResponse addCategory(String categoryName, Integer parentId) {
		if (parentId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMessage("添加商品参数错误");
		}
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);
		int rowCount = categoryMapper.insert(category);
		if (rowCount > 0) {
			return ServerResponse.createBySuccess("添加商品品类成功");
		} else {
			return ServerResponse.createByErrorMessage("添加商品失败");
		}
	}

	public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
		if (categoryId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMessage("更新商品参数错误");
		}
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);
		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if (rowCount > 0) {
			return ServerResponse.createBySuccess("更新品类名字成功");
		}
		return ServerResponse.createByErrorMessage("更新品类名字失败");
	}

	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if (CollectionUtils.isEmpty(categoryList)) {
			logger.info("未找到当前分类的子分类");
		}
		return ServerResponse.createBySuccess(categoryList);

	}

	/**
	 * 
	 * @Title: 递归查询子节点和孩子节点的Id @Description: TODO @param @param
	 * categoryId @param @return @return ServerResponse @throws
	 */
	public ServerResponse<List<Integer>>selectCategoryAndChildrenById(Integer categoryId) {
		// guava缓存中读取的初始化集合
		Set<Category> categorySet = Sets.newHashSet();
		findChildCategory(categorySet, categoryId);
		// guava缓存中读取的初始化集合
		List<Integer> categoryIdList = Lists.newArrayList();
		if (categoryId != null) {
			for (Category categoryItem : categorySet) {
				categoryIdList.add(categoryItem.getId());
			}
		}
		return ServerResponse.createBySuccess(categoryIdList);

	}

	// 处理set 对于对象的时候需要重写hashCode和equals
	// 递归算法，算出子节点
	private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}
		// 查找子节点，递归算法一定要有一个退出的条件
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		for (int i = 0; i < categoryList.size(); i++) {
			findChildCategory(categorySet, categoryList.get(i).getId());
		}
		return categorySet;
	}
}
