# unipm

## Запуск

Нужны JDK 25, Node.js и npm

Подготовка:

```bash
npm install
```

Затем тестовый запуск

```bash
./gradlew run --args="--package junit:junit:4.13.2@https://repo.maven.apache.org/maven2"
```

`npm install` устанавливает Mermaid CLI для создания SVG. По умолчанию результат сохраняется
в файл `graph.svg`

## Параметры

- `--package groupId:artifactId:version@URL`: имя, версия пакета и адрес Maven-репозитория
- `--package-info PATH`: JSON с данными пакета вместо `--package`
- `--config PATH`: путь к конфигу. По умолчанию `other/generic_config.json`
- `--overwrite-output`: разрешить перезапись SVG файла, если такой уже существует

Важно: указывать нужно `--package` или `--package-info` (только 1 из них)

Пример JSON для `--package-info`:

```json
{
  "name": "junit:junit",
  "version": "4.13.2",
  "repository": "https://repo.maven.apache.org/maven2"
}
```

Подробнее см в `JsonPackageInfoDocument`

## Конфиг

Образец: `other/demo_config.json`.

- `test_mode`: при `true` зависимости читаются из локального JSON, например `--package A:1@путь`.
  Имена тестовых пакетов состоят из заглавных латинских букв. При `false` используется
  Maven-репозиторий
- `output_file`: путь к выходному файлу с расширением `.svg`
- `overwrite_output`: разрешить перезапись существующего `.svg` файла 
- `ascii_tree`: вывести дерево зависимостей в терминал (`true`/`false`)
- `max_depth`: максимальная глубина графа. `0` оставляет только исходный пакет, `1` добавляет
  прямые зависимости и тд.
- `exclude_substring`: исключить зависимости, имя которых содержит эту строку. Регистр учитывается.
  Пустая строка отключает фильтр.
- `load_order`: вывести порядок загрузки, в котором зависимости идут раньше зависимых пакетов.

Все поля обязательны, кроме `overwrite_output` и `load_order`: при отсутствии они равны `false`.

Подробнее см в `JsonAppConfigDocument`

## Тесты

Тестовые сценарии и данные лежат в `other/stageN/tests/`.  Сценарии также проверяют ошибочные
входные данные, поэтому часть запусков должна завершаться с ошибкой.
