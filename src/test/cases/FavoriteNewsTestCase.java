package cases;

import com.school.Constants.RetCode;
import com.school.Entity.FavoriteNewsDTO;
import com.school.Gson.RetResultGson;
import com.school.service.NewsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class FavoriteNewsTestCase {
	@Resource
	private NewsService newsService;

	@Test
	public void testAddFavoriteNews()
	{
		FavoriteNewsDTO favoriteNewsDTO = new FavoriteNewsDTO(1L, 1L);
		RetResultGson retResultGson = newsService.addOrDeleteFavoriteNews(true, 1L, 1L);
		Assert.assertTrue(retResultGson.getRetCode() == RetCode.RET_CODE_OK);

		retResultGson = newsService.addOrDeleteFavoriteNews(false, 1L, 1L);
		Assert.assertTrue(retResultGson.getRetCode() == RetCode.RET_CODE_OK);
	}
}
