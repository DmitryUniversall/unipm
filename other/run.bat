@echo off
setlocal

pushd "%~dp0.."
call gradlew.bat run --args="--package org.junit.jupiter:junit-jupiter-api:5.10.0@https://repo.maven.apache.org/maven2"
set "run_exit=%errorlevel%"
popd

exit /b %run_exit%
