
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_MENUEXCEPTIONS_H
#define ELEMENTAL_SQL_IMPLEMENTATION_MENUEXCEPTIONS_H

#include <exception>
#include <string>

using namespace std;

class InvalidFileExportException : public exception {
    string message;

public:
    InvalidFileExportException(const string& details)
            : message("\033[1;31m[INVALID FILE EXPORT]\033[0m Cannot export database to the provided file path.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m:\033[0m " + details) {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};

class InvalidFileImportException : public exception {
    string message;

public:
    InvalidFileImportException(const string& details)
            : message("\033[1;31m[INVALID FILE IMPORT]\033[0m Cannot import database from the provided file path.\n"
                      "\033[1;31m\033[4mERROR\033[0m\033[1;31m:\033[0m " + details) {
        ofstream outFile("output.txt", ios::out);
        outFile << "!" << endl;
        outFile << message << endl;
        outFile.close();
    }

    const char* what() const noexcept override {
        return message.c_str();
    }
};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_MENUEXCEPTIONS_H
