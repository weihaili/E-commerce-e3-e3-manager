package cn.kkl.mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
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
import cn.kkl.mall.service.JedisClient;
import cn.kkl.mall.utils.IDUtils;
import cn.kkl.mall.utils.JsonUtils;
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
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO}")
	private String itemInfoCacheKey;
	
	@Value("ITEM_EXPIRE")
	private Integer expireTimeLimit;
	
	/* 
	 * query item depend on itemId 
	 */
	@Override
	public TbItem getItemById(long itemId) {
		try {
			String string = jedisClient.get(itemInfoCacheKey+":"+itemId+":base");
			if (StringUtils.isNotBlank(string)) {
				TbItem pojo = JsonUtils.jsonToPojo(string, TbItem.class);
				return pojo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			jedisClient.set(itemInfoCacheKey+":"+itemId+":base", JsonUtils.objectToJson(item));
			jedisClient.expire(itemInfoCacheKey+":"+itemId+":base", expireTimeLimit);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		final long itemId = IDUtils.genItemId();
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
		itemDescMapper.insert(itemDesc);
		
		//send item add message
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId+"");
				return message;
			}
		});
		return E3Result.ok();
	}
	
	
	

}
