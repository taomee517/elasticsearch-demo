package com.es.demo.controller;

import com.es.demo.beans.AppLogModel;
import com.es.demo.beans.PageEntity;
import com.es.demo.beans.PageRequest;
import com.es.demo.beans.dto.AppLogQueryDTO;
import com.es.demo.jpa.AppLogRepository;
import com.es.demo.service.IAppLogSearchService;
import com.es.demo.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "log")
@Api(tags={"微服务日志接口"}, value = "AppLogController")
public class AppLogController {

    @Autowired
    private AppLogRepository appLogRepository;

    @Autowired
    private IAppLogSearchService appLogSearchService;

    @PostMapping("/add")
    @ApiOperation(value = "新增blog文档")
    public ResponseEntity<Boolean> add(@RequestBody AppLogModel blog){
        appLogRepository.save(blog);
        return ResponseEntity.ok(Boolean.TRUE);
    }


    @GetMapping("/id/{id}")
    @ApiOperation(value = "根据id查询blog文档")
    public ResponseEntity<AppLogModel> queryById(@PathVariable(value = "id") String id){
        if(StringUtils.isEmpty(id)){
            return ResponseEntity.badRequest().body(null);
        }
        Optional<AppLogModel> optional = appLogRepository.findById(id);
        if(optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getAll")
    @ApiOperation(value = "查询所有blog文档")
    public ResponseEntity<List<AppLogModel>> getAll(){
        Iterable<AppLogModel> AppLogModels = appLogRepository.findAll();
        Iterator<AppLogModel> iterator = AppLogModels.iterator();
        List<AppLogModel> blogs = new ArrayList<>();
        while (iterator.hasNext()){
            blogs.add(iterator.next());
        }
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据id更新blog文档")
    public ResponseEntity<Boolean> updateById(@RequestBody AppLogModel blog){
        String id = blog.getId();
        Assert.notNull(id, "blog id must not be null");
        try {
            appLogRepository.save(blog);
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
            appLogRepository.deleteById(id);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
        }
    }


    @PostMapping("/getPage")
    @ApiOperation(value = "查询blog分页文档")
    public ResponseEntity<PageEntity<List<AppLogModel>>> getPage(@RequestBody PageRequest pageRequest){
        Assert.isTrue(pageRequest.getIsPaged(), "isPaged must be true!");
        Pageable pageable = PageUtils.buildSpringPageRequest(pageRequest);
        Page<AppLogModel> AppLogModels = appLogRepository.findAll(pageable);
        List<AppLogModel> blogs = null;
        if (AppLogModels.getSize()>0) {
            Iterator<AppLogModel> iterator = AppLogModels.iterator();
            blogs = new ArrayList<>();
            while (iterator.hasNext()){
                blogs.add(iterator.next());
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setCount(AppLogModels.getTotalElements());
        pageEntity.setPageCount(AppLogModels.getTotalPages());
        pageEntity.setData(blogs);
        return ResponseEntity.ok(pageEntity);
    }


    @GetMapping("/contentSearch/{keyword}")
    @ApiOperation(value = "查询标题匹配关键字的文档")
    public ResponseEntity<List<AppLogModel>> contentSearch(@PathVariable(value = "keyword") String keyword){
        Assert.notNull(keyword, "keyword must not be null");
        List<AppLogModel> blogs = appLogRepository.findByContentLike(keyword);
        return ResponseEntity.ok(blogs);
    }


    @GetMapping("/contentSearchCustom/{keyword}")
    @ApiOperation(value = "自定义查询标题匹配关键字的文档")
    public ResponseEntity<List<AppLogModel>> contentSearchCustom(@PathVariable(value = "keyword") String keyword){
        Assert.notNull(keyword, "keyword must not be null");
        List<AppLogModel> blogs = appLogRepository.findByContentCustom(keyword);
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/searchByDTO")
    @ApiOperation(value = "根据关键字自定义搜索标题，内容匹配的文档")
    public ResponseEntity<PageEntity<List<AppLogModel>>> searchByKeyword(AppLogQueryDTO appLogQueryDTO, @RequestBody PageRequest pageRequest){
        Assert.notNull(appLogQueryDTO, "keyword must not be null");
        Assert.isTrue(pageRequest.getIsPaged(), "isPaged must be true!");
        PageEntity<List<AppLogModel>> page = appLogSearchService.queryPageByDTO(appLogQueryDTO,pageRequest);
        return ResponseEntity.ok(page);
    }
}
