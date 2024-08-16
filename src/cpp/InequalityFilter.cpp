
#include "InequalityFilter.h"

InequalityFilter::InequalityFilter(std::string columnName, std::string value) : columnName(std::move(columnName)), value(std::move(value)) {}

bool InequalityFilter::applyFilter(const Row &row) const {
    try {
        return row.getColumnValue(columnName) != value;
    } catch (const ColumnDoesNotExistException& e) {
        return false;
    }
}

string InequalityFilter::toString() const {
    return columnName + " != " + value;
}
