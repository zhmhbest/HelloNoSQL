# 查看所有索引
GET /_cat/indices?v

# 查看索引状态
GET /_cat/indices/${IndexName}?format=json

# 索引是否存在
HEAD /${IndexName}

# 查看索引配置、字段
GET /${IndexName}

# 创建索引（自动匹配模板）
PUT /${IndexName}

# 删除索引
DELETE /${IndexName}