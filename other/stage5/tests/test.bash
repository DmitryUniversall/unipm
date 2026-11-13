#!/usr/bin/env bash

echo 'JUnit SVG and ASCII tree'
./gradlew run --args='--config other/stage5/tests/configs/junit.json'\
' --package "junit:junit:4.13.2@https://repo.maven.apache.org/maven2"'

echo 'JUnit Jupiter SVG and shared dependency'
./gradlew run --args='--config other/stage5/tests/configs/jupiter.json'\
' --package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'

echo 'API Guardian without dependencies'
./gradlew run --args='--config other/stage5/tests/configs/apiguardian.json'\
' --package "org.apiguardian:apiguardian-api:1.1.2@https://repo.maven.apache.org/maven2"'

echo 'Cyclic graph SVG and ASCII tree'
./gradlew run --args='--config other/stage5/tests/configs/cycle.json'\
' --package "A:1@other/stage5/tests/repositories/cycle.json"'
