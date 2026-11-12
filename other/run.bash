#!/usr/bin/env bash
set -e

cd "$(dirname "$0")/.."
exec bash gradlew run --args='--package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'
