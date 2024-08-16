
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_CUSTOMFORMAT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_CUSTOMFORMAT_H

#include "Format.h"

class CustomFormat : public Format {

public:

    string formatDatabaseName(const string& databaseName) const;
    string formatTable(const Table &table) const override;
    string formatRow(const Table& table, const Row &row) const override;

    string parseDatabaseName(const string& line) const;

    Table parseTable(const string &line) const override;

    vector<string> parseRow(const string &line) const override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_CUSTOMFORMAT_H
