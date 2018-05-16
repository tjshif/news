package cases;

import com.school.Entity.CommentCountDTO;
import com.school.Gson.RetFLCommentResultGson;
import com.school.service.CommentsService;
import com.school.service.common.CommentsServiceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml", "classpath:amqContext.xml"})
public class CommentsTestcase {
	@Resource
	private CommentsService commentsService;

	@Resource
	private CommentsServiceUtils commentsServiceUtils;

	@Test
	public void addFLCommentCase()
	{
		List<String> news = new ArrayList<>();
		news.add("2830");
		news.add("12");
		news.add("3642");

		//List<CommentCountDTO> rest = commentsServiceUtils.selectCommentCounts(news);
		//RetFLCommentResultGson fr = commentsService.addFLComment(1L, 35L, "test2test3test4test5test6test7test8test9test10");
		commentsService.addSecComment(1L, 1L, 1L, "reply5");
		//commentsService.getComments(1L, 1, 10);
		//commentsService.removeReplyComments(5L);
		//commentsService.removeComments(1L);

		commentsService.getComments(1L, 2, 5);
	}
}
