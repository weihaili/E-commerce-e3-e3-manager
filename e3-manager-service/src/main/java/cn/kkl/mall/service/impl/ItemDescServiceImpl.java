package cn.kkl.mall.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.kkl.mall.mapper.TbItemDescMapper;
import cn.kkl.mall.pojo.TbItemDesc;
import cn.kkl.mall.service.ItemDescService;
import cn.kkl.mall.service.JedisClient;
import cn.kkl.mall.utils.JsonUtils;

@Service
public class ItemDescServiceImpl implements ItemDescService {
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO}")
	private String itemInfoCacheKey;
	
	@Value("ITEM_EXPIRE")
	private Integer expireTimeLimit; 

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		try {
			String string = jedisClient.get(itemInfoCacheKey+":"+itemId+":desc");
			if (StringUtils.isNotBlank(string)) {
				TbItemDesc pojo = JsonUtils.jsonToPojo(string, TbItemDesc.class);
				if (pojo!=null) {
					return pojo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			if (itemDesc!=null) {
				jedisClient.set(itemInfoCacheKey+":"+itemId+":desc", JsonUtils.objectToJson(itemDesc));
				jedisClient.expire(itemInfoCacheKey+":"+itemId+":desc", expireTimeLimit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
