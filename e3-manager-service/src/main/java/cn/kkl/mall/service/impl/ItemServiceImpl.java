package cn.kkl.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kkl.mall.mapper.TbItemMapper;
import cn.kkl.mall.pojo.TbItem;
import cn.kkl.mall.service.ItemService;

/**
 *item manager service
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;

	/* 
	 * query item depend on itemId 
	 */
	@Override
	public TbItem getItemById(long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

}
