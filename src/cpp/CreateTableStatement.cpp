
#include "CreateTableStatement.h"

CreateTableStatement::CreateTableStatement(const string &query) : Statement(query) {}

bool CreateTableStatement::parse() {
    errors();
    regex createTableRegex(R"(CREATE\s+TABLE\s+(['"`]?\w+['"`]?)\s*\(((?:\s*(?:\w+|['"`][^'"`]+['"`])\s*,?\s*)+)\))", regex_constants::icase);
    smatch matches;
    if (regex_search(query, matches, createTableRegex) && matches.size() == 3) {

        tableName = matches[1].str();
        if (tableName.front() == '\'' || tableName.front() == '\"' || tableName.front() == '`') {
            tableName = tableName.substr(1, tableName.size() - 2);
        }

        string columns = matches[2].str();
        regex columnRegex(R"(\w+|['"`]([^'"`]+)['"`])");
        sregex_iterator it(columns.begin(), columns.end(), columnRegex);
        sregex_iterator end;
        while (it != end) {
            string column = it->str();
            if(column.front() == '\'' || column.front() == '\"' || column.front() == '`') {
                column = column.substr(1, column.size() - 2);
            }
            columnNames.push_back(column);
            it++;
        }
        return true;
    }
    return false;
}


void CreateTableStatement::execute(Database& db) {
    if (!parse()) {
        return;
    }
    // converting column names to Column objects
    vector<Column> columns;
    columns.reserve(columnNames.size());
    for (const auto& columnName : columnNames) {
        columns.emplace_back(columnName);
    }
    db.createTable(tableName, columns);

    // Insert into file for native format
    ofstream outFile("output.txt", ios::out);
    db.getTable(tableName).printTableInFile(outFile);
    outFile.close();
}

void CreateTableStatement::errors() {
    regex create_table_basic_pattern(R"(^\s*CREATE\s+TABLE\s+([^(\s]+)\s*(\(([^)]+)\))?\s*$)", regex_constants::icase);
    smatch matches;
    regex_match(query, matches, create_table_basic_pattern);
    string table_name = matches[1].str();
    string column_definitions_with_brackets = matches[2].str();
    string column_definitions = matches[3].str();

    if (!matches[1].matched && !matches[2].matched) {
        throw MissingArgumentsException("CREATE TABLE is missing table name and column definitions.");
    } else if (!matches[1].matched) {
        throw MissingArgumentsException("CREATE TABLE is missing table name.");
    } else if (!matches[2].matched) {
        throw MissingArgumentsException("CREATE TABLE is missing column definitions.");
    }
    // quotes check
    if (!regex_match(table_name, SyntaxRegexPatterns::VALID_QUOTE_REGEX) && !regex_match(column_definitions, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in table name and column definitions.");
    } else if (!regex_match(table_name, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in table name.");
    } else if (!regex_match(column_definitions, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in column definitions.");
    }
    // brackets check
    if (!regex_match(column_definitions_with_brackets, SyntaxRegexPatterns::PAIRED_BRACKETS_REGEX) ||
        !regex_match(column_definitions_with_brackets, SyntaxRegexPatterns::MUST_CONTAIN_PARENTHESES)) {
        throw SyntaxException("Mismatched parentheses in column definitions.");
    }
    // invalid column definitions
    if (!regex_match(column_definitions, SyntaxRegexPatterns::COLUMNS_SYNTAX_REGEX)) {
        throw InvalidArgumentsException(
                "Invalid or improperly formatted column definitions in CREATE TABLE statement.");
    }
}

