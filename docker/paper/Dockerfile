FROM openjdk:17 as paper-builder

WORKDIR /work
ADD https://api.papermc.io/v2/projects/paper/versions/1.18.2/builds/388/downloads/paper-1.18.2-388.jar paper.jar
RUN java - jar paper.jar

# /paper-filesをコンテナが提供するファイル群とする
COPY --from=paper-builder /artifact/ /paper-files/

COPY docker/paper/serverfiles /paper-files/
COPY localDependencies/ /paper-files/plugins/
COPY build/libs /paper-files/plugins

RUN nkf -Lu --overwrite /paper-files/start-server.sh

CMD bash /paper-files/start-server.sh