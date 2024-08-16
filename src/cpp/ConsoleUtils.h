
#ifndef ELEMENTAL_SQL_IMPLEMENTATION_CONSOLEUTILS_H
#define ELEMENTAL_SQL_IMPLEMENTATION_CONSOLEUTILS_H

#include <vector>
#include <string>
#include "Colors.h"

namespace ConsoleUtils {

    enum class TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    };

    void printLine(const std::vector<int>& columnWidths, char left, char middle, char right, std::string color = resetColor);
    void printRow(const std::vector<std::string>& rowData, const std::vector<int>& columnWidths, TextAlignment allignment = TextAlignment::CENTER, std::string color = resetColor);
    std::vector<int> calculateColumnWidths(const std::vector<std::string>& columns, const std::vector<std::vector<std::string>>& rows);
};


#endif //ELEMENTAL_SQL_IMPLEMENTATION_CONSOLEUTILS_H
