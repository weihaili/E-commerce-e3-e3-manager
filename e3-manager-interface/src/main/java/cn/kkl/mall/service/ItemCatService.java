package cn.kkl.mall.service;

import java.util.List;

import cn.kkl.mall.pojo.EasyUITreeNode;

public interface ItemCatService {
	
	List<EasyUITreeNode> getItemCatList(long parentId);
}
