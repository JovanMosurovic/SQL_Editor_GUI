
#include "CustomFormat.h"

string CustomFormat::formatDatabaseName(const string &databaseName) const {
    return "Database: " + databaseName + "\n";
}

string CustomFormat::formatTable(const Table &table) const {
    stringstream customFormat;
    customFormat << "Table: " << table.getName() << " (Rows: " << table.getRows().size() << ")\n";
    const auto& columns = table.getColumns();
    for (size_t i = 0; i < columns.size(); i++) {
        customFormat << columns[i].getName();
        if (i < columns.size() - 1) {
            customFormat << " | ";
        }
    }
    customFormat << "\n";
    return customFormat.str();
}

string CustomFormat::formatRow(const Table& table, const Row &row) const {
    stringstream customFormat;
    const auto& data = row.getData();
    for (size_t i = 0; i < data.size(); ++i) {
        customFormat << data[i];
        if (i < data.size() - 1) customFormat << " | ";
    }
    customFormat << "\n";
    return customFormat.str();
}

string CustomFormat::parseDatabaseName(const string &line) const {
    // Skipping "Database: "
    return line.substr(10);
}

Table CustomFormat::parseTable(const string &line) const {
    // Skipping "Table: "
    string tableName = line.substr(7);
    return {tableName, vector<Column>()};
}

vector<string> CustomFormat::parseRow(const string &line) const {
    // Splitting the line by " | "
    vector<string> values;
    stringstream ss(line);
    string value;
    while (getline(ss, value, '|')) {
        // Removing leading and trailing spaces
        value.erase(0, value.find_first_not_of(' '));
        value.erase(value.find_last_not_of(' ') + 1);
        values.push_back(value);
    }
    return values;
}
