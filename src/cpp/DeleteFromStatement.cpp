
#include "DeleteFromStatement.h"

DeleteFromStatement::DeleteFromStatement(const string &query) : Statement(query) {}

bool DeleteFromStatement::parse() {
    errors();
    regex delete_regex(R"(^\s*DELETE\s+FROM\s+(\S+)(?:\s+WHERE\s+(.+))?\s*$)", regex_constants::icase);
    smatch matches;

    if (!regex_search(query, matches, delete_regex) || matches.size() != 3) {
        return false;
    }

    table_name = matches[1];
    if (table_name.front() == '\'' || table_name.front() == '\"' || table_name.front() == '`') {
        table_name = table_name.substr(1, table_name.size() - 2);
    }
    if(matches[2].matched) {
        where_condition = matches[2];
        parseWhereClause(where_condition);
    }
    return true;
}

void DeleteFromStatement::execute(Database &db) {
    if (!parse()) {
        return;
    }

    Table &table = db.getTable(table_name);
    for (auto it = table.getRows().begin(); it != table.getRows().end(); ) {
        bool shouldDeleteRow = true;
        for (const auto &filter : filters) {
            if (filter && !filter->applyFilter(*it)) {
                shouldDeleteRow = false;
                break;
            }
        }
        if (shouldDeleteRow) {
            db.removeRowFromTable(table_name, distance(table.getRows().begin(), it));
            it = table.getRows().begin();  // Reset the iterator as the container has been modified
        } else {
            ++it;
        }
    }

    // Insert into file for native format
    ofstream outFile("output.txt", ios::out);
    db.getTable(table_name).printTableInFile(outFile);
    outFile.close();
}

void DeleteFromStatement::parseWhereClause(const string &whereClause) {
    regex whereRegex(R"(\s*(\w+)\s*(=|!=)\s*(\"[^"]*\"|'[^']*'|\w+)(?:\s*(AND))?)", regex_constants::icase);
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

void DeleteFromStatement::errors() {
    regex delete_from_regex(R"(^\s*DELETE\s+FROM(?:\s+(\S+))?(?:\s+WHERE\s*(.*))?\s*$)", regex_constants::icase);
    smatch matches;

    if (!regex_match(query, matches, delete_from_regex)) {
        throw SyntaxException("Invalid syntax for DELETE FROM statement.");
    }

    if (!matches[1].matched) {
        throw MissingArgumentsException("DELETE FROM is missing table name.");
    }

    string table_name_for_errors = matches[1].str();
    if (!regex_match(table_name_for_errors, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in table name.");
    }

    if (matches[2].matched) {
        string where_clause = matches[2].str();
        SyntaxRegexPatterns::checkWhereClauseErrors(where_clause);
    }
}


