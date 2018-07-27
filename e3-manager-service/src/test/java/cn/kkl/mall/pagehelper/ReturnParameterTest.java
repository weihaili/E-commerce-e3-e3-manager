package cn.kkl.mall.pagehelper;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.kkl.mall.mapper.TbItemMapper;
import cn.kkl.mall.pojo.TbItem;
import cn.kkl.mall.pojo.TbItemExample;



/**
 * @author Admin
 * Initialize spring contain then get mybatis mapper proxy object
 * pageHelper use step:
 * 1. set pageParameters before execute query sql statement every time and very query sql statement.use pagaHelper static method.startPage().
 * 2. execute query sql statement then get query resultSet
 * 3. get pageInformation from resultSet use class PageInfo(contains :totalRecords,currentPage,totalPages,rows per page)
 */
public class ReturnParameterTest {
	
	private ApplicationContext applicationContext;
	private TbItemMapper itemMapper;
	
	@Before
	public void Init() {
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		itemMapper = applicationContext.getBean(TbItemMapper.class);
	}
	
	@Test
	public void testPageTest() {
		PageHelper.startPage(1, 10);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		System.out.println(pageInfo.getPageSize());
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(list.size());
		
	}

}
