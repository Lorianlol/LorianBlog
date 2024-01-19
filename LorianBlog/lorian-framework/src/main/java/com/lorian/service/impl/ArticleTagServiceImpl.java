package com.lorian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lorian.domain.entity.ArticleTag;
import com.lorian.mapper.ArticleTagMapper;
import com.lorian.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-08 14:05:55
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
