
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_TABLE_H
#define ELEMENTAL_SQL_IMPLEMENTATION_TABLE_H

#include <utility>
#include <iomanip>
#include <sstream>
#include <regex>
#include <fstream>
#include "Column.h"
#include "Row.h"
#include "Colors.h"
#include "ConsoleUtils.h"
#include "TableExceptions.h"

using namespace std;

class Table {

    string name;
    vector<Column> columns;
    vector<Row> rows;

public:
    Table(const string& name, const vector<Column> &columns);
    Table(Table&& other) noexcept;
    // copy constructor
    Table(const Table& other);

    void addRow(const vector<string>& rowData);

    void removeRow(long long rowIndex);
    vector<Row>::iterator removeRow(vector<Row>::iterator rowIt);

    void clearRows();

    void updateRow(long long rowIndex, const vector<string>& newData);

    static shared_ptr<Table> mergeTwoTables(const Table& t1, const Table& t2);

    bool hasColumn(const string& columnName) const;

    void printTable() const;

//    void printTableInFile(ostream &os, const ) const {
//        os << "\t" << name << endl;
//        for (const auto &column: columns) {
//            os << column.getName() << "~";
//        }
//        os << endl;
//
//        for (const auto &row: rows) {
//            for (const auto &data: row.getData()) {
//                os << data << "~";
//            }
//            os << endl;
//        }
//    }

    void printTableInFile(ostream &os, const vector<string>& selectedColumns = {}) const {
        os << "\t" << name << endl;

        if (selectedColumns.empty()) {
            for (const auto &column: columns) {
                os << column.getName() << "~";
            }
            os << endl;

            for (const auto &row: rows) {
                for (const auto &data: row.getData()) {
                    os << data << "~";
                }
                os << endl;
            }
        } else {
            for (const auto &columnName: selectedColumns) {
                os << columnName << "~";
            }
            os << endl;

            for (const auto &row: rows) {
                for (const auto &columnName: selectedColumns) {
                    int columnIndex = getColumnIndex(columnName);
                    os << row.getData()[columnIndex] << "~";
                }
                os << endl;
            }
        }
    }

    //<editor-fold desc="Getters">

    const vector<Column> &getColumns() const;

    int getColumnIndex(const string& columnName) const;

    const string &getName() const;

    const vector<Row> &getRows() const;
    vector<Row> &getRows();

    shared_ptr<Table> getTableAsColumn(const string &columnName) const;

    //</editor-fold>

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_TABLE_H
