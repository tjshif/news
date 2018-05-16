package cases;

import com.school.Constants.RetCode;
import com.school.DAO.INewsDao;
import com.school.Entity.NewsDetailDTO;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Gson.NewsCountResultGson;
import com.school.Gson.NewsDetailResultGson;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Gson.RetResultGson;
import com.school.service.NewsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class NewsTestcase {
	@Resource
	private NewsService newsService;
	@Test
	public void getNewsDetailTest()
	{
		NewsSubjectResultGson r1 = newsService.getNewsSubjectList(NewsTypeEnum.NEWS_JOB, null, 25, null, 20);

		NewsSubjectResultGson r2 = newsService.getNewsSubjectListByPage(NewsTypeEnum.NEWS_JOB, null,25, 0, 20);

		NewsDetailResultGson resultGson = newsService.getNewsDetail(1L, null);
		Assert.assertTrue(resultGson.getRetCode() == RetCode.RET_CODE_OK);
	}

	@Test
	public void getFavList()
	{
		NewsSubjectResultGson resultGson = newsService.getFavoriteNews(1L, 0, 2);
		Assert.assertTrue(resultGson.getRetCode() == RetCode.RET_CODE_OK);
	}

	@Test
	public void updateRowTest()
	{
		RetResultGson resultGson = newsService.updateNewsValid(6L, false);
		Assert.assertTrue(resultGson.getRetCode() == RetCode.RET_CODE_OK);
	}

	@Test
	public void getCount()
	{
		NewsCountResultGson resultGson = newsService.getNewsCount(NewsTypeEnum.NEWS_JOB, null, 21);
		Assert.assertTrue(resultGson.getRetCode() == RetCode.RET_CODE_OK);
	}
}
