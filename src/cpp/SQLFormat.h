
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_SQLFORMAT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_SQLFORMAT_H

#include "Format.h"

class SQLFormat : public Format {
public:

    string formatTable(const Table &table) const override;
    string formatRow(const Table& table, const Row &row) const override;

    string parseDatabaseName(const string& line) const override;
    Table parseTable(const string& line) const override;
    vector<string> parseRow(const string &line) const override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_SQLFORMAT_H
