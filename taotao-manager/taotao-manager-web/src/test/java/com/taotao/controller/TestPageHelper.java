package com.taotao.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {

	@Test
	public void test() {
		ApplicationContext  app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		TbItemMapper mapper = app.getBean(TbItemMapper.class);
		
		TbItemExample example =  new TbItemExample();
		PageHelper.startPage(1, 10);
		
	}

}
