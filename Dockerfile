FROM arm32v7/groovy
MAINTAINER Ihor Lavryniuk
WORKDIR /opt
USER root
COPY app.groovy /opt/app.groovy

ENTRYPOINT ["groovy", "-Dpackaging.type=jar", "-Dgroovy.grape.report.downloads=true", "-Dorg.slf4j.simpleLogger.showDateTime=true", "-Dorg.slf4j.simpleLogger.showThreadName=false", "-Dorg.slf4j.simpleLogger.dateTimeFormat=yyyy-MM-dd HH:mm:ss:SSS", "/opt/app.groovy"]
