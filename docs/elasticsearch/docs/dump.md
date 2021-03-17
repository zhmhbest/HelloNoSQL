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
