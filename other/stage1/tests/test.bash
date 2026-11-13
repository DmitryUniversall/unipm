#!/usr/bin/env bash

echo 'Valid configuration'
./gradlew run --args='--config other/stage1/tests/configs/valid.json'\
' --package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'

echo 'Overwrite permission from CLI'
./gradlew run --args='--config other/stage1/tests/configs/existing-output.json --overwrite-output'\
' --package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'

echo 'Error: negative max_depth'
./gradlew run --args='--config other/stage1/tests/configs/invalid-max-depth.json'\
' --package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'

echo 'Error: existing output without overwrite permission'
./gradlew run --args='--config other/stage1/tests/configs/existing-output.json'\
' --package "org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"'
