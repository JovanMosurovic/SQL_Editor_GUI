
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_MENU_H
#define ELEMENTAL_SQL_IMPLEMENTATION_MENU_H

#include <iostream>
#include <limits>
#include "Colors.h"
#include "Menu.h"
#include "ConsoleUtils.h"
#include "SyntaxRegexPatterns.h"
#include "MenuExceptions.h"
#include "Database.h"
#include "SyntaxExceptions.h"
#include "CreateTableStatement.h"
#include "DropTableStatement.h"
#include "SelectStatement.h"
#include "InsertIntoStatement.h"
#include "UpdateStatement.h"
#include "DeleteFromStatement.h"
#include "ShowTablesStatement.h"
#include "SQLFormat.h"
#include "CustomFormat.h"


class Menu {

public:
    static void importDatabaseMenu();
    static void mainMenu(Database& database);
    static void exportDatabaseMenu(Database& database);
    static shared_ptr<Statement> parseSQLQuery(const string& query);

private:
    static void cleanConsole();
    static void finishProgram();
    static vector<pair<string, int>> readSQLQuery();
    static void highlightKeywords(string& line);

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_MENU_H
