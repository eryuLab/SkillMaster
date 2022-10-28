#!/bin/sh

set -e

\cp -Rf /paper-files/* /paper/

cd /paper/

java \
  -Xmx2g -Xms256m \
  -jar /paper/paper.jar nogui