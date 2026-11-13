#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

exec bash gradlew --console=plain run \
    --args='--config other/demo_test_config.json --package-info other/demo_test_package_info.json'
