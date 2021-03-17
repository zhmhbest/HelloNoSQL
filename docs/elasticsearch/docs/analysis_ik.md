
[IK分词器](https://github.com/medcl/elasticsearch-analysis-ik)

```bash
#!/bin/bash
ES_VERSION='7.10.1'
cd elasticsearch-${ES_VERSION}/plugins
# https://github.com.cnpmjs.org/medcl/elasticsearch-analysis-ik/releases
wget https://github.com.cnpmjs.org/medcl/elasticsearch-analysis-ik/releases/download/v${ES_VERSION}/elasticsearch-analysis-ik-${ES_VERSION}.zip
unzip elasticsearch-analysis-ik-${ES_VERSION}.zip -d ik
```

```bash
# 测试插件加载
# elasticsearch-plugin list
GET /_cat/plugins
# name|component  |version
# ... |analysis-ik|7.10.1

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
