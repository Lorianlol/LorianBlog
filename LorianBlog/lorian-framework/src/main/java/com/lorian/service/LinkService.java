package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.entity.Link;
import com.lorian.domain.entity.ResponseResult;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-02-16 20:35:12
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(Link link);

    ResponseResult showLink(Long id);

    ResponseResult editLink(Link link);

    ResponseResult deleteLink(Long id);
}
