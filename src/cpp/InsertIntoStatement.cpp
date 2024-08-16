
#include "InsertIntoStatement.h"

InsertIntoStatement::InsertIntoStatement(const string &query) : Statement(query) {}

bool InsertIntoStatement::parse() {
    errors();
    regex insertIntoRegex(R"(^\s*INSERT\s+INTO\s+(\S+)?\s+(?:\(([^)]+)\))?\s+VALUES\s+(?:\(([^)]+)\))?$)", regex_constants::icase);
    smatch matches;
    if (!regex_search(query, matches, insertIntoRegex) || matches.size() != 4) {
        return false;
    }
    table_name = matches[1];
    if (table_name.front() == '\'' || table_name.front() == '\"' || table_name.front() == '`') {
        table_name = table_name.substr(1, table_name.size() - 2);
    }
    string columns = matches[2];
    regex columnRegex(R"(\w+|['"`]([^'"`]+)['"`])");
    sregex_iterator it(columns.begin(), columns.end(), columnRegex);
    sregex_iterator end;
    while (it != end) {
        string column = it->str();
        if(column.front() == '\'' || column.front() == '\"' || column.front() == '`') {
            column = column.substr(1, column.size() - 2);
        }
        column_names.push_back(column);
        it++;
    }
    string valuesTemp = matches[3];
    regex valuesRegex(R"(\w+|['"`]([^'"`]+)['"`])");
    sregex_iterator it2(valuesTemp.begin(), valuesTemp.end(), valuesRegex);
    sregex_iterator end2;
    while (it2 != end2) {
        string value = it2->str();
        if(value.front() == '\'' || value.front() == '\"' || value.front() == '`') {
            value = value.substr(1, value.size() - 2);
        }
        values.push_back(value);
        it2++;
    }
    return true;
}

void InsertIntoStatement::execute(Database &db) {
    if(!parse()) {
        return;
    }
    db.insertIntoTable(table_name, column_names, values);

    // Insert into file for native format
    ofstream outFile("output.txt", ios::out);
    db.getTable(table_name).printTableInFile(outFile);
    outFile.close();
}

void InsertIntoStatement::errors() {
    regex insert_into_regex(R"(^\s*INSERT\s+INTO\s+(\S+)?\s+(\(?([^)]+)\)?)?\s*VALUES\s*(\(?\s*([^)]*)\)?)?\s*$)",regex_constants::icase);
    regex insert_into_values_with_quotes_regex(R"(^(['"].*['"](,\s*['"].*['"])*$))");
    regex insert_into_values_syntax_regex(R"(\s*\((\S+)\s*(?:,\s*(\S+)\s*)*\s*\))", regex_constants::icase);
    smatch matches;
    regex_match(query, matches, insert_into_regex);

    string table_name_for_errors = matches[1].str();
    string column_list_with_brackets = matches[2].str();
    string column_list = matches[3].str();
    string values_list_with_brackets = matches[4].str();
    string values_list = matches[5].str();

    if (!matches[1].matched && !matches[2].matched && !matches[3].matched) {
        throw MissingArgumentsException("INSERT INTO is missing table name, column list and values list.");
    } else if (!matches[1].matched && !matches[2].matched) {
        throw MissingArgumentsException("INSERT INTO is missing table name and column list.");
    } else if (!matches[1].matched && !matches[3].matched) {
        throw MissingArgumentsException("INSERT INTO is missing table name and values list.");
    } else if (!matches[2].matched && !matches[3].matched) {
        throw MissingArgumentsException("INSERT INTO is missing column list and values list.");
    } else if (!matches[1].matched) {
        throw MissingArgumentsException("INSERT INTO is missing table name.");
    } else if (!matches[2].matched) {
        throw MissingArgumentsException("INSERT INTO is missing column list.");
    } else if (!matches[3].matched) {
        throw MissingArgumentsException("INSERT INTO is missing values list.");
    }
    // quotes check
    if (!regex_match(values_list, insert_into_values_with_quotes_regex)) {
        throw SyntaxException("Values must be surrounded by quotes.");
    }
    if (!regex_match(table_name_for_errors, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in table name.");
    } else if (!regex_match(column_list, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in column list.");
    } else if (!regex_match(values_list, insert_into_values_syntax_regex)) {
        if (!regex_match(values_list, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
            throw SyntaxException("Mismatched or mixed quotes in values list.");
        }
    }
    //brackets check
    if (!regex_match(column_list_with_brackets, SyntaxRegexPatterns::PAIRED_BRACKETS_REGEX)) {
        throw SyntaxException("Mismatched parentheses in column list.");
    } else if (!regex_match(values_list_with_brackets, SyntaxRegexPatterns::PAIRED_BRACKETS_REGEX)) {
        throw SyntaxException("Mismatched parentheses in values list.");
    } else if (!regex_match(column_list_with_brackets, SyntaxRegexPatterns::MUST_CONTAIN_PARENTHESES)) {
        throw SyntaxException("Missing parentheses in column list.");
    } else if (!regex_match(values_list_with_brackets, SyntaxRegexPatterns::MUST_CONTAIN_PARENTHESES)) {
        throw SyntaxException("Missing parentheses in values list.");
    }

    if (matches[2].matched && !regex_match(column_list, SyntaxRegexPatterns::COLUMNS_SYNTAX_REGEX)) {
        throw SyntaxException("Invalid or improperly formatted column list in INSERT INTO statement.");
    }

}
