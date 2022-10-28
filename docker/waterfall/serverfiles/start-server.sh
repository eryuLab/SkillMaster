#!/bin/sh

set -e

rm -r /waterfall/plugins/ || true

# overwrite server directory
cp -Rf /waterfall-files/* /waterfall/

cd /waterfall/
java -jar waterfall.jar