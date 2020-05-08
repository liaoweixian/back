FROM mysql:5.7

MAINTAINER jc

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

COPY ./db/1schema.sql /docker-entrypoint-initdb.d

COPY ./db/xs.sql /docker-entrypoint-initdb.d

