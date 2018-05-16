package cases;

import com.school.Gson.PostMsgGson;
import com.school.Utils.GsonUtil;
import com.school.Utils.IdWorkerUtils;
import com.school.service.PostMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class PostMsgTestcase {
	@Autowired
	PostMsgService postMsgService;

	@Test
	public void testPostMsg()
	{
		PostMsgGson postMsgGson = new PostMsgGson();
		postMsgGson.setContent("test");
		postMsgGson.setLocationCode(21);
		postMsgGson.setNewsType(1);
		List<String> imagePath = new ArrayList<>();
		imagePath.add("a.png");
		imagePath.add("b.jpg");
		Long id1 = IdWorkerUtils.getGlobalID();
		Long id2 = IdWorkerUtils.getGlobalID();
		postMsgService.postMsgToRedis(35L, GsonUtil.toJson(postMsgGson), imagePath);
	}
}
