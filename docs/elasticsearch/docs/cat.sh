# 列出_cat参数允许查看的内容
GET /_cat/
# =^.^=
# /_cat/allocation
# /_cat/shards
# /_cat/shards/{index}
# /_cat/master
# /_cat/nodes
# /_cat/tasks
# /_cat/indices
# /_cat/indices/{index}
# /_cat/segments
# /_cat/segments/{index}
# /_cat/count
# /_cat/count/{index}
# /_cat/recovery
# /_cat/recovery/{index}
# /_cat/health
# /_cat/pending_tasks
# /_cat/aliases
# /_cat/aliases/{alias}
# /_cat/thread_pool
# /_cat/thread_pool/{thread_pools}
# /_cat/plugins
# /_cat/fielddata
# /_cat/fielddata/{fields}
# /_cat/nodeattrs
# /_cat/repositories
# /_cat/snapshots/{repository}
# /_cat/templates
# /_cat/ml/anomaly_detectors
# /_cat/ml/anomaly_detectors/{job_id}
# /_cat/ml/trained_models
# /_cat/ml/trained_models/{model_id}
# /_cat/ml/datafeeds
# /_cat/ml/datafeeds/{datafeed_id}
# /_cat/ml/data_frame/analytics
# /_cat/ml/data_frame/analytics/{id}
# /_cat/transforms
# /_cat/transforms/{transform_id}

# 参数说明
#   ?v          显示表头
#   ?help       可显示的列及其说明
#   ?h=*        限制返回列（不指定返回默认的几列）
#   ?format     text | json | yaml
GET /_cat/nodes?v
GET /_cat/nodes?help
GET /_cat/nodes?h=ip,port
GET /_cat/nodes?format=json

# 查看所有集群健康状态
GET /_cat/health?v&h=cluster,status

# 查看所有节点
GET /_cat/nodes?v&h=ip,port,name,node.role,master