@echo off
title GeorgesBot
mvn clean package && echo. && java -Dfile.encoding=UTF-8 -jar target\BendemBot.jar