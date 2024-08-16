
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_COLUMN_H
#define ELEMENTAL_SQL_IMPLEMENTATION_COLUMN_H

#include <string>

using namespace std;

class Column {
    string name;

public:
    Column(string name);

    const string &getName() const;
};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_COLUMN_H
