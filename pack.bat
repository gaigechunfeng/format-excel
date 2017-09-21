@cd /d %~dp0
@set home=%cd%
@set jre_home=D:\jdk1.8.0_74\jre
@set dcs_maven_home=Z:\maven\apache-maven-3.2.5
@set dcs_maven_settings=Z:\settings.xml

@if exist set-maven.bat call set-maven.bat
@if exist set-jre.bat call set-jre.bat

@if exist lib rd /s /q lib

@echo copy dependencies and package
@call %dcs_maven_home%\bin\mvn dependency:copy-dependencies --settings %dcs_maven_settings% -DoutputDirectory=lib -DincludeScope=compile
@call %dcs_maven_home%\bin\mvn package --settings %dcs_maven_settings% -Dmaven.test.skip=true

@set dist_home=dist\app
@if exist %dist_home% rd /s /q %dist_home%

@md %dist_home%
@md %dist_home%\bin
@md %dist_home%\lib
@md %dist_home%\jre

@xcopy /e /i /y bin %dist_home%\bin
@xcopy /e /i /y lib %dist_home%\lib
@xcopy /e /i /y config %dist_home%\config
@xcopy /e /i /y %jre_home% %dist_home%\jre
@copy /y readme.md %dist_home%\readme.md