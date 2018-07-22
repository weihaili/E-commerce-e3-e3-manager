package cn.kkl.mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.kkl.mall.mapper.TbItemDescMapper;
import cn.kkl.mall.mapper.TbItemMapper;
import cn.kkl.mall.pojo.E3Result;
import cn.kkl.mall.pojo.EasyUIDataGridResult;
import cn.kkl.mall.pojo.TbItem;
import cn.kkl.mall.pojo.TbItemDesc;
import cn.kkl.mall.pojo.TbItemExample;
import cn.kkl.mall.service.ItemService;
import cn.kkl.mall.utils.IDUtils;
import javassist.expr.NewArray;

/**
 *item manager service
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;

	/* 
	 * query item depend on itemId 
	 */
	@Override
	public TbItem getItemById(long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

	/* 
	 * get itemList and page
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		TbItemExample example = new TbItemExample();
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	/* 
	 * add item and desc
	 */
	@Override
	public E3Result addItem(TbItem item, String desc) {
		Date date = new Date();
		long itemId = IDUtils.genItemId();
		item.setCreated(date);
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setUpdated(date);
		itemMapper.insert(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setCreated(date);
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(itemId);
		itemDesc.setUpdated(date);
		itemDescMapper.insertSelective(itemDesc);
		return E3Result.ok();
	}
	
	
	

}
