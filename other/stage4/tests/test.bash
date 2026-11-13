#!/usr/bin/env bash

echo 'Dependency chain'
./gradlew run --args='--config other/stage4/tests/config.json'\
' --package "A:1@other/stage4/tests/repositories/chain.json"'

echo 'Branches with a shared dependency'
./gradlew run --args='--config other/stage4/tests/config.json'\
' --package "A:1@other/stage4/tests/repositories/shared.json"'

echo 'Package without dependencies'
./gradlew run --args='--config other/stage4/tests/config.json'\
' --package "A:1@other/stage4/tests/repositories/empty.json"'

echo 'Error: cyclic dependencies'
./gradlew run --args='--config other/stage4/tests/config.json'\
' --package "A:1@other/stage4/tests/repositories/cycle.json"'
