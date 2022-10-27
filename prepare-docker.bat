call rd /s /q build

call gradlew build || goto :onerror

REM https://stackoverflow.com/a/23812179
call echo n | copy /-y docker\paper\eula.txt docker\paper\serverfiles\eula.txt

call docker-compose build -m 2g || goto :onerror

call docker-compose down
call docker-compose up --abort-on-container-exit

exit /b

:onerror
echo Failed with error. Quiting batch...
exit /b %errorlevel%