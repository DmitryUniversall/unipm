#!/usr/bin/env bash

echo 'Transitive dependencies and a shared package'
./gradlew run --args='--config other/stage3/tests/configs/default.json --package "A:1@other/stage3/tests/repositories/graph.json"'

echo 'Maximum depth and a shorter path to a shared package'
./gradlew run --args='--config other/stage3/tests/configs/depth.json --package "A:1@other/stage3/tests/repositories/graph.json"'

echo 'Exclude packages containing D'
./gradlew run --args='--config other/stage3/tests/configs/filter.json --package "A:1@other/stage3/tests/repositories/graph.json"'

echo 'Cyclic dependencies'
./gradlew run --args='--config other/stage3/tests/configs/default.json --package "A:1@other/stage3/tests/repositories/cycle.json"'
