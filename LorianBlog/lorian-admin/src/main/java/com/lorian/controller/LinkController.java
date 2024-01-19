package com.lorian.controller;

import com.lorian.domain.entity.Link;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    /*
    * 分页查询友链列表
    * */
    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.listLink(pageNum, pageSize, name, status);
    }

    @PostMapping()
    public ResponseResult addLink(@RequestBody Link link){
        return linkService.addLink(link);
    }

    /*
    * 修改List进行回显
    * */
    @GetMapping("/{id}")
    public ResponseResult showLink(@PathVariable("id")Long id){
        return linkService.showLink(id);
    }

    @PutMapping
    public ResponseResult editLink(@RequestBody Link link){
        return linkService.editLink(link);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id")Long id){
        return linkService.deleteLink(id);
    }

}
