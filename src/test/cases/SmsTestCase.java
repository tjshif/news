package cases;

import com.aliyuncs.exceptions.ClientException;
import com.school.Utils.SendSmsUtil;
import com.school.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml"})
public class SmsTestCase {
	@Resource
	LoginService loginService;

	@Test
	public void sendSms() throws ClientException {
		loginService.sendSms("13918802002");
	}

}
