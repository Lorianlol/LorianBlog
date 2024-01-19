package com.lorian.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.lorian.domain.entity.Category;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.vo.ExcelCategoryVo;
import com.lorian.enums.AppHttpCodeEnum;
import com.lorian.service.CategoryService;
import com.lorian.utils.BeanCopyUtils;
import com.lorian.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("content/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //    设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //    获取需要导出的数据
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            //    将数据写到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class
                    ).autoCloseStream(Boolean.FALSE).sheet("test").doWrite(excelCategoryVos);
        } catch (Exception e) {
            //    如果出现异常也要响应Json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /*
    * 管理菜单页面，分页模糊查询菜单列表
    * */
    @GetMapping("/list")
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status){
        return categoryService.listCategory(pageNum, pageSize, name, status);
    }

    @PostMapping()
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    /*
    * 更新Category时回显数据
    * */
    @GetMapping("/{id}")
    public ResponseResult showCategoryDetail(@PathVariable("id") Long id){
        return categoryService.showCategoryDetail(id);
    }

    @PutMapping()
    public ResponseResult editCategory(@RequestBody Category category){
        return categoryService.editCategory(category);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

}