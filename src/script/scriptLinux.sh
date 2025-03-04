#!/bin/bash

TOTAL_COMMANDS=24
COUNTER=$TOTAL_COMMANDS

cd "/project_path/src/cpp"

echo "$COUNTER commands remaining"
((COUNTER--))
javac -h . JavaInterface.java 

# Compile all .cpp files to object files
echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" cpp_JavaInterface.cpp -o cpp_JavaInterface.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" Column.cpp -o Column.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" ConsoleUtils.cpp -o ConsoleUtils.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" CreateTableStatement.cpp -o CreateTableStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" CustomFormat.cpp -o CustomFormat.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" Database.cpp -o Database.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" DeleteFromStatement.cpp -o DeleteFromStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" DropTableStatement.cpp -o DropTableStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" EqualityFilter.cpp -o EqualityFilter.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" InequalityFilter.cpp -o InequalityFilter.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" InsertIntoStatement.cpp -o InsertIntoStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" Menu.cpp -o Menu.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" Row.cpp -o Row.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" SelectStatement.cpp -o SelectStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" ShowTablesStatement.cpp -o ShowTablesStatement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" SQLFormat.cpp -o SQLFormat.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" Statement.cpp -o Statement.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" SyntaxRegexPatterns.cpp -o SyntaxRegexPatterns.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" Table.cpp -o Table.o

echo "$COUNTER commands remaining"
((COUNTER--))
g++ -std=c++17 -c -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" UpdateStatement.cpp -o UpdateStatement.o

# Link all object files into a single shared object
echo "$COUNTER commands remaining"
((COUNTER--))
g++ -shared -o libnative.so cpp_JavaInterface.o Column.o ConsoleUtils.o CreateTableStatement.o CustomFormat.o Database.o DeleteFromStatement.o DropTableStatement.o EqualityFilter.o InequalityFilter.o InsertIntoStatement.o Menu.o Row.o SelectStatement.o ShowTablesStatement.o SQLFormat.o Statement.o SyntaxRegexPatterns.o Table.o UpdateStatement.o

# Move the shared object to the specified directory
echo "$COUNTER commands remaining"
((COUNTER--))
mv libnative.so "/project_path/native"
