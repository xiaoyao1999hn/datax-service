package org.datax.console.ds.controller;

import org.datax.console.base.entity.ResResult;
import org.datax.console.common.utils.PageUtils;
import org.datax.console.common.utils.Query;
import org.datax.console.ds.service.DataXDsService;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.enums.DataSourceType;
import org.datax.console.enums.EffectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 10:10
 **/
@RestController
@RequestMapping("/ods/ds")
public class DataXDsController {
    @Autowired
    private DataXDsService dataXDsService;

    @GetMapping("/list")
    public ResResult list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<DataXDsVO> dynamicDatasourceList = dataXDsService.list(query);
        Long total = dataXDsService.count(query);
        PageUtils pageUtils = new PageUtils(dynamicDatasourceList, total);
        return ResResult.ok(pageUtils);
    }

    @GetMapping("/dsOption")
    public ResResult optionList(@RequestParam Map<String, Object> params) {
        List<Map> optionList = dataXDsService.optionList(params);
        return ResResult.ok(optionList);
    }

    @GetMapping("/listTable/{id}")
    public ResResult listTable(@PathVariable("id") Long id) {
        //查询列表数据
        DataXDsVO ds = dataXDsService.get(id);
        if (ds == null) {
            throw new RuntimeException("not found datasource for id: " + id);
        }
        return ResResult.ok(dataXDsService.listTable(ds.getId()));
    }


    @GetMapping("/listColumn")
    public ResResult listColumn(Long id, String tableName, String typeName, String indexName) {
        return ResResult.ok(dataXDsService.listColumn(id, tableName, typeName, indexName));
    }

    @GetMapping("/listColumnName")
    public ResResult listColumnName(Long id, String tableName, String typeName, String indexName) {
        return ResResult.ok(dataXDsService.listColumnName(id, tableName, typeName, indexName));
    }

    @GetMapping("/info/{id}")
    ResResult get(@PathVariable("id") Long id) {
        DataXDsVO dynamicDatasource = dataXDsService.get(id);
        return ResResult.ok(dynamicDatasource);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResResult save(@RequestBody DataXDsVO ds) throws Exception {
        ds.setStatus(EffectStatus.EFFECTED.getType());
        if (dataXDsService.save(ds) > 0) {
            return ResResult.ok();
        }
        return ResResult.error();
    }

    /**
     * 修改
     */
    @PostMapping("/modify")
    public ResResult update(@RequestBody DataXDsVO ds) throws Exception {
        if (dataXDsService.modify(ds) > 0) {
            return ResResult.ok();
        }
        return ResResult.error();
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove/{id}")
    public ResResult remove(@PathVariable("id") Long id) {
        if (dataXDsService.remove(id) > 0) {
            return ResResult.ok();
        }
        return ResResult.error();
    }

    /**
     * 删除
     */
    @GetMapping("/dsType")
    public ResResult dsType() {
        List<Map> data=Arrays.stream(DataSourceType.values()).map(x->{
            Map temp =new HashMap();
            temp.put("value",x.getType());
            temp.put("name",x.getDescription());
            return temp;
        }).collect(Collectors.toList());
        return ResResult.ok(data);
    }
}
