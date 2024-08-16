
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_EQUALITYFILTER_H
#define ELEMENTAL_SQL_IMPLEMENTATION_EQUALITYFILTER_H

#include "IFilter.h"

class EqualityFilter : public IFilter {
    string columnName;
    string value;

public:
    EqualityFilter(std::string columnName, std::string value);

    bool applyFilter(const Row& row) const override;

    string toString() const override;

};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_EQUALITYFILTER_H
