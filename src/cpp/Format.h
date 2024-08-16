
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_FORMAT_H
#define ELEMENTAL_SQL_IMPLEMENTATION_FORMAT_H

#include <string>
#include "Table.h"
#include "DatabaseExceptions.h"

class Format {
public:
    virtual ~Format() = default;

    virtual string formatTable(const Table& table) const = 0;
    virtual string formatRow(const Table& table, const Row& row) const = 0;

    virtual string parseDatabaseName(const string& line) const = 0;
    virtual Table parseTable(const string& line) const = 0;
    virtual vector<string> parseRow(const string& line) const = 0;
};

#endif //ELEMENTAL_SQL_IMPLEMENTATION_FORMAT_H
