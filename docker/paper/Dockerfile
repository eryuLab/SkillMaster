FROM openjdk:17-jdk-alpine

WORKDIR /paper
ADD https://api.papermc.io/v2/projects/paper/versions/1.18.2/builds/388/downloads/paper-1.18.2-388.jar /paper-files/paper.jar

# /paper-filesをコンテナが提供するファイル群とする
COPY docker/paper/serverfiles/ /paper-files/
COPY localDependencies/ /paper-files/plugins/
COPY build/libs /paper-files/plugins/

CMD /bin/sh /paper-files/start-server.sh