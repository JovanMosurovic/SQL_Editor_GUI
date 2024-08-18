
#include "SQLFormat.h"

string SQLFormat::parseDatabaseName(const string &line) const {
    return "";
}

string SQLFormat::formatTable(const Table& table) const {
    stringstream sql;
    sql << "CREATE TABLE " << table.getName() << " (";
    const auto& columns = table.getColumns();
    for (size_t i = 0; i < columns.size(); i++) {
        sql << columns[i].getName();
        if (i < columns.size() - 1) {
            sql << ", ";
        }
    }
    sql << ");";
    return sql.str();
}

string SQLFormat::formatRow(const Table& table, const Row& row) const {
    std::stringstream sql;
    sql << "INSERT INTO " << table.getName() << " (";
    const auto& columns = table.getColumns();
    for (size_t i = 0; i < columns.size(); ++i) {
        sql << columns[i].getName();
        if (i < columns.size() - 1) sql << ", ";
    }
    sql << ") VALUES (";
    const auto& data = row.getData();
    for (size_t i = 0; i < data.size(); ++i) {
        sql << "\"" << data[i] << "\"";
        if (i < data.size() - 1) sql << ", ";
    }
    sql << ");";
    return sql.str();
}

vector<string> SQLFormat::parseRow(const string &line) const {
    // Improved regex pattern to capture table name and values correctly
    regex rowPattern(R"(^\s*INSERT\s+INTO\s+(\S+)\s*\(([^)]+)\)\s*VALUES\s*\(([^)]+)\)\s*;?$)", regex_constants::icase);
    smatch matches;

    if (regex_match(line, matches, rowPattern)) {
        string valuesStr = matches[3];
        stringstream ss(valuesStr);
        string value;
        vector<string> values;

        while (getline(ss, value, ',')) {
            value.erase(0, value.find_first_not_of(" \t\"'"));
            value.erase(value.find_last_not_of(" \t\"'") + 1);
            values.push_back(value);
        }
        return values;
    } else {
        throw InvalidFormatException("Invalid row format in SQL file: " + line);
    }
}


Table SQLFormat::parseTable(const string &line) const {
    regex tablePattern(R"(CREATE TABLE (\w+)\s*\((.*)\)\s*;?\s*$)");
    smatch matches;
    if (regex_match(line, matches, tablePattern)) {
        string tableName = matches[1];
        string columnsStr = matches[2];
        istringstream ss(columnsStr);
        vector<Column> columns;

        string columnDef;
        while (getline(ss, columnDef, ',')) {
            // Uklanjanje vodećih i pratećih razmaka
            columnDef = regex_replace(columnDef, regex("^\\s+|\\s+$"), "");

            // Izdvajanje imena kolone (prvi deo pre razmaka)
            istringstream columnStream(columnDef);
            string columnName;
            columnStream >> columnName;

            columns.emplace_back(columnName);
        }

        return {tableName, columns};
    } else {
        throw InvalidFormatException("Invalid table format in SQL file: " + line);
    }
}
