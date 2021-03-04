<link rel="stylesheet" href="https://zhmhbest.gitee.io/hellomathematics/style/index.css">
<script src="https://zhmhbest.gitee.io/hellomathematics/style/index.js"></script>

# [ElasticSearch](../index.html)

[toc]

## 运行环境

学习环境下减少ES内存占用，进入ES目录

`vim ./config/jvm.options`

```bash
# Xms represents the initial size of total heap space
# Xmx represents the maximum size of total heap space

-Xms256m
-Xmx256m
```

### ES-Head

```bash
# 下载
wget 'https://github.com/mobz/elasticsearch-head/archive/master.zip' -O 'es-head.zip'
unzip './es-head.zip'
mv './elasticsearch-head-master/crx/es-head.crx' './'
rm -rf './elasticsearch-head-master'
```

### 启动

```bash
# 启动ES
elasticsearch-${VERSION}/bin/elasticsearch

# 启动Kibana
kibana-${VERSION}/bin/kibana
```

#### 访问

- <http://localhost:5601>
- <http://localhost:5601/app/dev_tools#/console>

## REST API

### 索引操作

```bash
# 删除索引
DELETE /hello-index

# 创建索引
PUT /hello-index
{
  "mappings": {
    "properties": {
      "name": {
        "type": "keyword"
      },
      "gender": {
        "type": "boolean"
      },
      "group": {
        "type": "long"
      },
      "birth": {
        "type": "date"
      },
      "desc": {
        "type": "text"
      }
    }
  }
}

# 查看索引
GET /hello-index
```

### 文档操作

```bash
# 创建文档（指定ID）
PUT /hello-index/_doc/1
{
  "name": "张三",
  "gender": true,
  "group": 1,
  "birth": "1998-07-07",
  "desc": "暖男"
}
# 创建文档（随机ID）
POST /hello-index/_doc
{
  "name": "李四",
  "gender": true,
  "group": 1,
  "birth": "1998-01-01",
  "desc": "直男"
}
POST /hello-index/_doc
{
  "name": "李七",
  "gender": false,
  "group": 1,
  "birth": "1998-02-02",
  "desc": "靓女"
}
POST /hello-index/_doc
{
  "name": "王五",
  "gender": true,
  "group": 2,
  "birth": "1998-03-03",
  "desc": "渣男"
}
POST /hello-index/_doc
{
  "name": "赵六",
  "gender": false,
  "group": 3,
  "birth": "1998-10-10",
  "desc": "美女"
}

# 查看所有文档
# GET /hello-index/_doc/_search
GET /hello-index/_search

# 修改文档
# POST /hello-index/_doc/1/_update
POST /hello-index/_update/1
{
  "doc": {
    "name": "张三三"
  }
}

# 查看文档
GET /hello-index/_doc/1
```

### 分词器

[IK分词器](https://github.com/medcl/elasticsearch-analysis-ik)

```bash
cd elasticsearch-?.??.?/plugins
wget https://github.com.cnpmjs.org/medcl/elasticsearch-analysis-ik/releases/download/v7.10.1/elasticsearch-analysis-ik-7.10.1.zip
unzip elasticsearch-analysis-ik-7.10.1.zip -d ik
```

```bash
# 测试插件加载
elasticsearch-plugin list
```

```bash
GET /_analyze
{
  "analyzer": "ik_smart",
  "text": "北京故宫博物院建立于1925年10月10日，位于北京故宫紫禁城内。"
}
GET /_analyze
{
  "analyzer": "ik_max_word",
  "text": "北京故宫博物院建立于1925年10月10日，位于北京故宫紫禁城内。"
}

GET /_analyze
{
  "analyzer": "keyword",
  "text": "北京故宫博物院建立于1925年10月10日，位于北京故宫紫禁城内。"
}

GET /_analyze
{
  "analyzer": "standard",
  "text": "北京故宫博物院建立于1925年10月10日，位于北京故宫紫禁城内。"
}
```

### 条件查询

#### 分词与关键字

```bash
# 分词解析搜索（_score代表匹配度）
GET /hello-index/_search?q=desc:直男
GET /hello-index/_search
{
  "query": {
    "match": {
      "desc": "直男"
    }
  }
}

# 关键字搜索
GET /hello-index/_search
{
  "query": {
    "term": {
      "name": "李四"
    }
  }
}
```

#### 多条件

```bash
# and
GET /hello-index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "desc": "男"
          }
        },
        {
          "match": {
            "group": 1
          }
        }
      ]
    }
  }
}

# or
GET /hello-index/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "desc": "女"
          }
        },
        {
          "match": {
            "group": 2
          }
        }
      ]
    }
  }
}

# not
GET /hello-index/_search
{
  "query": {
    "bool": {
      "must_not": [
        {
          "match": {
            "desc": "男"
          }
        }
      ]
    }
  }
}

# filter
# gt gte lt lte
GET /hello-index/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "birth": {
              "gt": "1998-01-30",
              "lt": "1998-02-10"
            }
          }
        }
      ]
    }
  }
}
```

### 高亮

```bash
GET /hello-index/_search
{
  "query": {
    "match": {
      "desc": "直男"
    }
  },
  "highlight": {
    "pre_tags": "<a>",
    "post_tags": "</a>",
    "fields": {
      "desc": {}
    }
  }
}
```

### 其它

```bash
# 字段过滤
GET /hello-index/_search
{
  "_source": ["name", "gender"]
}

# 排序
GET /hello-index/_search
{
  "sort": [
    {
      "birth": {
        "order": "desc"
      }
    }
  ]
}

# 分页查询
GET /hello-index/_search
{
  "from": 0,
  "size": 2
}
GET /hello-index/_search
{
  "from": 1,
  "size": 2
}
```

## ES Clients

- [Clients](https://www.elastic.co/guide/en/elasticsearch/client/index.html)

### JS调用

```bash
mkdir HelloJSElasticSearch
cd HelloJSElasticSearch
npm init -f
npm -S i @elastic/elasticsearch
```

`index.js`

```js
const { Client } = require('@elastic/elasticsearch');
const client = new Client({
    node: 'http://localhost:9200',
    // auth: {
    //     username: 'elastic',
    //     password: 'changeme'
    // },
    // ssl: {
    //     ca: fs.readFileSync('./cacert.pem'),
    //     rejectUnauthorized: false
    // }
});

client.search({
    index: 'hello-index',
    body: {
        query: {
            match: {
                desc: '男'
            }
        }
    }
}).then(res => {
    console.log(res.body);
}).catch(err => {
    console.log(`${err}`);
});
```

### JAVA调用

```bash
mvn archetype:generate "-DgroupId=org.example.es" "-DartifactId=HelloJavaElasticSearch" "-DarchetypeArtifactId=maven-archetype-quickstart" "-DinteractiveMode=false"
cd HelloJavaElasticSearch
vim pom.xml
```

`pom.xml`

```xml
<!-- ... -->
<dependencies>
    <!-- <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>3.8.1</version>
        <scope>test</scope>
    </dependency> -->

    <!-- Java API: 二进制传输 -->
    <!-- <dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>transport</artifactId>
        <version>7.10.1</version>
    </dependency> -->

    <!-- Rest Low-level API -->
    <dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>elasticsearch-rest-client</artifactId>
        <version>7.10.1</version>
    </dependency>

    <!-- Rest High-level API （推荐） -->
    <dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>elasticsearch-rest-high-level-client</artifactId>
        <version>7.10.1</version>
    </dependency>
</dependencies>
<!-- ... -->
```

#### Low-level

```java
package org.example.es;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * Elasticsearch
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/5.6
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.10.1
 */
public class App {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200, "http")
        ).build();

        // 5.6.4
        // Response response = restClient.performRequest("GET", "/");
        // 7.10.1
        Request request = new Request("GET", "/");
        Response response = restClient.performRequest(request);

        RequestLine requestLine = response.getRequestLine();
        System.out.println(requestLine);

        HttpHost host = response.getHost();
        System.out.println(host);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        Header[] headers = response.getHeaders();
        for (Header header: headers) {
            System.out.println(header);
        }

        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);

        restClient.close();
    }
}
```

#### High-level

```java
package org.example.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import java.io.IOException;

/**
 * Elasticsearch
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.x/java-rest-high-supported-apis.html
 */
public class App {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            new HttpHost("localhost", 9200, "http")
        ));
        // ...
        client.close();
    }
}
```
