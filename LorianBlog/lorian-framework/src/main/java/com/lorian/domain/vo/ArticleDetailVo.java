package com.lorian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    private String content;
    private Long categoryId;
    //所属分类名
    private String categoryName;
    //是否允许评论 1是，0否
    private String isComment;


    //访问量
    private Long viewCount;

    private Date createTime;
}
