FROM openjdk:17-jdk-alpine
WORKDIR /waterfall

ADD https://api.papermc.io/v2/projects/waterfall/versions/1.19/builds/506/downloads/waterfall-1.19-506.jar /waterfall-files/waterfall.jar

COPY docker/waterfall/serverfiles/ /waterfall-files/

CMD /bin/sh /waterfall-files/start-server.sh