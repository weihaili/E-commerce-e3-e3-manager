package cn.kkl.mall.service;

import cn.kkl.mall.pojo.EasyUIDataGridResult;
import cn.kkl.mall.pojo.TbItem;

public interface ItemService {
	
	TbItem getItemById(long itemId);
	
	EasyUIDataGridResult getItemList(int page,int rows);

}
