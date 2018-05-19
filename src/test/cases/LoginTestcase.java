package cases;

import com.school.Constants.RetCode;
import com.school.Gson.*;
import com.school.service.LoginService;
import com.school.service.UserService;
import junit.runner.Version;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class LoginTestcase {
	@Resource
	private LoginService loginService;

	@Resource
	private UserService userService;

	@Test
	public void testLogin()
	{
		String phoneNumber = "13918802002";
		String smsCode = "7139";
		LoginRegisterGson loginRegisterGson = loginService.loginRegister(phoneNumber, smsCode);
		Assert.assertTrue(loginRegisterGson != null);
		Assert.assertTrue(loginRegisterGson.getRetCode() == RetCode.RET_CODE_OK);
	}

	@Test
	public void testInvalidSmsLogin()
	{
		String phoneNumber = "13918802002";
		String smsCode = "1396";
		LoginRegisterGson loginRegisterGson = loginService.loginRegister(phoneNumber, smsCode);
		Assert.assertTrue(loginRegisterGson != null);
		Assert.assertTrue(loginRegisterGson.getRetCode() == RetCode.RET_CODE_SMSERROR);

	}

	@Test
	public void testFeedback()
	{
		RetResultGson retResultGson = loginService.insertFeedback("13918802002", "测试");
		Assert.assertTrue(retResultGson != null);
		Assert.assertTrue(retResultGson.getRetCode() == RetCode.RET_CODE_OK);
	}

	@Test
	public void getLaestVersion()
	{
		VersionResultGson versionResultGson = loginService.getVersionInfo(1L);
		Assert.assertTrue(versionResultGson != null);
	}

	@Test
	public void getUserInfo()
	{
		RetIDResultGson r = userService.getUserIDInfo("ukjqtt");
		UserInfoResultGson resultGson = userService.getUserInfo(1L);
		 resultGson = userService.getUserInfo(105L);
	}
}
