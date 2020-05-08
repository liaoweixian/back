FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER xs-api

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /xs-api

WORKDIR /xs-api

EXPOSE 9525

ADD ./xs-api-2.4.jar ./

#ADD ./FAIO_x64.dll ./

CMD sleep 1;java -Djava.security.egd=file:/dev/./urandom -jar xs-api-2.4.jar
