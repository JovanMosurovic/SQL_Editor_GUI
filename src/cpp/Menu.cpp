#include "Menu.h"

using namespace std;

void Menu::importDatabaseMenu() {
    string choice;
    do {
        ConsoleUtils::printLine({40}, '\xDA', '\xC4', '\xBF');
        ConsoleUtils::printRow({"MENU"}, {40});
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"1. CREATE DATABASE"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"2. IMPORT DATABASE FROM FILE"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"0. EXIT"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC0', '\xC4', '\xD9');
        cout << "\xB3" << "Enter a number to select your desired option: " << endl << "\xC4>";
        cin >> choice;
        cin.ignore(numeric_limits<streamsize>::max(), '\n');

        if (choice == "1") {
            try {
                cout << "You have selected the option \"CREATE DATABASE\"" << endl;
                string databaseName;
                cout << "Enter the name of the database you want to create: " << endl << "\xC4>";
                cin >> databaseName;
                if (databaseName.empty()) {
                    throw DatabaseNameException(databaseName);
                }
                shared_ptr<Database> database = make_shared<Database>(databaseName);
                cout << "Database \"" << databaseName << "\" has been " << green << "successfully" << resetColor << " created!" << endl;
                mainMenu(*database);
            } catch (const DatabaseNameException &e) {
                cout << e.what() << endl;
            } catch (const exception &e) {
                cout << red << "Unexpected exception caught:\n" << e.what() << resetColor << endl;
            }
            break;
        } else if (choice == "2") {
            cout << "You have selected the option \"IMPORT DATABASE FROM FILE\"" << endl;
            cout << "Enter the file path and name (e.g., C:/exports/mydatabase.sql): " << endl << "\xC4>";
            string file_path;
            cin >> file_path;
            if (file_path[0] == '\"' && file_path[file_path.size() - 1] == '\"') {
                file_path = file_path.substr(1, file_path.size() - 2);
            } else if (file_path[0] == '\'' && file_path[file_path.size() - 1] == '\'') {
                file_path = file_path.substr(1, file_path.size() - 2);
            }

            try {
                shared_ptr<Format> format;
                regex validPathRegex(".*\\.(dbexp|sql)$", regex_constants::icase);
                if (!regex_match(file_path, validPathRegex)) {
                    throw InvalidFileImportException("Invalid file path for importing database. Please provide a valid path with .\033[1mdbexp\033[0m or .\033[1msql\033[0m extension.");
                }
                if (file_path.substr(file_path.size() - 6) == ".dbexp") {
                    format = make_shared<CustomFormat>();
                } else if (file_path.substr(file_path.size() - 4) == ".sql") {
                    format = make_shared<SQLFormat>();
                }

                ifstream file(file_path);
                if (!file.is_open()) {
                    throw FileNotOpenedException(file_path);
                }
                string firstLine;
                getline(file, firstLine);
                file.close();
                Database database(format->parseDatabaseName(firstLine));
                database.importDatabase(*format, file_path);
                cout << green << "Database imported successfully from " << resetColor << "\"" << file_path << "\"" << endl << endl;
                mainMenu(database);
            } catch (const InvalidFileImportException &e) {
                cout << e.what() << endl;
            }
            break;
        } else if (choice == "0") {
            finishProgram();
            break;
        } else {
            cout << "Wrong choice, please enter the numbers 1, 2 or 0 to exit." << endl;
        }

    } while (choice != "0");
}

void Menu::mainMenu(Database &database) {
    string choice;
    do {
        ConsoleUtils::printLine({40}, '\xDA', '\xC4', '\xBF');
        ConsoleUtils::printRow({"MENU"}, {40});
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"1. EXECUTE SQL QUERY"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"2. EXPORT DATABASE"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"0. EXIT"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC0', '\xC4', '\xD9');
        cout << "\xB3" << "Enter a number to select your desired option: " << endl << "\xC4>";
        cin >> choice;

        if (choice == "1") {
            cout << "You have selected the option \"EXECUTE SQL QUERY \"" << endl;
            cleanConsole();
            int currLine = 0;
            vector<pair<string, int>> queries = readSQLQuery();
            try {
                for (const auto &[query, line]: queries) {
                    currLine = line;
                    if (!query.empty()) {
                        shared_ptr<Statement> statement = parseSQLQuery(query);
                        statement->execute(database);
                    }
                }
                cout << endl << bgGray << green << "All queries have been successfully executed!" << resetColor << endl;
                cout << bgGray << "Press ENTER to continue..." << resetColor << endl;
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
                cleanConsole();
            } catch (const exception &e) {
                cout << "\033[1mLine " << currLine << " \033[0m\xC4\033[1m>\033[0m ";
                cout << e.what() << endl;
                cout << bgGray << "Press ENTER to continue..." << resetColor << endl;
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
                cleanConsole();
            }
        } else if (choice == "2") {
            cout << "You have selected the option \"EXPORT DATABASE\"" << endl;
            try {
                exportDatabaseMenu(database);
            } catch (const exception &e) {
                cout << e.what() << endl;
            }
        } else if (choice == "0") {
            finishProgram();
        } else {
            cout << "Wrong choice, please enter the numbers 1, 2 or 0 to exit." << endl;
        }

    } while (choice != "0");
}

void Menu::exportDatabaseMenu(Database &database) {
    string choice;
    do {
        ConsoleUtils::printLine({40}, '\xDA', '\xC4', '\xBF');
        ConsoleUtils::printRow({"MENU"}, {40});
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"1. CUSTOM FORMAT EXPORT"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"2. SQL FORMAT EXPORT"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC3', '\xC5', '\xB4');
        ConsoleUtils::printRow({"0. GO BACK <\xC4"}, {40}, ConsoleUtils::TextAlignment::LEFT);
        ConsoleUtils::printLine({40}, '\xC0', '\xC4', '\xD9');
        cout << "\xB3" << "Enter a number to select your desired option: " << endl << "\xC4>";
        cin >> choice;

        if (choice == "1" || choice == "2") {
            cout << "\xB3" << "Enter file path and name (e.g., C:/exports/mydatabase.sql): " << endl << "\xC4>";
            string file_path;
            cin >> file_path;
            if (file_path[0] == '\"' && file_path[file_path.size() - 1] == '\"') {
                file_path = file_path.substr(1, file_path.size() - 2);
            } else if (file_path[0] == '\'' && file_path[file_path.size() - 1] == '\'') {
                file_path = file_path.substr(1, file_path.size() - 2);
            }
            shared_ptr<Format> format;
            try {
                if (choice == "1") {
                    regex validPathRegex(".*\\.dbexp$");
                    if (!regex_match(file_path, validPathRegex)) {
                        throw InvalidFileExportException(
                                "Invalid file path for exporting database. Please provide a valid path with .\033[1mdbexp\033[0m extension.");
                    }
                    format = make_shared<CustomFormat>();
                } else {
                    regex validPathRegex(".*\\.sql$");
                    if (!regex_match(file_path, validPathRegex)) {
                        throw InvalidFileExportException(
                                "Invalid file path for exporting database. Please provide a valid path with .\033[1msql\033[0m extension.");
                    }
                    format = make_shared<SQLFormat>();
                }

                database.exportDatabase(*format, file_path);
                cout << green << "Database exported successfully to " << resetColor << "\"" << file_path << "\"" << endl
                     << endl;
            } catch (const InvalidFileExportException &e) {
                cout << e.what() << endl;
            }

        } else if (choice == "0") {
            return;
        } else {
            cout << "Wrong choice, please enter the numbers 1, 2 or 0 to exit." << endl;
        }
    } while (choice != "0");
}

vector<pair<string, int>> Menu::readSQLQuery() {
    string query;
    string line;
    vector<pair<string, int>> queries;
    bool firstLine = true;
    int lineCounter = 0;
    bool wasPreviousLineEmpty = false;
    bool hasTextBeenEntered = false;
    int commandStartLine = 0;

    cout << bgGray << "Enter your SQL query. Type \"EXIT\" to exit the console." << resetColor << endl;
    do {
        if (!firstLine) {
            cout << bgGray << lineCounter << "." << resetColor << " ";
        }
        getline(cin, line);
        if (line == "exit" || line == "EXIT") {
            break;
        }
        if (!line.empty()) {
            hasTextBeenEntered = true;
            wasPreviousLineEmpty = false;
            string originalLine = line;
            highlightKeywords(line);
            cout << "\033[A\033[2K";  // Clear the current line
            cout << bgGray << lineCounter << "." << resetColor << " " << line << endl;

            if (query.empty()) {
                commandStartLine = lineCounter;
            }
            query += originalLine + " ";

            if (originalLine.find(';') != string::npos) {
                stringstream ss(query);
                string segment;
                while (getline(ss, segment, ';')) {
                    string trimmedSegment = regex_replace(segment, regex("^\\s+|\\s+$"), "");
                    if (!trimmedSegment.empty()) {
                        queries.emplace_back(trimmedSegment, commandStartLine);
                    }
                }
                query.clear();
            }
        } else {
            if (wasPreviousLineEmpty && hasTextBeenEntered) {
                if (!query.empty()) {
                    queries.emplace_back(regex_replace(query, regex("^\\s+|\\s+$"), ""), commandStartLine);
                    query.clear();
                }
                break;
            }
            wasPreviousLineEmpty = true;
        }
        lineCounter++;
        firstLine = false;
    } while (true);

    if (!query.empty()) {  // Handle any remaining query part after the last non-empty line
        queries.emplace_back(regex_replace(query, regex("^\\s+|\\s+$"), ""), commandStartLine);
    }
    return queries;
}

shared_ptr<Statement> Menu::parseSQLQuery(const string &query) {
    regex create_table_basic_pattern(R"(^\s*CREATE\s+TABLE(?:\s+([^(\s]*)\s*(\([^)]*\)?)?)?\s*$)",regex_constants::icase);
    regex drop_table_regex(R"(^\s*DROP\s+TABLE(?:\s+(\S+))?\s*$)", regex_constants::icase);
    regex insert_into_regex(R"(^\s*INSERT\s+INTO\s+(\S+)?\s*(\(?([^)]+)\)?)?\s*VALUES\s*(\(?\s*([^)]*)\)?)?\s*$)",regex_constants::icase);
    // regex select_regex(R"(^\s*SELECT\s+(.*?)\s+FROM\s+(\S+)(?:\s+(\S+))?\s*(?:WHERE\s+(.+))?\s*$)", regex_constants::icase);
     regex select_regex(R"(^\s*SELECT\s*(.*?)?\s*(FROM)?\s+(\S+)?(?:\s+(\S+))?(?:\s+INNER JOIN\s+(\S+)\s+(\S+)\s+ON\s+(\S+\.\S+)\s*=\s*(\S+\.\S+))?\s*(?:WHERE\s+(.+))?\s*;?\s*$)", regex_constants::icase);
    regex update_regex(R"(^\s*UPDATE\s+(\S+)?\s*(?:SET\s+(.*?))?(?:\s+WHERE\s+(.*))?\s*$)", regex_constants::icase);
    regex delete_regex("^DELETE FROM.*", regex_constants::icase);
    regex show_tables_regex("^SHOW TABLES", regex_constants::icase);

    if (regex_match(query, create_table_basic_pattern)) {
        return make_shared<CreateTableStatement>(query);
    } else if (regex_match(query, drop_table_regex)) {
        return make_shared<DropTableStatement>(query);
    } else if (regex_match(query, select_regex)) {
        return make_shared<SelectStatement>(query);
    } else if (regex_match(query, insert_into_regex)) {
        return make_shared<InsertIntoStatement>(query);
    } else if (regex_match(query, update_regex)) {
        return make_shared<UpdateStatement>(query);
    } else if (regex_match(query, delete_regex)) {
        return make_shared<DeleteFromStatement>(query);
    } else if (regex_match(query, show_tables_regex)) {
        return make_shared<ShowTablesStatement>(query);
    } else if (regex_match(query, SyntaxRegexPatterns::MULTIPLE_KEYWORDS_REGEX)) {
        throw SyntaxException("Multiple keywords detected");
    } else {
        throw SyntaxException("Invalid SQL syntax.");
    }
}

//<editor-fold desc="Utility functions">

void Menu::highlightKeywords(string &line) {
    map<string, string> keywords = {
            {"SELECT", red},
            {"FROM",   red},
            {"WHERE",  red},
            {"INNER",  red},
            {"JOIN",   red},
            {"ON",     red},
            {"DELETE", red},
            {"CREATE", yellow},
            {"TABLE",  yellow},
            {"DROP",   yellow},
            {"INSERT", yellow},
            {"INTO",   yellow},
            {"VALUES", yellow},
            {"UPDATE", cyan},
            {"SET",    cyan},
            {"SHOW",   magenta},
            {"TABLES", magenta},
            {"AND", green},
            {"OR",     green}
    };

    for (const auto &[keyword, color]: keywords) {
        regex keywordPattern("\\b" + keyword + "\\b", regex_constants::icase);
        string replacement;
        replacement.reserve(color.length() + keyword.length() + resetColor.length());
        replacement.append(color).append(keyword).append(resetColor);
        line = regex_replace(line, keywordPattern, replacement);
    }
}

void Menu::cleanConsole() {
#ifdef _WIN32
    system("cls"); // For Windows
#else
    system("clear"); // For Unix-based systems
#endif
}

void Menu::finishProgram() {
    ConsoleUtils::printLine({40}, '\xDA', '\xC4', '\xBF');
    ConsoleUtils::printRow({"Exiting the program..."}, {40});
    ConsoleUtils::printLine({40}, '\xC0', '\xC4', '\xD9');
    cout << "----------------------------------------" << endl;
    ConsoleUtils::printLine({40}, '\xDA', '\xC4', '\xBF', green);
    ConsoleUtils::printRow({"Program successfully completed!"}, {40}, ConsoleUtils::TextAlignment::CENTER, green);
    ConsoleUtils::printLine({40}, '\xC0', '\xC4', '\xD9', green);
    exit(0);
}

//</editor-fold>

