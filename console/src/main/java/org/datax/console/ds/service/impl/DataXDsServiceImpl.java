package org.datax.console.ds.service.impl;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.datax.console.base.enums.DataSourceType;
import org.datax.console.common.utils.ds.*;
import org.datax.console.ds.dao.DataXDsDao;
import org.datax.console.ds.entity.config.KafkaDsConfig;
import org.datax.console.ds.entity.config.RabbitMQDsConfig;
import org.datax.console.ds.service.DataXDsService;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.JDBCType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/10/16 11:15
 **/
@Service
public class DataXDsServiceImpl implements DataXDsService {

    @Resource
    private DataXDsDao dsDao;

    @Override
    public List<Map> optionList(Map param) {
        return dsDao.optionList(param);
    }

    @Override
    public DataXDsVO queryByName(String name) {
        return dsDao.queryByName(name);
    }

    @Override
    public DsUtil chooseDsUtil(DataSourceType type) {
        switch (type){
            case MYSQL:return RdbmsDsUtil.getInstance();
            case ORACLE:return RdbmsDsUtil.getInstance();
            case ES:return EsDsUtil.getInstance();
            case HIVE:return RdbmsDsUtil.getInstance();
            case PRESTO:return RdbmsDsUtil.getInstance();
            case RABBITMQ:return RabbitMQDsUtil.getInstance();
            case KAFKA:return KafkaDsUtil.getInstance();
            case HBASE:return RdbmsDsUtil.getInstance();
            default:return RdbmsDsUtil.getInstance();
        }
    }

    @Override
    public List<String> listTable(Long dsId) {
        //查询列表数据
        DataXDsVO ds = get(dsId);
        if (Objects.isNull(ds)) {
            return Collections.EMPTY_LIST;
        }
        DsUtil dsUtil=chooseDsUtil(DataSourceType.fromType(ds.getType()));
        return Optional.ofNullable(dsUtil).map(x -> x.getTables(ds))
                .map(x -> x.stream().filter(StringUtils::isNotBlank).sorted().collect(Collectors.toList()))
                .orElse(Collections.EMPTY_LIST);
    }

    @Override
    public List<DataXColumn> listColumn(Long dataSourceId, String tableName, String typeName, String indexName) {
        DataXDsVO ds = dsDao.queryById(dataSourceId);
        if (ds == null) {
            return Collections.emptyList();
        }
        // 数据源类型
        DataSourceType dsType = DataSourceType.fromType(ds.getType());
        // es
        try {
            if (DataSourceType.ES.equals(dsType)) {
                OdsEsDsConfigDO configDO = (OdsEsDsConfigDO) ds.build().getConfig();
                return Optional.ofNullable(EsMappingCache.INSTANCE.getMapping(configDO.getHost(), Integer.parseInt(configDO.getPort()), typeName, indexName))
                        .map(x -> {
                            x.forEach(c -> {
                                ValueTypes valueType = FieldTypeConvertUtil.INSTANCE.convertToValueType(
                                        EsFieldTypeEnum.fromType(c.getTypeName()));
                                c.setType(valueType.getType());
                                c.setTypeName(valueType.getDescription());
                            });
                            return x;
                        }).orElse(Collections.emptyList());
            } else {
                DbTypeEnum dbType = DbTypeEnum.fromDataSourceType(ds.getType());
                OdsRdbmsDsConfigDO configDO=(OdsRdbmsDsConfigDO) ds.build().getConfig();
                String jdbcUrl = dbType.genJdbcUrl(configDO.getHost(), Integer.parseInt(configDO.getPort()), configDO.getInstanceName());
                return Optional.ofNullable(TableMetaCache.getTableMeta(null, jdbcUrl, configDO.getUserName(), configDO.getPassword(), tableName))
                        .map(x -> x.getAllColumns(true))
                        .map(x -> x.stream().map(m -> {
                            ValueTypes valueType = FieldTypeConvertUtil.INSTANCE.convertToValueType(JDBCType.valueOf(m.getDataType()));
                            return new DataColumnInfo(m.getColumnName(), valueType.getType(),
                                    valueType.getDescription(), m.getRemarks());
                        }).collect(Collectors.toList()))
                        .orElse(Collections.emptyList());
            }
        } catch (IllegalArgumentException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> listColumnName(Long dataSourceId, String tableName, String typeName, String indexName) {
        return Optional.ofNullable(listColumn(dataSourceId, tableName, typeName, indexName))
                .map(x -> x.stream().map(DataXColumn::getName).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer save(DataXDsVO dsVO) throws Exception {
        return dsDao.insert(dsVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer modify(DataXDsVO dsVO) throws Exception {
        return dsDao.update(dsVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer remove(Long id) {
        return dsDao.deleteById(id);
    }

    @Override
    public List<DataXDsVO> list(Map condition) {
        return dsDao.selectByParams(condition);
    }

    @Override
    public Long count(Map condition) {
        return dsDao.count(condition);
    }

    @Override
    public DataXDsVO get(Long id) {
        return dsDao.queryById(id);
    }
}
