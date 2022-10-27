#!/bin/sh

set -e

cd /paper/

java \
  -Xmx2g -Xms256m \
  -jar /paper/paper*.jar nogui