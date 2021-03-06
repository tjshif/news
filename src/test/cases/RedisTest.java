package cases;

import com.school.Enum.NewsTypeEnum;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Redis.LoadDataToRedis;
import com.school.service.NewsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class RedisTest {
	@Resource
	private LoadDataToRedis loadDataToRedis;

	@Resource
	private NewsService newsService;

	@Test
	public void testRedis()
	{
		loadDataToRedis.LoadDataToRedis();
		loadDataToRedis.removeDataFromRedis();
	}

	@Test
	public void testReadFromRedis()
	{
		newsService.getNewsSubjectList(NewsTypeEnum.NEWS_JOB, null, 27, null, 30);
	}

	@Test
	public void testReadByPage()
	{
		NewsSubjectResultGson resultGson1 = newsService.getNewsSubjectList(NewsTypeEnum.NEWS_JOB, null, 27, null, 4);
		NewsSubjectResultGson resultGson2 = newsService.getNewsSubjectListByPage(NewsTypeEnum.NEWS_JOB, null, 27, 0, 4);
		Assert.assertTrue(true);
	}
}
