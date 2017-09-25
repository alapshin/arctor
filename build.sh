#!/bin/bash

set -euo pipefail

./gradlew --no-daemon \
    -PciBuild -PdisablePreDex \
    clean build
