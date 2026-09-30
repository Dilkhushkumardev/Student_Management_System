@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
"%JAVA_HOME%\bin\java.exe" -Dspring.profiles.active=h2 -Dnet.bytebuddy.experimental=true -jar target\smart-attend-1.0.0.jar
