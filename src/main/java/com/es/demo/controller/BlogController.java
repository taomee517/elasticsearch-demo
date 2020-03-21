package com.es.demo.controller;

import com.es.demo.beans.BlogModel;
import com.es.demo.beans.PageEntity;
import com.es.demo.beans.PageRequest;
import com.es.demo.jpa.BlogRepository;
import com.es.demo.jpa.CunstomPageable;
import com.es.demo.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "blog")
@Api(tags={"Blog接口-继承CRUD Repository"}, value = "BlogController")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @PostMapping("/add")
    @ApiOperation(value = "新增blog文档")
    public ResponseEntity<Boolean> add(@RequestBody BlogModel blog){
        blogRepository.save(blog);
        return ResponseEntity.ok(Boolean.TRUE);
    }


    @GetMapping("/id/{id}")
    @ApiOperation(value = "根据id查询blog文档")
    public ResponseEntity<BlogModel> queryById(@PathVariable(value = "id") String id){
        if(StringUtils.isEmpty(id)){
            return ResponseEntity.badRequest().body(null);
        }
        Optional<BlogModel> optional = blogRepository.findById(id);
        if(optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getAll")
    @ApiOperation(value = "查询所有blog文档")
    public ResponseEntity<List<BlogModel>> getAll(){
        Iterable<BlogModel> blogModels = blogRepository.findAll();
        Iterator<BlogModel> iterator = blogModels.iterator();
        List<BlogModel> blogs = new ArrayList<>();
        while (iterator.hasNext()){
            blogs.add(iterator.next());
        }
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据id更新blog文档")
    public ResponseEntity<Boolean> updateById(@RequestBody BlogModel blog){
        String id = blog.getId();
        Assert.notNull(id, "blog id must not be null");
        try {
            blogRepository.save(blog);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(Boolean.FALSE);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据id删除blog文档")
    public ResponseEntity<Boolean> deleteById(@PathVariable(value = "id") String id){
        Assert.notNull(id, "blog id must not be null");
        try {
            blogRepository.deleteById(id);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
        }
    }


    @PostMapping("/getPage")
    @ApiOperation(value = "查询blog分页文档")
    public ResponseEntity<PageEntity<List<BlogModel>>> getPage(@RequestBody PageRequest pageRequest){
        Assert.isTrue(pageRequest.getIsPaged(), "isPaged must be true!");
        Pageable pageable = PageUtils.buildSpringPageRequest(pageRequest);
        Page<BlogModel> blogModels = blogRepository.findAll(pageable);
        List<BlogModel> blogs = null;
        if (blogModels.getSize()>0) {
            Iterator<BlogModel> iterator = blogModels.iterator();
            blogs = new ArrayList<>();
            while (iterator.hasNext()){
                blogs.add(iterator.next());
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setCount(blogModels.getTotalElements());
        pageEntity.setPageCount(blogModels.getTotalPages());
        pageEntity.setData(blogs);
        return ResponseEntity.ok(pageEntity);
    }
}
