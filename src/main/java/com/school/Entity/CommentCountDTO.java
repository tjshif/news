package com.school.Entity;

public class CommentCountDTO {
    private String newsID;
    private Integer totalCount;

    public String getNewsID() {
        return newsID;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
