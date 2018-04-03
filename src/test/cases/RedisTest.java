package cases;

import com.school.Constants.LocationConst;
import com.school.Constants.NewsTypeConst;
import com.school.Enum.NewsEnum;
import com.school.Redis.LoadDateToRedis;
import com.school.Redis.StoredCacheService;
import com.school.service.NewsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

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
		loadDateToRedis.LoadDataToRedisByDate(NewsTypeConst.FRIENDS, 10);
		loadDateToRedis.removeDataFromRedis();
	}

	@Test
	public void testReadFromRedis()
	{
		newsService.getNewsSubjectList(NewsTypeConst.FRIENDS, null, LocationConst.ALL, -1, 1);
	}
}
