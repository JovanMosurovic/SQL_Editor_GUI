
#include "DropTableStatement.h"

DropTableStatement::DropTableStatement(const string &query) : Statement(query) {}

bool DropTableStatement::parse() {
    errors();
    regex dropTableRegex(R"(^\s*DROP\s+TABLE(?:\s+(\S+))?\s*$)", regex_constants::icase);
    smatch matches;
    if(!regex_search(query, matches, dropTableRegex) && matches.size() == 2) {
        return false;
    }
    table_name = matches[1];
    if (table_name.front() == '\'' || table_name.front() == '\"' || table_name.front() == '`') {
        table_name = table_name.substr(1, table_name.size() - 2);
    }
    return true;
}

void DropTableStatement::execute(Database &db) {
    if(!parse()) {
        return;
    }
    db.dropTable(table_name);
}

void DropTableStatement::errors() {
    regex drop_table_regex(R"(^\s*DROP\s+TABLE(?:\s+(\S+))?\s*$)", regex_constants::icase);
    smatch matches;
    regex_match(query, matches, drop_table_regex);

    string table_name_for_errors = matches[1].str();
    if (!matches[1].matched) {
        throw MissingArgumentsException("DROP TABLE is missing table name.");
    }
    if (!regex_match(table_name_for_errors, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in table name.");
    }
}
