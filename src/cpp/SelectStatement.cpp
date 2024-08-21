
#include "SelectStatement.h"

SelectStatement::SelectStatement(const string &query) : Statement(query), join_table_name(""), join_table_alias(""), join_column(""), join_column2("") {}

bool SelectStatement::parse() {
    errors();
    regex selectRegex(
            R"(^\s*SELECT\s+(.*?)\s+FROM\s+(\S+)(?:\s+(\S+))?(?:\s+(?:INNER\s+)?JOIN\s+(\S+)\s+(\S+)\s+ON\s+(\S+\.\S+)\s*=\s*(\S+\.\S+))?\s*(?:WHERE\s+(.+))?\s*;?\s*$)",
            regex_constants::icase);
    smatch matches;
    if (!regex_search(query, matches, selectRegex) || matches.size() < 4) {
        return false;
    }

    // Parsing FROM clause
    table_name = matches[2];
    table_alias = matches[3].length() > 0 ? matches[3] : table_name;
    aliasMap[table_alias] = table_name;

    // Parsing INNER JOIN clause, if present
    if (matches[4].matched && matches[5].matched) {
        join_table_name = matches[4];
        join_table_alias = matches[5];
        aliasMap[join_table_alias] = join_table_name;  // Map the joined table alias to its name
        join_column = matches[6].str().substr(matches[6].str().find('.') + 1);
        join_column2 = matches[7].str().substr(matches[7].str().find('.') + 1);
    }

    // Parsing SELECT columns
    string columns = matches[1];
    regex columnRegex(R"((\w+)\.(\w+)|(\w+)|(\*))");
    sregex_iterator columnIt(columns.begin(), columns.end(), columnRegex);
    sregex_iterator columnEnd;
    while (columnIt != columnEnd) {
        smatch match = *columnIt;
        string tableNamePart = match.str(1);
        string columnNamePart = match.str(2);
        string column = match.str(3);
        string star = match.str(4);

        if (!star.empty()) {
            column_names.emplace_back("*");
        } else {
            if (!tableNamePart.empty()) {
                if (aliasMap.find(tableNamePart) == aliasMap.end()) {
                    cout << red << "Alias not found: " << resetColor << bold << tableNamePart << resetColor << endl;
                    throw InvalidColumnReferenceException("Column reference does not match any table alias or name.");
                }
            }
            column_names.push_back(columnNamePart.empty() ? column : columnNamePart);
        }
        ++columnIt;
    }

    // Parsing WHERE clause, if present
    if (matches.size() >= 9 && matches[8].matched) {
        parseWhereClause(matches[8]);
    }

    return true;
}


void SelectStatement::parseWhereClause(const string &whereClause) {
    regex whereRegex(R"(\s*(\w+)\s*(=|!=|<>|<|>)\s*(\"[^"]*\"|'[^']*'|\w+)(?:\s*(AND|OR))?)", regex_constants::icase);
    sregex_iterator whereIt(whereClause.begin(), whereClause.end(), whereRegex);
    sregex_iterator whereEnd;

    while (whereIt != whereEnd) {
        smatch match = *whereIt;
        string columnName = match.str(1);
        string operatorSymbol = match.str(2);
        string value = match.str(3);

        // Remove quotes if they exist
        if ((value.front() == '\"' && value.back() == '\"') || (value.front() == '\'' && value.back() == '\'')) {
            value = value.substr(1, value.size() - 2);
        }

        // Create appropriate filter
        shared_ptr<IFilter> currentFilter;
        if (operatorSymbol == "=") {
            currentFilter = make_shared<EqualityFilter>(columnName, value);
        } else if (operatorSymbol == "!=" || operatorSymbol == "<>") {
            currentFilter = make_shared<InequalityFilter>(columnName, value);
        } else {
            currentFilter = nullptr;  // Handle other operators if needed
        }

        // Add the filter to the filters list
        if (currentFilter) {
            filters.push_back(currentFilter);
        }

        whereIt++;
    }
}

void SelectStatement::execute(Database &db) {
    if (!parse()) {
        cout << red << "Parsing failed. Unable to execute SQL query." << resetColor << endl;
        return;
    }

    if (!join_table_name.empty()) {
        db.selectFromTable(table_name, table_alias, column_names, filters, join_table_name, join_column, join_column2);
    } else {
        db.selectFromTable(table_name, table_alias, column_names, filters);
    }
}

void SelectStatement::errors() {
    regex select_regex(
            R"(^\s*SELECT\s+(.*?)\s+FROM\s+(\S+)?(?:\s+(\S+))?(?:\s+(?:INNER\s+)?JOIN\s+(\S+)\s+(\S+)\s+ON\s+(\S+\.\S+)\s*=\s*(\S+\.\S+))?\s*(WHERE\s+(.+))?\s*;?\s*$)",
            regex_constants::icase);
    smatch matches;

    if (!regex_search(query, matches, select_regex)) {
        throw SyntaxException("Invalid syntax for SELECT statement.");
    }

    if (matches[1].str().empty()) {
        throw MissingArgumentsException("SELECT statement is missing column names.");
    }

    if (!matches[2].matched) {
        throw MissingArgumentsException("SELECT statement is missing table name.");
    }

    if (matches[4].matched && (!matches[5].matched || !matches[6].matched || !matches[7].matched)) {
        throw SyntaxException("Incomplete JOIN clause. Ensure table name, alias and join conditions are specified correctly.");
    }

    if (matches[8].matched) {
        SyntaxRegexPatterns::checkWhereClauseErrors(matches[9]);
    } else if (matches[9].str().empty() && regex_search(query, regex(R"(WHERE)", regex_constants::icase))) {
        throw MissingArgumentsException("WHERE clause is specified but no conditions are provided.");
    }
}

