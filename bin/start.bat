@cd /d %~dp0..
@set home=%cd%
@set jre_home=%home%\jre

@set dcs_classpath=config;
@setLocal enabledelayedexpansion
@for /f "delims= " %%i in ('dir /b/a-d %home%\lib\*.jar') do @(
 set dcs_classpath=!dcs_classpath!;lib\%%i
)
@set dcs_classpath=!dcs_classpath!
@setLocal disabledelayedexpansion

@start %jre_home%\bin\javaw -cp %dcs_classpath% com.wk.excel.App
