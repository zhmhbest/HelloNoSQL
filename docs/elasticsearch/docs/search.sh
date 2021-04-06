
# 分词解析搜索（_score代表匹配度）
GET /${IndexName}/_search?q=${FieldName}:${FieldValue}
GET /${IndexName}/_search
{
  "query": {
    "match": {
      "${FieldName}": ${FieldValue}
    }
  }
}

# 关键字搜索（必须完全一致）
GET /${IndexName}/_search
{
  "query": {
    "term": {
      "${FieldName}": ${FieldValue}
    }
  }
}

# and
GET /${IndexName}/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": { "${FieldName1}": ${FieldValue1} }
        },
        {
          "match": { "${FieldName2}": ${FieldValue2} }
        }
      ]
    }
  }
}

# or
GET /${IndexName}/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "match": { "${FieldName1}": ${FieldValue1} }
        },
        {
          "match": { "${FieldName2}": ${FieldValue2} }
        }
      ]
    }
  }
}

# not
GET /${IndexName}/_search
{
  "query": {
    "bool": {
      "must_not": [
        {
          "match": { "${FieldName}": ${FieldValue} }
        }
      ]
    }
  }
}

# range: gt gte lt lte
GET /${IndexName}/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "${FieldName}": {
              "gt": "${Value1}",
              "lt": "${Value2}"
            }
          }
        }
      ]
    }
  }
}

# 日期范围
GET /${IndexName}/_search
{
    "query": {
        "range": {
            "${FieldName}": {
                # y M w d
                # H h m s
                "gte": "now-1d/d",  # 当前时间的上一天, 四舍五入到天
                "lt":  "now/d"      # 当前时间, 四舍五入到天
            }
        }
    }
}

# 字段过滤
GET /${IndexName}/_search
{
  "_source": ["${FieldName1}", "${FieldName2}"]
}

# 排序: desc asc
GET /${IndexName}/_search
{
  "sort": [
    {
      "${FieldName}": {
        "order": "desc"
      }
    }
  ]
}

# 分页查询
GET /${IndexName}/_search
{
  "from": ${Offset},
  "size": ${PageSize}
}

### 高亮匹配的内容
GET /${IndexName}/_search
{
  "query": {
    "match": {
      "${FieldName}": ${FieldValue}
    }
  },
  "highlight": {
    "pre_tags": ["<a>"],
    "post_tags": ["</a>"],
    "fields": {
      "${FieldName}": {
        "pre_tags": ["<b>"],
        "post_tags": ["</b>"],
      }
    }
  }
}