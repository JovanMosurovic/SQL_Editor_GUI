
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_INSERTINTOSTATEMENT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_INSERTINTOSTATEMENT_H

#include "Statement.h"

class InsertIntoStatement : public Statement {
    string table_name;
    vector<string> column_names;
    vector<string> values;

public:
    InsertIntoStatement(const string &query);

    bool parse() override;
    void execute(Database &db) override;
    void errors() override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_INSERTINTOSTATEMENT_H
