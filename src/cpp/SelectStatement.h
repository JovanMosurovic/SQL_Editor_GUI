
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_SELECTSTATEMENT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_SELECTSTATEMENT_H

#include <string>
#include <vector>
#include "Statement.h"


using namespace std;

class SelectStatement : public Statement {
    string table_name;
    string table_alias;
    string join_table_name;
    string join_table_alias;
    string join_column;
    string join_column2;
    vector<string> column_names;
    map<string, string> aliasMap;
    vector<shared_ptr<IFilter>> filters;

public:
    SelectStatement(const string &query);

    bool parse() override;
    void parseWhereClause(const string &whereClause);
    void execute(Database &db) override;
    void errors() override;


};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_SELECTSTATEMENT_H
