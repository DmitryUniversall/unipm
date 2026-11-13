#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

exec bash gradlew --console=plain run \
    --args='--config other/demo_config.json'\
' --package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'
