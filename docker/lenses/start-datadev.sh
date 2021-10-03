#! /usr/bin/env bash
docker run -d -e ADV_HOST=127.0.0.1 -e EULA="${LENSES-EULA}" -e DISABLE="azure-documentdb,blockchain,bloomberg,coap,druid,kudu,voltdb,yahoo,hdfs" -e CONNECT_HEAP="2048m" --rm -p 3030:3030 -p 9092:9092 -p 8083:8083 -p 8081:8081 -p 2181:2181 -p 9200:9200 --name datadevbox lensesio/box:latest
