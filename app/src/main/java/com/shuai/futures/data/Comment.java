package com.shuai.futures.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 评论信息
 */
public class Comment {
    public static final long INVALID_ID = -1;

    private static class CommentUser {
        @SerializedName("id")
        private long mCommentUserId;

        @SerializedName("nickName")
        private String mCommentUserNickName;

        @SerializedName("headImageUrl")
        private String mCommentUserHeadUrl;
    }

    @SerializedName("id")
    private long mCommentId = INVALID_ID;

    @SerializedName("ppid")
    private long mPpid = INVALID_ID;

    @SerializedName("date")
    private long mCommentTime;

    @SerializedName("user")
    private CommentUser mCommentUser;

    @SerializedName("content")
    private String mCommentContent;

    @SerializedName("pic")
    private ArrayList<String> mCommentImageList = new ArrayList<String>();

    @SerializedName("originalComment")
    private Comment mOriginalComment;

    public boolean hasOriginalComment() {
        return mOriginalComment != null;
    }

    public long getCommentId() {
        return mCommentId;
    }

    public void setCommentId(long commentId) {
        mCommentId = commentId;
    }

    public long getPpid() {
        return mPpid;
    }

    public void setPpid(long ppid) {
        this.mPpid = ppid;
    }

    public long getCommentUserId() {
        return mCommentUser.mCommentUserId;
    }

    public void setCommentUserId(long commentUserId) {
        mCommentUser.mCommentUserId = commentUserId;
    }

    public String getCommentUserNickName() {
        return mCommentUser.mCommentUserNickName;
    }

    public void setCommentUserNickName(String commentUserNickName) {
        mCommentUser.mCommentUserNickName = commentUserNickName;
    }

    public String getCommentUserHeadUrl() {
        return mCommentUser.mCommentUserHeadUrl;
    }

    public void setCommentUserHeadUrl(String commentUserHeadUrl) {
        mCommentUser.mCommentUserHeadUrl = commentUserHeadUrl;
    }

    public long getCommentTime() {
        return mCommentTime;
    }

    public void setCommentTime(long commentTime) {
        mCommentTime = commentTime;
    }

    public ArrayList<String> getCommentImageList() {
        return mCommentImageList;
    }

    public void setCommentImageList(ArrayList<String> commentImageList) {
        mCommentImageList = commentImageList;
    }

    public String getCommentContent() {
        return mCommentContent;
    }

    public void setCommentContent(String commentContent) {
        mCommentContent = commentContent;
    }

    public long getOriginalCommentUserId() {
        if (mOriginalComment == null)
            return 0;
        return mOriginalComment.mCommentUser.mCommentUserId;
    }

    public void setOriginalCommentUserId(long originalCommentUserId) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentUser.mCommentUserId = originalCommentUserId;
    }

    public String getOriginalCommentUserNickName() {
        if (mOriginalComment == null)
            return null;

        return mOriginalComment.mCommentUser.mCommentUserNickName;
    }

    public void setOriginalCommentUserNickName(String originalCommentUserNickName) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentUser.mCommentUserNickName = originalCommentUserNickName;
    }

    public String getOriginalCommentUserHeadUrl() {
        if (mOriginalComment == null)
            return null;

        return mOriginalComment.mCommentUser.mCommentUserHeadUrl;
    }

    public void setOriginalCommentUserHeadUrl(String originalCommentUserHeadUrl) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentUser.mCommentUserHeadUrl = originalCommentUserHeadUrl;
    }

    public long getOriginalCommentTime() {
        if (mOriginalComment == null)
            return 0;

        return mOriginalComment.mCommentTime;
    }

    public void setOriginalCommentTime(long originalCommentTime) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentTime = originalCommentTime;
    }

    public long getOriginalCommentId() {
        if (mOriginalComment == null)
            return 0;

        return mOriginalComment.mCommentId;
    }

    public void setOriginalCommentId(long originalCommentId) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentId = originalCommentId;
    }

    public ArrayList<String> getOriginalCommentImageList() {
        if (mOriginalComment == null)
            return null;

        return mOriginalComment.mCommentImageList;
    }

    public void setOriginalCommentImageList(ArrayList<String> originalCommentImageUrl) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentImageList = originalCommentImageUrl;
    }

    public String getOriginalCommentContent() {
        if (mOriginalComment == null)
            return null;

        return mOriginalComment.mCommentContent;
    }

    public void setOriginalCommentContent(String originalCommentContent) {
        if (mOriginalComment == null)
            return;

        mOriginalComment.mCommentContent = originalCommentContent;
    }
}
