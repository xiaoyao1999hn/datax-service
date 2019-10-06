package org.datax.console.common.utils.ds;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.ds.entity.config.EsDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 11:48
 **/
@Slf4j
public class EsDsUtil implements DsUtil {

    @Override
    public void testConnect(DataXDsVO dsVO) {
        esExecute(dsVO, "/");
    }

    @Override
    public List<String> getTables(DataXDsVO ds) {
        List<String> indexList = new ArrayList<>();
        String dsl = "/_cat/indices";
        Response response = esExecute(ds, dsl);
        try {
            //green open tags_index                             17mOWf6HRCWvTdOsf_urkA 5 1        4 0 294.5kb 146.9kb
            //green open ods_index                              jyLL_RQ6RbW2gZpZQiigXQ 1 1    71510 0  55.6mb  27.8mb
            String indexStr = EntityUtils.toString(response.getEntity());
            String[] indexInfoList = indexStr.split("\\n");
            String[] temp = null;
            for (String indexInfo : indexInfoList) {
                temp = Arrays.stream(indexInfo.split(" ")).filter(StringUtils::isNotBlank).toArray(String[]::new);
                indexList.add(temp[2].trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indexList;
    }

    private Response esExecute(DataXDsVO DataXDsVO, String dsl) {
        RestClient restClient = null;
        Response response = null;
        try {
            restClient = getRestClient(DataXDsVO);
            response = restClient.performRequest("GET", dsl, Collections.singletonMap("pretty", "true"));
            if (response.getHost() == null) {
                throw new GlobalegrowExpcetion("连接不上ES，请检查连接信息");
            }
            return response;
        } catch (IOException e) {
            throw new GlobalegrowExpcetion("连接ES异常: " + e.getMessage());
        } finally {
            try {
                if (restClient != null) {
                    restClient.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static RestClient getRestClient(DataXDsVO DataXDsVO) {
        EsDsConfig dsConfigDO = DataXDsVO.getConfig();

        RestClient restClient = RestClient
                .builder(new HttpHost(dsConfigDO.getHost(), Integer.parseInt(dsConfigDO.getPort()), dsConfigDO.getScheme()))
                .build();
        return restClient;
    }


    private static class EsUtilHandler {
        private static final EsDsUtil esDsUtil = new EsDsUtil();
    }

    public static EsDsUtil getInstance() {
        return EsUtilHandler.esDsUtil;
    }
}
