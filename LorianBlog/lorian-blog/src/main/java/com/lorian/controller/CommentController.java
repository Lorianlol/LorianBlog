package com.lorian.controller;

import com.lorian.constants.SystemConstants;
import com.lorian.domain.dto.AddCommentDto;
import com.lorian.domain.entity.Comment;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.service.CommentService;
import com.lorian.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口") //使用Swagger进行文档注解
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    /*
    * 实际开发中，接口中参数都是使用的Dto对象（Data transaction object数据传输对象）
    * */
    @PostMapping()
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表", notes = "获取一页友链评论") //Swagger注解 接口配置描述
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum", value="页号"),
            @ApiImplicitParam(name="pageSize", value="每页大小")
    }
    ) //Swagger注解 参数描述
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT, null,  pageNum, pageSize);

    }
}
