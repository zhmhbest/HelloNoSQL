package org.example.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Elasticsearch
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.x/java-rest-high-supported-apis.html
 */
public class AppHighLevel {
    /**
     * @param min 闭
     * @param max 闭
     */
    private static int randomInteger(int min, int max) {
        if (min >= max) throw new IllegalArgumentException("min >= max?");
        return new Random().nextInt((max - min) + 1) + min;
    }

    private static String randomFirstName() {
        final String[] NAMES = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "仁", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟", "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "咎", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "鹰", "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀", "羊", "於", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景", "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "博", "印", "宿", "白", "怀", "蒲", "台", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "却", "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习", "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄", "阙", "东", "殴", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "竺", "权", "逯", "盖", "後", "桓", "公", "万", "俟", "司", "马", "上", "官", "欧", "阳", "夏", "侯", "诸", "葛", "闻", "人", "东", "方", "赫", "连", "皇", "甫", "尉", "迟", "公", "羊", "澹", "台", "公", "冶", "宗", "政", "濮", "阳", "淳", "于", "单", "于", "太", "叔", "申", "屠", "公", "孙", "仲", "孙", "轩", "辕", "令", "狐", "钟", "离", "宇", "文", "长", "孙", "慕", "容", "鲜", "于", "闾", "丘", "司", "徒", "司", "空", "亓", "官", "司", "寇", "仉", "督", "子", "车", "颛", "孙", "端", "木", "巫", "马", "公", "西", "漆", "雕", "乐", "正", "壤", "驷", "公", "良", "拓", "拔", "夹", "谷", "宰", "父", "谷", "粱", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "段", "干", "百", "里", "东", "郭", "南", "门", "呼", "延", "归", "海", "羊", "舌", "微", "生", "岳", "帅", "缑", "亢", "况", "后", "有", "琴", "梁", "丘", "左", "丘", "东", "门", "西", "门", "商", "牟", "佘", "佴", "伯", "赏", "南", "宫", "墨", "哈", "谯", "笪", "年", "爱", "阳", "佟", "第", "五", "言", "福", "百", "家", "姓", "终",};
        final int NAMES_BOUND = NAMES.length - 1;
        return NAMES[randomInteger(0, NAMES_BOUND)];
    }

    private static Map<String, Object> getRandomOne() {
        Random random = new Random();
        Map<String, Object> one = new HashMap<>();
        one.put("name", "小" + randomFirstName() + randomInteger(0, 99));
        one.put("comment", "评价" + random.nextDouble());
        one.put("gender", random.nextBoolean());
        int y = randomInteger(1980, 2020);
        int m = randomInteger(1, 12);
        int d = randomInteger(1, 28);
        one.put("birth", y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d));
        one.put("weight", 100 * random.nextFloat());
        one.put("class", randomInteger(100, 105));
        return one;
    }

    public static void demoIndex(RestHighLevelClient client, String indexName, boolean isRemove) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean isExist = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!isExist) {
            // 测试创建
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            CreateIndexResponse createIndexResponse =
                    client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println("创建索引：" + createIndexResponse.index());
        }
        if (isRemove) {
            // 测试删除
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse acknowledgedResponse =
                    client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            System.out.println("删除索引：" + acknowledgedResponse.isAcknowledged());
        }
    }

    public static void demoDocument(RestHighLevelClient client, String indexName, String docID) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, docID);
        boolean isExist = client.exists(getRequest, RequestOptions.DEFAULT);
        if (!isExist) {
            // 测试创建
            IndexRequest request = new IndexRequest(indexName);
            request.id("1");
            request.timeout(TimeValue.timeValueSeconds(10));
            request.source(getRandomOne());
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println("创建文档：" + response.status());
        }
        {
            // 测试更新
            UpdateRequest updateRequest = new UpdateRequest(indexName, docID);
            Map<String, Object> one = new HashMap<>();
            one.put("name", "小王");
            updateRequest.doc(one);
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println("更新文档：" + updateResponse.status());
        }
        {
            // 测试读取
            GetRequest getRequestContent = new GetRequest(indexName, docID);
            GetResponse getResponseContent = client.get(getRequestContent, RequestOptions.DEFAULT);
            Map<String, Object> one = getResponseContent.getSource();
            System.out.println("获取文档：" + one);
        }
        {
            // 测试删除
            DeleteRequest deleteRequest = new DeleteRequest(indexName, docID);
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println("删除文档：" + deleteResponse.status());
        }
    }

    public static void demoBatchInsert(RestHighLevelClient client, String indexName) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));
        ArrayList<Map<String, Object>> docList = new ArrayList<>();
        // 生成
        for (int i = 0; i < 50; i++) {
            docList.add(getRandomOne());
        }
        // 添加
        int id = 100;
        for (Map<String, Object> item : docList) {
            bulkRequest.add(new IndexRequest(indexName).id(Integer.toString(id)).source(item));
            id++;
        }
        // 提交
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("批量插入：" + bulkResponse.status());
        System.out.println("批量插入失败：" + bulkResponse.hasFailures());
    }

    public static void main(String[] args) throws IOException {
        final String indexName = "student10";
        final String docId = "1";
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ));
        demoIndex(client, indexName, false);
        demoDocument(client, indexName, docId);
        demoBatchInsert(client, indexName);
        client.close();
    }
}