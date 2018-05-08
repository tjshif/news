package cases;

import com.school.Constants.RetCode;
import com.school.Gson.RetBeAdminLoginGson;
import com.school.service.BeAdminService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml"})
public class BeAdminTest {
	@Autowired
	private BeAdminService beAdminService;

	@Test
	public void testLogin()
	{
		RetBeAdminLoginGson retBeAdminLoginGson = beAdminService.adminLoginIn("shifeng", "welcome@123");
		Assert.assertTrue(retBeAdminLoginGson.getRetCode() == RetCode.RET_CODE_OK);
	}
}
