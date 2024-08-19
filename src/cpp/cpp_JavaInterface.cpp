#include <jni.h>
#include "cpp_JavaInterface.h"
#include <string>
#include "Database.h"
#include "Menu.h"

using namespace std;

shared_ptr<Database> database = nullptr;

JNIEXPORT void JNICALL Java_cpp_JavaInterface_createNewDatabase (JNIEnv *env, jobject obj) {
    database = make_shared<Database>("untitled");
    cout << "Database \"" << database->getName() << "\" has been " << green << "successfully" << resetColor << " created!" << endl;
}

JNIEXPORT void JNICALL Java_cpp_JavaInterface_executeQuery (JNIEnv *env, jobject obj, jstring jquery) {
    const char *jquery_ptr = env->GetStringUTFChars(jquery, 0);
    string query(jquery_ptr);
    env->ReleaseStringUTFChars(jquery, jquery_ptr);

    try {
        shared_ptr<Statement> statement = Menu::parseSQLQuery(query);
        statement->execute(*database);
    } catch (exception &e) {
        cout << e.what() << endl;
        return;
    }
    cout << "\nQuery has been " << green << "successfully" << resetColor << " executed!" << endl;
};

JNIEXPORT void JNICALL Java_cpp_JavaInterface_importDatabase (JNIEnv *env, jobject obj, jstring jfile_path) {
    const char *file_path_ptr = env->GetStringUTFChars(jfile_path, 0);
    string file_path(file_path_ptr);
    env->ReleaseStringUTFChars(jfile_path, file_path_ptr);

    if (file_path[0] == '\"' && file_path[file_path.size() - 1] == '\"') {
        file_path = file_path.substr(1, file_path.size() - 2);
    } else if (file_path[0] == '\'' && file_path[file_path.size() - 1] == '\'') {
        file_path = file_path.substr(1, file_path.size() - 2);
    }

    try {
        shared_ptr<Format> format;
        regex validPathRegex(".*\\.(dbexp|sql)$", regex_constants::icase);
        if (!regex_match(file_path, validPathRegex)) {
            throw InvalidFileImportException(
                    "Invalid file path for importing database. Please provide a valid path with .\033[1mdbexp\033[0m or .\033[1msql\033[0m extension.");
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
        database = make_shared<Database>(format->parseDatabaseName(firstLine));
        database->importDatabase(*format, file_path);
        cout << green << "Database imported successfully from " << resetColor << "\"" << file_path << "\"" << endl
             << endl;
    } catch (const InvalidFileImportException &e) {
        cout << e.what() << endl;
    }

};

JNIEXPORT void JNICALL Java_cpp_JavaInterface_exportDatabase (JNIEnv *env, jobject obj, jstring jformat, jstring jfile_path) {
    const char *format_ptr = env->GetStringUTFChars(jformat, 0);
    string formatStr(format_ptr);
    env->ReleaseStringUTFChars(jformat, format_ptr);

    const char *file_path_ptr = env->GetStringUTFChars(jfile_path, 0);
    string file_path(file_path_ptr);
    env->ReleaseStringUTFChars(jfile_path, file_path_ptr);

    shared_ptr<Format> format;
    if(formatStr == "sql") {
        format = make_shared<SQLFormat>();
    } else if(formatStr == "dbexp") {
        format = make_shared<CustomFormat>();
    } else {
        throw InvalidFileExportException("\nInvalid file format for exporting database. Please provide a valid format.");
    }
    database->exportDatabase(*format, file_path);

};