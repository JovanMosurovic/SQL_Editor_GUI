
#include "SyntaxRegexPatterns.h"

void SyntaxRegexPatterns::checkWhereClauseErrors(const string &where_clause) {

    if (where_clause.empty()) {
        throw MissingArgumentsException("WHERE clause requires conditions. None provided.");
    }

    if (!regex_match(where_clause, SyntaxRegexPatterns::VALID_QUOTE_REGEX)) {
        throw SyntaxException("Mismatched or mixed quotes in WHERE clause.");
    }

    if(!regex_match(where_clause, SyntaxRegexPatterns::MUST_CONTAIN_QUOTES)) {
        throw SyntaxException("Values in WHERE clause must be surrounded by quotes.");
    }

    regex ends_with_operator_regex(R"(.*(\s+AND\s*|\s+OR\s*)$)", regex_constants::icase);
    if (regex_match(where_clause, ends_with_operator_regex)) {
        throw SyntaxException("WHERE clause ends with an operator. It should end with a condition.");
    }

    if (!regex_match(where_clause, SyntaxRegexPatterns::WHERE_CLAUSE_SYNTAX_REGEX)) {
        throw SyntaxException("Invalid or improperly formatted WHERE clause.");
    }

}