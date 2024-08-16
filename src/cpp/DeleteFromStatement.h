
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_DELETEFROMSTATEMENT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_DELETEFROMSTATEMENT_H

#include "Statement.h"

class DeleteFromStatement : public Statement {
    string table_name;
    string where_condition;
    vector<shared_ptr<IFilter>> filters;

public:
    DeleteFromStatement(const string &query);

    bool parse() override;
    void parseWhereClause(const string &whereClause);
    void execute(Database &db) override;
    void errors() override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_DELETEFROMSTATEMENT_H
