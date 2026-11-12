#!/usr/bin/env bash
set -e

project_dir=$(dirname "$0")

bash "$project_dir/gradlew" -p "$project_dir" -q installDist
exec bash "$project_dir/build/install/unipm/bin/unipm" "$@"
