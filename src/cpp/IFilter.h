
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_IFILTER_H
#define ELEMENTAL_SQL_IMPLEMENTATION_IFILTER_H

#include <vector>
#include <string>
#include <memory>
#include "Row.h"

using namespace std;

class IFilter {

public:
    virtual ~IFilter() = default;

    virtual bool applyFilter(const Row &row) const = 0;

    virtual string toString() const = 0;
};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_IFILTER_H
