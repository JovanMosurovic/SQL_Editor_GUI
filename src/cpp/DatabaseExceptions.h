
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_DATABASEEXCEPTIONS_H
#define ELEMENTAL_SQL_IMPLEMENTATION_DATABASEEXCEPTIONS_H

#include "TableExceptions.h"

class DatabaseNameException : public exception {
    string message;

public:
    DatabaseNameException(const string& databaseName)
            : message("\033[1;31m[DATABASE CREATION FAILED]\033[0m Cannot create database with the provided name.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "Database name cannot be empty!\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class TableAlreadyExistsException : public exception {
    string message;

public:
    TableAlreadyExistsException(const string& tableName)
            : message("\033[1;31m[INVALID TABLE NAME ERROR]\033[0m Cannot create table with the provided name.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "Table \033[0m" + tableName + "\033[1;31m already exists in the database.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class TableDoesNotExistException : public exception {
    string message;

public:
    TableDoesNotExistException(const string& tableName)
            : message("\033[1;31m[TABLE OPERATION FAILED]\033[0m Cannot perform the requested operation.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "Table \033[0m" + tableName + "\033[1;31m does not exist in the database.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class FileNotOpenedException : public exception {
    string message;

public:
    FileNotOpenedException(const string& filename)
            : message("\033[1;31m[FILE OPENING FAILED]\033[0m Cannot open the file with the provided name.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "File with path\033[0m " + filename + "\033[1;31m cannot be opened.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class InvalidFormatException : public exception {
    string message;

public:
    InvalidFormatException(const string& message)
            : message("\033[1;31m[INVALID FORMAT ERROR]\033[0m Cannot parse the provided format.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      + message + "\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

#endif //ELEMENTAL_SQL_IMPLEMENTATION_DATABASEEXCEPTIONS_H
