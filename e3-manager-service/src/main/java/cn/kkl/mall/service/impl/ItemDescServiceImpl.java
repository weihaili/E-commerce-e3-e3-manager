package cn.kkl.mall.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.kkl.mall.mapper.TbItemDescMapper;
import cn.kkl.mall.pojo.TbItemDesc;
import cn.kkl.mall.pojo.TbItemDescExample;
import cn.kkl.mall.pojo.TbItemDescExample.Criteria;
import cn.kkl.mall.service.ItemDescService;

@Service
public class ItemDescServiceImpl implements ItemDescService {
	
	private TbItemDescMapper itemDescMapper;

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		TbItemDescExample example = new TbItemDescExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
		if (list==null || list.size()==0) {
			return null;
		}
		return list.get(0);
	}

}
