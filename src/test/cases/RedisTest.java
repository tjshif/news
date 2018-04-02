package cases;

import com.school.Enum.NewsEnum;
import com.school.Redis.LoadDateToRedis;
import com.school.Redis.StoredCacheService;
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

	@Test
	public void testRedis()
	{
		loadDateToRedis.LoadDataToRedisByDate(new Date(), NewsEnum.NEWS_JOB);
	}
}
