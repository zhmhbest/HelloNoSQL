<link rel="stylesheet" href="https://zhmhbest.gitee.io/hellomathematics/style/index.css">
<script src="https://zhmhbest.gitee.io/hellomathematics/style/index.js"></script>

# [Redis](../index.html)

[TOC]

## 环境准备

### Windows

下载[Redis](https://github.com/microsoftarchive/redis/releases)

```batch
REM 启动服务
redis-server.exe redis.windows.conf

REM 连接服务
redis-cli.exe -h localhost -p 6379
```

### Linux

下载[Redis](http://www.redis.cn/download.html)

```bash
yum -y install gcc-c++ make
redis_version='5.0.8'
wget "http://download.redis.io/releases/redis-${redis_version}.tar.gz" 
tar -xvf redis-${redis_version}.tar.gz
pushd redis-${redis_version}
make distclean
make
make install
ll /usr/local/bin/redis-*
cp ./redis.conf /usr/local/etc/
popd; rm -rf ./redis-${redis_version}/

# 创建自动关联配置的启动程序
echo 'redis-server /usr/local/etc/redis.conf'>'/usr/local/bin/redis-start'
chmod 711 '/usr/local/bin/redis-start'

# 修改为后台启动
sed -i '/daemonize/s/no/yes/' '/usr/local/etc/redis.conf'
grep ^daemonize '/usr/local/etc/redis.conf'
```

```bash
# 启动服务
redis-start

# 查看服务
netstat -anp | grep redis-server

# 连接服务
redis-cli -h localhost -p 6379
```

#### 配置文件

```bash
redis_conf='/usr/local/etc/redis.conf'

# 查看登录密码
egrep '^(# )?requirepass' ${redis_conf}

# 是否后台运行
grep ^daemonize '/usr/local/etc/redis.conf'

# PID
grep ^pidfile ${redis_conf}

# 数据库数量
grep ^databases ${redis_conf}

# 日志级别 = debug | verbose | notice | warning
grep ^loglevel ${redis_conf}

# 持久化：条件 = 时间(s) 修改Key数量
grep ^save ${redis_conf}

# 持久化：错误后是否继续工作
grep ^stop-writes-on-bgsave-error ${redis_conf}

# 持久化：是否压缩
grep ^rdbcompression ${redis_conf}

# 持久化：是否校验
grep ^rdbchecksum ${redis_conf}

# 持久化：保存目录
grep ^dir ${redis_conf}
```

## 性能测试

```bash
# -c 并发连接数量
# -n 每个连接请求数量
redis-benchmark -h localhost -p 6379 -c 100 -n 100000
```

## [Redis命令](http://www.redis.cn/commands.html)

### 连接

>- `PING`：是否可以正常登录
>- `AUTH password`：若启用密码则应使用此命令登录

### 数据库

>- `SELECT index`：切换数据库
>- `DBSIZE`：查看当前数据库占用大小
>- `KEYS *`：查看当前数据库所有KEY

### Hello

```txt
SET hello "Hello Redis!"
TYPE hello
GET hello
KEYS *
DEL hello
EXISTS hello
FLUSHALL
```

### Expire

>- `EXPIRE key seconds`：设置key有效时间
>- `TTL key`：查看有效时间（-1：永久、-2：已过期 ）

### GET/SET

>- `SETEX key value`：设置Key-Value
>- `SETEX key seconds value`：设置Key-Value和过期时间
>- `SETNX key value`：如果不存在则设置
>- `MSET key value [key value ...]`：设置多个值
>- `MGET key [key ...]`：获取多个值
>- `MSETNX key value [key value ...]`：如果均不存在则设置（原子性操作）
>- `GETSET key value`：先获取再设置

#### 设置对象

```txt
MSET user:1:name "Zhang3" user:1:gender 0
MGET user:1:name user:1:gender
```

### String

>- `SET key value`：设置
>- `APPEND key value`：追加
>- `GET key`=`GETRANGE key 0 -1`：获取
>- `GETRANGE key start end`：裁剪
>- `SETRANGE key offset value`：从offset起替换为value

#### 自增减

```txt
SET count "0"
GET count

INCR count
GET count

INCRBY count 5
GET count

DECR count
GET count

DECRBY count 5
GET count

DEL count
```

### List

>- `RPUSH key value [value ...]`：创建List（右侧）
>- `LPUSH key value [value ...]`：创建List（左侧）
>- `RPOP key`：移除List尾部第一个元素
>- `LPOP key`：移除List头部第一个元素
>- `LRANGE key start stop`：裁剪List（不修改原列表）
>- `LRANGE key 0 -1`：查看列表内元素
>- `LTRIM key start stop`：裁剪List
>- `LLEN key`：List元素个数
>- `LINDEX key index`：List第index个元素的值
>- `LREM key count value`：移除List中值为value从左起前count个
>- `LSET key index value`：将List中第index个值替换为指定的值
>- `RPOPLPUSH source destination`：从一个List右侧移到另一个List左侧

### Set

>- `SADD key  member [member ...]`：创建Set
>- `SMEMBERS key`：打印Set中所有元素
>- `SISMEMBER key member`：元素是否在Set中
>- `SCARD key`：Set中元素个数
>- `SREM key member [member ...]`：移除Set中元素
>- `SMOVE source destination member`：移动source中的member到destination中
>- `SINTER key [key ...]`：交集
>- `SDIFF key [key ...]`：差集
>- `SUNION key [key ...]`：并集

### Hash

>- `HSET key field value`：创建Hash
>- `HMSET key field value [field value ...]`：创建Hash设置多个field
>- `HGET key field`：获取Hash中field对应的值
>- `HMGET key field [field ...]`：获取Hash中多个field对应的值
>- `HLEN key`：获取Hash中包含field的数量
>- `HEXISTS key field`：判断field是否存在
>- `HKEYS key`：所有field名称
>- `HVALS key`：所有field对应的值
>- `HINCRBY key field increment`：指定field自增increment
>- `HSETNX key field value`：不存在field时则创建

```txt
HSET hash1 field1 "Hello"
HGET hash1 field1
```

### ZSet（有序集合）

>- `ZADD key [NX|XX] [CH] [INCR] score member [score member ...]`：不存在field时则创建
>   - XX: 仅仅更新存在的成员，不添加新成员。
>   - NX: 不更新存在的成员。只添加新成员。
>   - CH: 修改返回值为发生变化的成员总数。
>   - INCR: 当ZADD指定这个选项时，成员的操作就等同ZINCRBY命令，对成员的分数进行递增操作。
>- `ZRANGE key start stop [WITHSCORES]`：返回指定范围的元素
>- `ZREM key member [member ...]`：删除元素
>- `ZCARD key`：元素个数
>- `ZCOUNT key min max`：指定score范围内元素的个数

```txt
ZADD zset1 1 "one"
ZADD zset1 3 "three"
ZADD zset1 2 "two"
ZRANGE zset1 0 -1
ZRANGE zset1 0 -1 WITHSCORES
```

## Clients

### NodeJS

```bash
mkdir test
pushd test
npm init -f
npm -S i redis
touch index.js
```

`index.js`

```js
const redis = require('redis');
const client = redis.createClient(6379, '127.0.0.1');
// client.auth(123456);
client.on('connect', connected);
async function connected() {
    await new Promise((resolve, reject) => {
        client.set('name', 'Zhang4', (err, data) => err ? reject(err) : resolve(data));
    }).then(res => {
        console.log('set', res)
    })
    await new Promise((resolve, reject) => {
        client.get('name', (err, data) => err ? reject(err) : resolve(data));
    }).then(res => {
        console.log('get', res)
    })
    console.log("Done!");
    process.exit(0);
}
```
