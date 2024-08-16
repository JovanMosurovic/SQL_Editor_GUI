
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_TABLEEXCEPTIONS_H
#define ELEMENTAL_SQL_IMPLEMENTATION_TABLEEXCEPTIONS_H

#include <fstream>
#include <iostream>
#include <string>

using namespace std;

class InvalidTableNameException : public exception {
    string message;

public:
    InvalidTableNameException(const string& tableName)
            : message("\033[1;31m[TABLE CREATION FAILED]\033[0m Cannot create table with invalid name format.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: Table name \033[0m" + tableName + "\033[1;31m is not typed in the correct format.\033[0m\n"
                      "\033[1;31m\033[4mFormat\033[0m\033[1;31m:\033[0m Only English letters are allowed with underline character. No spaces or special characters are allowed.") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class RowOutOfBoundsException : public exception {
    string message;

public:
    RowOutOfBoundsException(long long int rowIndex, size_t rowsSize)
            : message("\033[1;31m[ROW ACCESS FAILED]\033[0m Cannot access row with the provided index.\n") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
        if (rowIndex < 0) {
            message += "\033[1;31m\033[4mERROR\033[0m\033[1;31m: Row index cannot be negative.\033[0m";
        } else {
            message += "\033[1;31m\033[4mERROR\033[0m\033[1;31m: Row index \033[0m" + to_string(rowIndex) + "\033[1;31m is out of bounds.\033[0m\n";
            if (rowsSize == 0) {
                message += "\033[1;31mThe table is empty.\033[0m";
            } else {
                message += "\033[1;31mMaximum row index is \033[1;31m\033[0m" + to_string(rowsSize - 1);
            }
        }
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class RowDoesNotExistException : public exception {
    string message;

public:
    RowDoesNotExistException()
            : message("\033[1;31m[ROW REMOVAL FAILED]\033[0m Cannot remove row that does not exist.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: The row does not exist in the table.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class InvalidDataForAddRowException : public exception {
    string message;

public:
    InvalidDataForAddRowException(size_t rowDataSize, size_t columnsSize)
            : message("\033[1;31m[INSERT FAILED]\033[0m Cannot add a new row with mismatching column count.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "Number of row data elements \033[0m(" + to_string(rowDataSize) + ")\033[1;31m does not match the number of columns \033[0m(" + to_string(columnsSize) + ")\033[1;31m in the table.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class InvalidDataForUpdateException : public exception {
    string message;

public:
    InvalidDataForUpdateException(size_t newDataSize, size_t columnsSize)
            : message("\033[1;31m[UPDATE FAILED]\033[0m Cannot update row with mismatching column count.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "Number of new data elements \033[0m(" + to_string(newDataSize) + ")\033[1;31m does not match the number of columns \033[0m(" + to_string(columnsSize) + ")\033[1;31m in the table.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class ColumnDoesNotExistException : public exception {
    string message;

public:
    ColumnDoesNotExistException(const string& columnName)
            : message("\033[1;31m[COLUMN ACCESS FAILED]\033[0m Cannot access column with the provided name.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m: "
                      "Column \033[0m" + columnName + "\033[1;31m does not exist in the table.\033[0m") {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

#endif //ELEMENTAL_SQL_IMPLEMENTATION_TABLEEXCEPTIONS_H
