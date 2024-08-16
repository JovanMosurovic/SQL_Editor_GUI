
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_UPDATESTATEMENT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_UPDATESTATEMENT_H

#include "Statement.h"

class UpdateStatement : public Statement {
    string table_name;
    map<string, string> changes;
    vector<shared_ptr<IFilter>> filters;


public:
    UpdateStatement(const string &query);

    bool parse() override;
    void execute(Database &db) override;
    void errors() override;

private:
    void parseSetClause(const string &setClause);
    void parseWhereClause(const string &whereClause);

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_UPDATESTATEMENT_H
