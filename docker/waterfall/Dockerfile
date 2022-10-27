FROM --platform=linux/amd64 openjdk:17 as waterfall-provider
WORKDIR /waterfall

ADD https://api.papermc.io/v2/projects/waterfall/versions/1.19/builds/506/downloads/waterfall-1.19-506.jar waterfall.jar

COPY docker/waterfall/serverfiles /waterfall-files

CMD bash /waterfall-files/start-server.sh