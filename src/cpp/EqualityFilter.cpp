
#include "EqualityFilter.h"

EqualityFilter::EqualityFilter(std::string columnName, std::string value) : columnName(std::move(columnName)), value(std::move(value)) {}

bool EqualityFilter::applyFilter(const Row &row) const {
    try {
        return row.getColumnValue(columnName) == value;
    } catch (const ColumnDoesNotExistException& e) {
        return false;
    }
}

string EqualityFilter::toString() const {
    return columnName + " = " + value;
}