package com.bokeproject.service.impl.model;

import javax.validation.constraints.NotBlank;

public class CommentModel {
    private Integer commentid;
    @NotBlank(message = "评价不能为空")
    private String comments;
    private Integer id;
    private Integer commentaterid;
    private String commentdate;
    private Integer blogid;
    private Integer fathercommentid;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getFathercommentid() {
        return fathercommentid;
    }

    public void setFathercommentid(Integer fathercommentid) {
        this.fathercommentid = fathercommentid;
    }

    public Integer getCommentid() {
        return commentid;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentaterid() {
        return commentaterid;
    }

    public void setCommentaterid(Integer commentaterid) {
        this.commentaterid = commentaterid;
    }

    public String getCommentdate() {
        return commentdate;
    }

    public void setCommentdate(String commentdate) {
        this.commentdate = commentdate;
    }

    public Integer getBlogid() {
        return blogid;
    }

    public void setBlogid(Integer blogid) {
        this.blogid = blogid;
    }
}
