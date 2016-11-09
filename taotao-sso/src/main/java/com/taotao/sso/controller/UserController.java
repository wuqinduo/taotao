package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 *  用户注册的时候，需要对填写的字段做 ，是否存在的检查
	 *  例如： 手机号，填写完，检查一下 此 手机号是否已经注册 了。
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type,String callback){
		TaotaoResult result  = null ;
		
		//参数有效行 检验
		if (StringUtils.isBlank(param))
			result = TaotaoResult.build(400, "校验内容不能为空");
		if (type == null) 
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		if (type != 1 && type != 2 && type != 3 ) 
			result = TaotaoResult.build(400, "校验内容类型错误");
		
		//参数有效性出错，（程序走到这里，且不为空，那就是出错了）
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result; 
			}
		}
		
		// 开始检查 参数
		//调用服务
				try {
					result = userService.checkData(param, type);
					
				} catch (Exception e) {
					result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
				}
				
				if (null != callback) {
					MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
					mappingJacksonValue.setJsonpFunction(callback);
					return mappingJacksonValue;
				} else {
					return result; 
				}
		}
	

	//创建用户
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user) {
		//有效性验证
				if (StringUtils.isBlank(user.getUsername())) {
					return TaotaoResult.build(400, "用户名不能为空");
				}
				if (StringUtils.isBlank(user.getPassword())) {
					return TaotaoResult.build(400, "密码不能为空");
				}
				if (StringUtils.isBlank(user.getPhone())) {
					return TaotaoResult.build(400, "手机不能为空");
				}
		
		try {
			TaotaoResult result = userService.createUser(user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	//用户登录
		@RequestMapping(value="/login", method=RequestMethod.POST)
		@ResponseBody
		public TaotaoResult userLogin(String username, String password,
				HttpServletRequest request, HttpServletResponse response) {
			try {
				
				TaotaoResult result = userService.userLogin(username, password, request, response);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
			}
		}
		
		//跨域？？？？？？怎么解决的
		@RequestMapping("/token/{token}")
		@ResponseBody
		public Object getUserByToken(@PathVariable String token, String callback) {
			TaotaoResult result = null;
			try {
				result = userService.getUserByToken(token);
			} catch (Exception e) {
				e.printStackTrace();
				result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
			}
			
			//判断是否为jsonp调用
			if (StringUtils.isBlank(callback)) {
				return result;
			} else {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}
			
		}
}
