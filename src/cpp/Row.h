
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_ROW_H
#define ELEMENTAL_SQL_IMPLEMENTATION_ROW_H

#include <vector>
#include <string>
#include <algorithm>
#include "Column.h"
#include "TableExceptions.h"

using namespace std;

class Row {
    vector<string> columnNames;
    vector<string> data;

public:
    Row(const vector<string> &columnNames, const vector<string> &data);

    void updateColumnValue(const string &columnName, const string &value);

    const vector<string> &getData() const;

    string getColumnValue(const string& columnName) const;

    void setData(const vector<string> &data_);

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_ROW_H
