FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER xs

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /xs-system

WORKDIR /xs-system

EXPOSE 9526

ADD ./backend/xs-system-2.4.jar ./

CMD sleep 30;java -Djava.security.egd=file:/dev/./urandom -jar xs-system-2.4.jar
