package cases;

import com.school.Enum.LocationEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Redis.LoadDateToRedis;
import com.school.service.NewsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml"})
public class RedisTest {
	@Resource
	private LoadDateToRedis loadDateToRedis;

	@Resource
	private NewsService newsService;

	@Test
	public void testRedis()
	{
		loadDateToRedis.LoadDataToRedisByDate(2, 100);
		loadDateToRedis.removeDataFromRedis();
	}

	@Test
	public void testReadFromRedis()
	{
		newsService.getNewsSubjectList(NewsTypeEnum.NEWS_JOB, null, LocationEnum.NANJING.getZipCode(), null, 30);
	}
}
