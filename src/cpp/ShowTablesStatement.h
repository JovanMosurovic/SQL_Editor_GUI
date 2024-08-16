
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_SHOWTABLESSTATEMENT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_SHOWTABLESSTATEMENT_H

#include "Statement.h"

class ShowTablesStatement : public Statement {

public:
    ShowTablesStatement(const string &query);

    void execute(Database &db) override;
    bool parse() override;
    void errors() override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_SHOWTABLESSTATEMENT_H
