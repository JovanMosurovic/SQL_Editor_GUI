@echo off

rem This script is used to create .o files and create the native.dll library in the project. 

set TOTAL_COMMANDS=24
set /A COUNTER=%TOTAL_COMMANDS%

cd /d "\project_path\cpp"

echo %COUNTER% commands remaining
set /A COUNTER-=1
javac -h . JavaInterface.java 

rem Compile all .cpp files to object files
echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" cpp_JavaInterface.cpp -o cpp_JavaInterface.o -m64

rem Compile all .cpp files to object files
echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" Column.cpp -o Column.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" ConsoleUtils.cpp -o ConsoleUtils.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" CreateTableStatement.cpp -o CreateTableStatement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" CustomFormat.cpp -o CustomFormat.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" Database.cpp -o Database.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" DeleteFromStatement.cpp -o DeleteFromStatement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" DropTableStatement.cpp -o DropTableStatement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" EqualityFilter.cpp -o EqualityFilter.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" InequalityFilter.cpp -o InequalityFilter.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" InsertIntoStatement.cpp -o InsertIntoStatement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" Menu.cpp -o Menu.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" Row.cpp -o Row.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" SelectStatement.cpp -o SelectStatement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" ShowTablesStatement.cpp -o ShowTablesStatement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" SQLFormat.cpp -o SQLFormat.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" Statement.cpp -o Statement.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" SyntaxRegexPatterns.cpp -o SyntaxRegexPatterns.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" Table.cpp -o Table.o -m64

echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -std=c++17 -c -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" UpdateStatement.cpp -o UpdateStatement.o -m64

rem Link all object files into a single DLL
echo %COUNTER% commands remaining
set /A COUNTER-=1
g++ -shared -o native.dll -m64 cpp_JavaInterface.o Column.o ConsoleUtils.o CreateTableStatement.o CustomFormat.o Database.o DeleteFromStatement.o DropTableStatement.o EqualityFilter.o InequalityFilter.o InsertIntoStatement.o Menu.o Row.o SelectStatement.o ShowTablesStatement.o SQLFormat.o Statement.o SyntaxRegexPatterns.o Table.o UpdateStatement.o

rem Move the DLL to the specified directory
echo %COUNTER% commands remaining
set /A COUNTER-=1
move native.dll "project_path\native\native.dll"
