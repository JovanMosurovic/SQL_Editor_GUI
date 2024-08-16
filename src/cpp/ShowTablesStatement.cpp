
#include "ShowTablesStatement.h"

ShowTablesStatement::ShowTablesStatement(const string &query) : Statement(query) {

}

void ShowTablesStatement::execute(Database &db) {
    db.printDatabase();
}

bool ShowTablesStatement::parse() {
    return true;
}

void ShowTablesStatement::errors() {
    regex show_tables_regex(R"(^\s*SHOW\s+TABLES\s*$)", regex_constants::icase);
    if (!regex_match(query, show_tables_regex)) {
        throw SyntaxException("Invalid syntax for SHOW TABLES statement.");
    }
}