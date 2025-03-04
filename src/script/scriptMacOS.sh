#!/bin/bash

TOTAL_COMMANDS=24
COUNTER=$TOTAL_COMMANDS

cd "/project_path/cpp"

echo "$COUNTER commands remaining"
((COUNTER--))
javac -h . JavaInterface.java

# Compile all .cpp files to object files
echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" cpp_JavaInterface.cpp -o cpp_JavaInterface.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" Column.cpp -o Column.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" ConsoleUtils.cpp -o ConsoleUtils.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" CreateTableStatement.cpp -o CreateTableStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" CustomFormat.cpp -o CustomFormat.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" Database.cpp -o Database.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" DeleteFromStatement.cpp -o DeleteFromStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" DropTableStatement.cpp -o DropTableStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" EqualityFilter.cpp -o EqualityFilter.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" InequalityFilter.cpp -o InequalityFilter.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" InsertIntoStatement.cpp -o InsertIntoStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" Menu.cpp -o Menu.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" Row.cpp -o Row.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" SelectStatement.cpp -o SelectStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" ShowTablesStatement.cpp -o ShowTablesStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" SQLFormat.cpp -o SQLFormat.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" Statement.cpp -o Statement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" SyntaxRegexPatterns.cpp -o SyntaxRegexPatterns.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" Table.cpp -o Table.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" UpdateStatement.cpp -o UpdateStatement.o

# Link all object files into a single shared library (.dylib for macOS)
echo "$COUNTER commands remaining"
((COUNTER--))
g++ -shared -o libnative.dylib cpp_JavaInterface.o Column.o ConsoleUtils.o CreateTableStatement.o CustomFormat.o Database.o DeleteFromStatement.o DropTableStatement.o EqualityFilter.o InequalityFilter.o InsertIntoStatement.o Menu.o Row.o SelectStatement.o ShowTablesStatement.o SQLFormat.o Statement.o SyntaxRegexPatterns.o Table.o UpdateStatement.o

# Move the shared library to the specified directory
echo "$COUNTER commands remaining"
((COUNTER--))
mkdir -p "project_path/native/native.dylib/native"
mv libnative.dylib "project_path/native/native.dylib/native/libnative.dylib"