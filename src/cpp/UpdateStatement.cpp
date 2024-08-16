
#include "UpdateStatement.h"

UpdateStatement::UpdateStatement(const string &query) : Statement(query) {}

bool UpdateStatement::parse() {
    errors();
    regex update_regex(R"(^\s*UPDATE\s+(\S+)\s+SET\s+(.+?)(?:\s+WHERE\s+(.+))?\s*$)", regex_constants::icase);
    smatch matches;

    if (!regex_search(query, matches, update_regex) || matches.size() != 4) {
        return false;
    }

    table_name = matches[1];
    parseSetClause(matches[2]);
    if(matches[3].matched) {
        parseWhereClause(matches[3]);
    }
    return true;
}

void UpdateStatement::parseSetClause(const string &setClause) {
    regex setRegex(R"(\s*(\w+)\s*=\s*(\"[^"]*\"|'[^']*'|\w+)(?:\s*,)?)", regex_constants::icase);
    sregex_iterator setIt(setClause.begin(), setClause.end(), setRegex);
    sregex_iterator setEnd;

    while (setIt != setEnd) {
        smatch match = *setIt;
        string columnName = match.str(1);
        string value = match.str(2);

        // Remove quotes if they exist
        if ((value.front() == '\"' && value.back() == '\"') || (value.front() == '\'' && value.back() == '\'')) {
            value = value.substr(1, value.size() - 2);
        }

        // Add the change to the changes list
        changes[columnName] = value;

        setIt++;
    }
}

void UpdateStatement::parseWhereClause(const string &whereClause) {
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
        } else if (operatorSymbol == "!=") {
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

void UpdateStatement::execute(Database &db) {
    if (!parse()) {
        return;
    }

    Table &table = db.getTable(table_name);
    for (Row& row : table.getRows()) {
        bool shouldUpdateRow = true;
        for (const auto &filter : filters) {
            if (filter && !filter->applyFilter(row)) {
                shouldUpdateRow = false;
                break;
            }
        }
        if (shouldUpdateRow) {
            for (const auto &change : changes) {
                row.updateColumnValue(change.first, change.second);
            }
        }
    }

    // Insert into file for native format
    ofstream outFile("output.txt", ios::out);
    db.getTable(table_name).printTableInFile(outFile);
    outFile.close();
}

void UpdateStatement::errors() {
    regex update_regex(R"(^\s*UPDATE\s+(\S+)(?:\s+SET\s*(.*?))?(?:\s+WHERE\s*(.*))?\s*$)", regex_constants::icase);
    smatch matches;

    if (!regex_match(query, matches, update_regex)) {
        throw SyntaxException("Invalid syntax for UPDATE statement.");
    }

    if (!matches[1].matched) {
        throw MissingArgumentsException("UPDATE is missing table name.");
    }

    if (!matches[2].matched) {
        throw IncompleteInputException("UPDATE statement is missing SET clause.");
    }

    regex set_clause_input_regex(R"(^\s*.*WHERE.*\s*$)", regex_constants::icase);
    if (matches[2].matched && regex_match(matches[2].str(), set_clause_input_regex)) {
        throw IncompleteInputException("Incomplete input after SET clause.");
    }

    if (matches[2].matched && !regex_match(matches[2].str(), SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in SET clause.");
    }

    if (matches[3].matched && matches[3].str().empty()) {
        throw MissingArgumentsException("WHERE clause requires conditions. None provided.");
    }

    string table_name_for_errors = matches[1].str();
    if (!regex_match(table_name_for_errors, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in table name.");
    }

    if (matches[2].matched) {
        string set_clause = matches[2].str();
        if (!regex_match(set_clause, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
            throw SyntaxException("Mismatched or mixed quotes in SET clause.");
        }

        regex assignment_regex(R"(\s*(\w+)\s*=\s*(\"[^"]*\"|'[^']*'|\w+)(?:\s*,)?)", regex_constants::icase);
        sregex_iterator assignment_it(set_clause.begin(), set_clause.end(), assignment_regex);
        sregex_iterator assignment_end;

        while (assignment_it != assignment_end) {
            smatch match = *assignment_it;
            string value = match.str(2);
            if (value.front() != '\"' || value.back() != '\"') {
                throw SyntaxException("Values in SET clause must be surrounded by quotes.");
            }

            assignment_it++;
        }
    }

    string set_clause_error = matches[2].str();
    regex set_clause_syntax_check(R"(^\s*(\S+)\s*=\s*('[^']*'|"[^"]*")\s*$)", regex_constants::icase);
    if (!regex_match(set_clause_error, set_clause_syntax_check)) {
        throw SyntaxException("Invalid syntax in SET clause.");
    }

    if (matches[3].matched) {
        string where_clause = matches[3].str();
        SyntaxRegexPatterns::checkWhereClauseErrors(where_clause);
    }
}
