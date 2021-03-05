package org.example.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
// import org.elasticsearch.common.transport.InetSocketTransportAddress;    /*5.6.4*/
import org.elasticsearch.common.transport.TransportAddress;                 /*7.10.1*/
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class AppTransport {
    private static int searchHits(SearchHits hits) {
        int i =0;
        for (SearchHit hit : hits.getHits()) {
            Map<String, Object> row = hit.getSourceAsMap();
            System.out.println(row);
            i++;
        }
        return i;
    }
    public static void main(String[] args) {
        // 参数准备
        final String ES_CLUSTER = "";
        final String ES_HOST = "127.0.0.1";
        final int ES_PORT = 9300;
        final String INDEX_NAME = "";
        final int BATCH_SIZE = 10000;
        final TimeValue TIME_OUT = new TimeValue(80000);
        InetAddress ES_ADDRESS = null;
        try {
            ES_ADDRESS = InetAddress.getByName(ES_HOST);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // 创建连接
        // 5.6.4
        TransportClient client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", ES_CLUSTER).build()
        )
                /*5.6.4*/ //.addTransportAddress(new InetSocketTransportAddress(ES_ADDRESS, ES_PORT));
                /*7.10.1*/.addTransportAddress(new TransportAddress(ES_ADDRESS, ES_PORT));

        // 查询条件
        BoolQueryBuilder where = QueryBuilders.boolQuery();
        where.should(QueryBuilders.termQuery("字段", "value"));

        // 查询
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME)
                .setQuery(where)
                .addSort(SortBuilders.fieldSort("_doc"))
                .setSize(BATCH_SIZE)
                .setScroll(TIME_OUT)
                .execute().actionGet();
        int count = 0;
        count += searchHits(searchResponse.getHits());
        for (;;) {
            // 从上次查询末尾续查BATCH_SIZE
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(TIME_OUT)
                    .execute().actionGet();
            if (0 == searchResponse.getHits().getHits().length) break;
            count += searchHits(searchResponse.getHits());
        }
        System.out.println(count);
    }
}