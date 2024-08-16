#ifndef ELEMENTAL_SQL_IMPLEMENTATION_COLORS_H
#define ELEMENTAL_SQL_IMPLEMENTATION_COLORS_H

#include <string>

// Text styles
const std::string resetColor = "\033[0m"; // Reset color
const std::string underline = "\033[4m"; // Underline
const std::string bold = "\033[1m"; // Bold

// Standard colors
const std::string red = "\033[1;31m"; // Red
const std::string green = "\033[1;32m"; // Green
const std::string darkGreen = "\033[0;32m"; // Dark green
const std::string yellow = "\033[1;33m"; // Yellow
const std::string blue = "\033[1;34m"; // Blue
const std::string lightBlue = "\033[1;94m"; // Light blue
const std::string lightGreen = "\033[1;92m"; // Light green
const std::string magenta = "\033[1;35m"; // Magenta
const std::string cyan = "\033[1;36m"; // Cyan
const std::string white = "\033[1;37m"; // White
const std::string gray = "\033[1;90m"; // Gray


// Background colors
const std::string bgBlack = "\033[1;40m"; // Black background
const std::string bgRed = "\033[1;41m"; // Red background
const std::string bgGreen = "\033[1;42m"; // Green background
const std::string bgDarkGreen = "\033[1;32m"; // Dark green background
const std::string bgYellow = "\033[1;43m"; // Yellow background
const std::string bgBlue = "\033[1;44m"; // Blue background
const std::string bgMagenta = "\033[1;45m"; // Magenta background
const std::string bgGray = "\033[1;100m"; // Gray background
const std::string bgCyan = "\033[1;46m"; // Cyan background
const std::string bgWhite = "\033[1;47m"; // White background

#endif //ELEMENTAL_SQL_IMPLEMENTATION_COLORS_H
