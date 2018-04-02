package cases;

import com.school.DAO.INewsDao;
import com.school.service.NewsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml"})
public class NewsTestcase {
	@Resource
	private NewsService newsService;

	@Test
	public void getNewsTest() throws ParseException {
		//TODO, refine later
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse("2018-04-01 12:10:12");
		newsService.selectNewsByCreateAt(date);
	}

	@Test
	public void getNewsByIDTests() throws ParseException {
		//TODO, refine later
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse("2018-04-01 12:10:12");
		newsService.selectNewsByCreateAtFromID(date, 3L);
	}
}
