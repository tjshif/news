package cases;

import com.school.Constants.RetCode;
import com.school.Gson.LoginRegisterGson;
import com.school.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml"})
public class LoginTestcase {
	@Resource
	private LoginService loginService;

	@Test
	public void testLogin()
	{
		String phoneNumber = "13918802002";
		String smsCode = "2222";
		LoginRegisterGson loginRegisterGson = loginService.loginRegister(phoneNumber, smsCode);
		Assert.assertTrue(loginRegisterGson != null);
		Assert.assertTrue(loginRegisterGson.getRetCode() == RetCode.RET_CODE_OK);
	}

	@Test
	public void testInvalidSmsLogin()
	{
		String phoneNumber = "13918802002";
		String smsCode = "2422";
		LoginRegisterGson loginRegisterGson = loginService.loginRegister(phoneNumber, smsCode);
		Assert.assertTrue(loginRegisterGson != null);
		Assert.assertTrue(loginRegisterGson.getRetCode() == RetCode.RET_CODE_SMSERROR);

	}
}
