#!/usr/bin/env bash

set -euo pipefail

./gradlew --no-daemon -PciBuild clean build
