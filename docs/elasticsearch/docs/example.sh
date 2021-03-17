# 查看所有模板
GET /_cat/templates?v&h=name
# 创建模板
PUT /_template/student
{
  "order": 1,
  "index_patterns": "student*",
  "settings": {
    "index": {}
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "keyword"
      },
      "gender": {
        "type": "boolean"
      },
      "birth": {
        "type": "date"
      },
      "weight": {
        "type": "float"
      },
      "class": {
        "type": "integer"
      }
    }
  },
  "aliases": {}
}
# 查看模板
GET /_template/student
HEAD /_template/student
# DELETE /_template/student

# ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■

# 查看所有索引
GET /_cat/indices?v&h=index
# 匹配模板创建索引
PUT /student1
# 查看索引
GET /student1
HEAD /student1
# DELETE /student1

# ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■

# 创建文档
PUT /student1/_doc/1
{
  "name": "张三",
  "gender": false,
  "birth": "1999-10-10",
  "weight": 120.6,
  "class": 101
}
POST /student1/_doc/
{
  "name": "李四",
  "gender": false,
  "birth": "2000-09-09",
  "weight": 99.2,
  "class": 101
}
# 修改文档
POST /student1/_update/1
{
    "doc": {
        "name": "张三三"
    }
}
# 查看所有文档
GET /student1/_search

