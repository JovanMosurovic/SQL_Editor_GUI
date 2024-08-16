
#include "Row.h"

Row::Row(const vector<string> &columnNames, const vector<string> &data) : columnNames(columnNames), data(data) {}

void Row::updateColumnValue(const string& columnName, const string& newValue) {
    auto it = find(columnNames.begin(), columnNames.end(), columnName);
    if (it == columnNames.end()) {
        throw ColumnDoesNotExistException(columnName);
    }
    data[distance(columnNames.begin(), it)] = newValue;
}

const vector<string> &Row::getData() const {
    return data;
}

string Row::getColumnValue(const string& columnName) const {
    auto it = std::find(columnNames.begin(), columnNames.end(), columnName);
    if (it == columnNames.end()) {
        throw ColumnDoesNotExistException(columnName);
    }
    return data[std::distance(columnNames.begin(), it)];
}


void Row::setData(const vector<string> &data_) {
    Row::data = data_;
}

