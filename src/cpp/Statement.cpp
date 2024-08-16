
#include "Statement.h"

#include <utility>

Statement::Statement(string query) : query(std::move(query)) {}

Statement::~Statement() {}
