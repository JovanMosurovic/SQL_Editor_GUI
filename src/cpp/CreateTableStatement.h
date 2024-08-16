
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_CREATETABLESTATEMENT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_CREATETABLESTATEMENT_H

#include "Statement.h"

class CreateTableStatement : public Statement {
    string tableName;
    vector<string> columnNames;

public:
    CreateTableStatement(const string &query);

    bool parse() override;
    void execute(Database &db) override;
    void errors() override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_CREATETABLESTATEMENT_H
