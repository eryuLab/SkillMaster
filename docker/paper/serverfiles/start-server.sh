#!/bin/sh

set -e

cd /spigot/

JMX_PORT=${JMX_PORT:-7091}
JMX_BINDING=${JMX_BINDING:-0.0.0.0}
JMX_HOST=${JMX_HOST:-localhost}

java \
  -Dcom.sun.management.jmxremote.local.only=false \
  -Dcom.sun.management.jmxremote.port=${JMX_PORT} \
  -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT} \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Dcom.sun.management.jmxremote.host=${JMX_BINDING} \
  -Djava.rmi.server.hostname=${JMX_HOST} \
  -Xmx4g -Xms256m \
  -jar /spigot/spigot*.jar nogui