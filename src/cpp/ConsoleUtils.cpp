#include <iostream>
#include "ConsoleUtils.h"

namespace ConsoleUtils {
    void printLine(const std::vector<int>& columnWidths, char left, char middle, char right, std::string color) {
        std::cout << color << left;
        for (int i = 0; i < columnWidths.size(); ++i) {
            for (int j = 0; j < columnWidths[i]; ++j) {
                std::cout << "\xC4";
            }
            if (i < columnWidths.size() - 1) {
                std::cout << middle;
            }
        }
        std::cout << right << std::endl;
        std::cout << resetColor;
    }

    void printRow(const std::vector<std::string>& rowData, const std::vector<int>& columnWidths, TextAlignment align, std::string color) {
        std::cout << color;
        auto alignText = [align](const std::string& text, int width) {
            int leftPadding, rightPadding;
            switch (align) {
                case TextAlignment::LEFT:
                    leftPadding = 1;
                    rightPadding = width - text.size() - leftPadding;
                    break;
                case TextAlignment::RIGHT:
                    rightPadding = 1;
                    leftPadding = width - text.size() - rightPadding;
                    break;
                case TextAlignment::CENTER:
                default:
                    leftPadding = (width - text.size()) / 2;
                    rightPadding = width - text.size() - leftPadding;
                    break;
            }
            return std::string(leftPadding, ' ') + text + std::string(rightPadding, ' ');
        };

        for (int i = 0; i < rowData.size(); ++i) {
            std::cout << "\xB3" << alignText(rowData[i], columnWidths[i]);
        }
        std::cout << "\xB3" << std::endl;
        std::cout << resetColor;
    }

    std::vector<int> calculateColumnWidths(const std::vector<std::string>& columns, const std::vector<std::vector<std::string>>& rows) {
        std::vector<int> columnWidths;
        for (const auto& column : columns) {
            int maxWidth = column.length();
            int columnIndex = &column - &columns[0];
            for (const auto& row : rows) {
                int cellWidth = 0;
                if (columnIndex < row.size()) {
                    const std::string& cellValue = row[columnIndex];
                    cellWidth = cellValue.empty() ? 0 : cellValue.length();
                }
                maxWidth = std::max(maxWidth, cellWidth);
            }
            columnWidths.push_back(maxWidth + 2);
        }
        return columnWidths;
    }
}