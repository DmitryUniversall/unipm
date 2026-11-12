#!/usr/bin/env bash

echo 'Direct dependencies and local properties'
./gradlew run --args='--config other/stage2/tests/config.json --package "junit:junit:4.13.2@https://repo.maven.apache.org/maven2"'

echo 'Package from JSON'
./gradlew run --args='--config other/stage2/tests/config.json --package-info other/stage2/tests/packages/junit-jupiter-api.json'

echo 'Package without dependencies'
./gradlew run --args='--config other/stage2/tests/config.json --package "org.apiguardian:apiguardian-api:1.1.2@https://repo.maven.apache.org/maven2"'

echo 'Error: package version not found'
./gradlew run --args='--config other/stage2/tests/config.json --package "junit:junit:0.0.0-unipm-missing@https://repo.maven.apache.org/maven2"'
