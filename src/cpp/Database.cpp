
#include "Database.h"

Database::Database(const string& name) : name(name) {}

Database::Database(const Database &other) {
    name = other.name;
    tables = other.tables;
}


Database::Database(Database &&other) noexcept {
    name = std::move(other.name);
    tables = std::move(other.tables);
}

Database &Database::operator=(const Database &other) {
    if (this == &other) {
        return *this;
    }
    name = other.name;
    tables = other.tables;
    return *this;
}

void Database::addTable(const Table &table) {
    if (tables.find(table.getName()) != tables.end()) {
        throw TableAlreadyExistsException(table.getName());
    }
    tables.emplace(table.getName(), table);
}

void Database::createTable(const string &tableName, const vector<Column> &columns) {
    regex tableName_pattern("^[A-Za-z_]+$");
    if (!(regex_match(tableName, tableName_pattern))) {
        throw InvalidTableNameException(tableName);
    }
    auto it = tables.find(tableName);
    if (it != tables.end()) {
        throw TableAlreadyExistsException(tableName);
    }
    tables.emplace(tableName, Table(tableName, columns));
}

void Database::dropTable(const string &tableName) {
    auto it = tables.find(tableName);
    if (it == tables.end()) {
        throw TableDoesNotExistException(tableName);
    }
    tables.erase(it);
}

void Database::addRowToTable(const string &tableName, const vector<string> &rowData) {
    Table &table = getTable(tableName);
    if(rowData.size() != table.getColumns().size()) {
        throw InvalidDataForAddRowException(rowData.size(), table.getColumns().size());
    }
    table.addRow(rowData);
}

void Database::insertIntoTable(const string &tableName, const vector<string> &columnNames, const vector<string> &rowData) {
    Table &table = getTable(tableName);
    if(columnNames.size() != rowData.size()) {
        throw InvalidDataForAddRowException(rowData.size(), columnNames.size());
    }

    vector<string> actualColumnOrder;
    for (const auto &column : table.getColumns()) {
        actualColumnOrder.push_back(column.getName());
    }

    vector<string> reorderedData(actualColumnOrder.size());
    for(size_t i = 0; i< columnNames.size(); i++) {
        auto pos = find(actualColumnOrder.begin(), actualColumnOrder.end(), columnNames[i]);
        if(pos != actualColumnOrder.end()) {
            int index = distance(actualColumnOrder.begin(), pos);
            reorderedData[index] = rowData[i];
        } else {
            throw ColumnDoesNotExistException(columnNames[i]);
        }
    }
    table.addRow(reorderedData);
}

void Database::updateRowInTable(const string &tableName, const long long rowIndex, const vector<string> &rowData) {
    Table &table = getTable(tableName);
    if(rowIndex >= table.getRows().size()) {
        throw RowOutOfBoundsException(rowIndex, table.getRows().size());
    }
    if(rowData.size() != table.getColumns().size()) {
        throw InvalidDataForUpdateException(rowData.size(), table.getColumns().size());
    }
    table.updateRow(rowIndex, rowData);
}

void Database::removeRowFromTable(const string &tableName, const long long rowIndex) {
    Table &table = getTable(tableName);
    if(rowIndex >= table.getRows().size()) {
        throw RowOutOfBoundsException(rowIndex, table.getRows().size());
    }
    table.removeRow(rowIndex);
}

void Database::clearTable(const string &tableName) {
    Table &table = getTable(tableName);
    table.clearRows();
}

shared_ptr<Table> Database::selectFromTable(const string &tableName, const string &tableAlias, const vector<string> &columnNames, const vector<shared_ptr<IFilter>>& filters) {
    Table &table = getTable(tableName);

    for(const auto &columnName : columnNames) {
        if(!table.hasColumn(columnName)) {
            throw ColumnDoesNotExistException(columnName);
        }
    }
    vector<Column> selectedColumns;
    for(const auto &columnName : columnNames) {
        int columnIndex = table.getColumnIndex(columnName);
        selectedColumns.push_back(table.getColumns()[columnIndex]);
    }
    auto selectedTable = make_shared<Table>(tableAlias, selectedColumns);

    for (const auto &row : table.getRows()) {
        bool shouldAddRow = true;
        for (const auto &filter : filters) {
            if (filter && !filter->applyFilter(row)) {
                shouldAddRow = false;
                break;
            }
        }
        if (shouldAddRow) {
            vector<string> selectedRow;
            selectedRow.reserve(columnNames.size());
            for (const auto &columnName : columnNames) {
                selectedRow.push_back(row.getData()[table.getColumnIndex(columnName)]);
            }
            selectedTable->addRow(selectedRow);
        }
    }

    if (selectedTable->getRows().empty()) {
        cout << "\xC4> Query did not return any results." << endl;
    } else {
        selectedTable->printTable();
    }

    return selectedTable;
}

shared_ptr<Table> Database::innerJoinTables(const string &table1Name, const string &table2Name, const string &column1,
                                            const string &column2) {
    Table &table1 = getTable(table1Name);
    Table &table2 = getTable(table2Name);

    if (!table1.hasColumn(column1) || !table2.hasColumn(column2)) {
        throw ColumnDoesNotExistException("Column does not exist in one of the tables.");
    }

    vector<Column> newColumns = table1.getColumns();
    newColumns.insert(newColumns.end(), table2.getColumns().begin(), table2.getColumns().end());

    Table resultTable("Result", newColumns);

    for (const auto &row1 : table1.getRows()) {
        for (const auto &row2 : table2.getRows()) {
            if (row1.getColumnValue(column1) == row2.getColumnValue(column2)) {
                vector<string> rowData = row1.getData();
                rowData.insert(rowData.end(), row2.getData().begin(), row2.getData().end());
                resultTable.addRow(rowData);
            }
        }
    }

    return make_shared<Table>(resultTable);
}

void Database::importDatabase(const Format& format, const string& filePath) {
    ifstream file(filePath);
    if (!file.is_open()) {
        throw FileNotOpenedException(filePath);
    }

    string line;
    unique_ptr<Table> currentTable = nullptr;
    bool isTableInitialized = false;
    bool isSQLFormat = filePath.substr(filePath.find_last_of('.') + 1) == "sql";
    bool expectingColumnHeaders = false;  // Only used for custom format

    auto trim = [](string& s) {
        s.erase(s.begin(), find_if(s.begin(), s.end(), [](unsigned char ch) { return !isspace(ch); }));
        s.erase(find_if(s.rbegin(), s.rend(), [](unsigned char ch) { return !isspace(ch); }).base(), s.end());
    };

    while (getline(file, line)) {
        trim(line);
        if (line.empty()) continue;

        if (isSQLFormat) {
            if (line.substr(0, 13) == "CREATE TABLE ") {
                if (currentTable) {
                    tables.emplace(currentTable->getName(), std::move(*currentTable));
                }
                Table newTable = format.parseTable(line);
                currentTable = make_unique<Table>(newTable.getName(), newTable.getColumns());
                isTableInitialized = true;
            } else if (line.substr(0, 11) == "INSERT INTO" && isTableInitialized) {
                vector<string> rowData = format.parseRow(line);
                currentTable->addRow(rowData);
            }
        } else {  // Custom format
            if (line.substr(0, 6) == "Table:") {
                if (currentTable) {
                    tables.emplace(currentTable->getName(), std::move(*currentTable));
                }
                string tableName = line.substr(6).substr(0, line.find('(') - 6);
                trim(tableName);
                currentTable = make_unique<Table>(tableName, vector<Column>());
                expectingColumnHeaders = true;
            } else if (expectingColumnHeaders) {
                vector<string> columnNames = format.parseRow(line);
                vector<Column> columns;
                columns.reserve(columnNames.size());
                for (const auto& columnName : columnNames) {
                    columns.emplace_back(columnName);
                }
                currentTable = make_unique<Table>(currentTable->getName(), columns);
                expectingColumnHeaders = false;
                isTableInitialized = true;
            } else if (isTableInitialized) {
                vector<string> rowData = format.parseRow(line);
                currentTable->addRow(rowData);
            }
        }
    }

    if (currentTable) {
        tables.emplace(currentTable->getName(), std::move(*currentTable));
    }

    file.close();
}

void Database::exportDatabase(const Format& format, const string& filePath) {
    ofstream file(filePath);
    if (!file.is_open()) {
        throw FileNotOpenedException(filePath);
    }

    if (const auto* customFormat = dynamic_cast<const CustomFormat*>(&format)) {
        file << customFormat->formatDatabaseName(name) << endl;
    }

    for (const auto& pair : tables) {
        const Table& table = pair.second;
        file << format.formatTable(table) << endl;
        for (const auto& row : table.getRows()) {
            file << format.formatRow(table, row) << endl;
        }
    }
    file.close();
}

void Database::printDatabase() {
    // Insert into file for native format
    ofstream outFile("output.txt", ios::out);
    cout << endl << "Database: " << name << endl;
    outFile << "Database name: " << name << endl;
    cout << "Tables: " << endl;
    if(tables.empty()) {
        cout << " - No tables in database" << endl;
    }
    for(auto it = tables.begin(); it != tables.end(); ++it) {
        cout << " - " << it->first << endl;
        it->second.printTableInFile(outFile);
        outFile << "#" << endl;
        it->second.printTable();
        if (next(it) != tables.end()) {
            cout << endl;
        }
    }
    outFile.close();
}

const string &Database::getName() const {
    return name;
}

Table &Database::getTable(const string &tableName) {
    auto it = tables.find(tableName);
    if (it == tables.end()) {
        throw TableDoesNotExistException(tableName);
    }
    return it->second;
}



