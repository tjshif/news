package com.school.service.common;

import com.school.AOP.CacheMethodLogo;
import com.school.DAO.ICommentDao;
import com.school.Entity.CommentCountDTO;
import com.school.Utils.TimeUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CommentsServiceUtils {
    @Resource
    private ICommentDao commentDao;

    public List<CommentCountDTO> selectCommentCounts(List<String> newsIDs)
    {
        if (newsIDs.size() == 0)
            return null;
        List<CommentCountDTO> commentCounts = commentDao.selectCommentsCount(newsIDs);
        return commentCounts;
    }

}
