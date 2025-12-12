@echo off
echo Fixing file encoding issues...

rem Удаляем русские комментарии из Java файлов
for /r src\main\java %%f in (*.java) do (
    echo Processing %%f
    powershell -Command "(Get-Content '%%f' -Raw) -replace '//[^\x00-\x7F].*', '// Comment removed due to encoding issue' | Set-Content '%%f' -Encoding UTF8"
)

echo Done!
pause