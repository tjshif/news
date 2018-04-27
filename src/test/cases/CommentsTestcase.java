package cases;

import com.school.service.CommentsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:applicationContex.xml"})
public class CommentsTestcase {
	@Resource
	private CommentsService commentsService;

	@Test
	public void addFLCommentCase()
	{
		//commentsService.addFLComment(1L, 35L, "test2test3test4test5test6test7test8test9test10");
		//commentsService.addSecComment(1L, 35L, 35L, "reply5");
		//commentsService.getComments(1L, 1, 10);
		//commentsService.removeReplyComments(5L);
		//commentsService.removeComments(1L);

		commentsService.getComments(1L, 2, 5);
	}
}
