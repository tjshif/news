package cases;

import com.school.Constants.RetCode;
import com.school.Gson.RetResultGson;
import com.school.service.FocusService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class FocusTestcase {
	@Autowired
	private FocusService focusService;

	@Test
	public void testFocus()
	{
		RetResultGson  resultGson = focusService.setFocus("35", "36");
		Assert.assertTrue(resultGson.getRetCode() == RetCode.RET_CODE_OK);
	}
}
