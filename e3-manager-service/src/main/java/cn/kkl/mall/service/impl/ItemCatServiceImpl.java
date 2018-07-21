package cn.kkl.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kkl.mall.mapper.TbItemCatMapper;
import cn.kkl.mall.pojo.EasyUITreeNode;
import cn.kkl.mall.pojo.TbItemCat;
import cn.kkl.mall.pojo.TbItemCatExample;
import cn.kkl.mall.pojo.TbItemCatExample.Criteria;
import cn.kkl.mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	/* 
	 * get itemCat List by parentId
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		List<EasyUITreeNode> treeNodes = new ArrayList<EasyUITreeNode>();
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}
