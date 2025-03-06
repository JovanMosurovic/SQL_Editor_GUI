# SQL Editor (with GUI)

This project encapsulates a **Graphical User Interface (GUI)** based Database Management System (DBMS) developed in **C++** with a **JavaFX** front-end. It offers an interactive interface for users to manipulate databases, tables, and records. The system is designed to interpret and execute SQL-like commands, enabling a comprehensive suite of operations such as creating, reading, updating, and deleting (CRUD) data.

The GUI provides an intuitive and user-friendly environment for interacting with the database, improving upon the original command-line interface. Users can now execute SQL queries directly within the JavaFX application, visualize table structures, and receive real-time feedback on their queries.

Additionally, the system now supports a wider range of SQL queries, enhancing its versatility for database management tasks.\
The **C++ native code** from the original [SQL Editor project](https://github.com/JovanMosurovic/SQL_Editor) has been integrated into JavaFX, offering performance and functionality improvements. The C++ code has been adapted from that project to fit the native format required by this JavaFX application, and the modified version (with minor changes) can be found in the [`cpp`](https://github.com/JovanMosurovic/SQL_Editor_with_GUI/tree/master/src/cpp) folder (`/src/cpp`).

> The primary motivation behind this project was to master **JavaFX** and various data structures in the Java programming language, as well as to gain a deeper understanding of integrating **native C++** with Java. 
>
> This project was developed as the [second university assignment](instructions.pdf) for "Practicum of Object-Oriented Programming" at the University of Belgrade School of Electrical Engineering majoring in Software Engineering. Please refer to the file for detailed assignment instructions.

## Table of Contents

- [Project demo](#project-demo)
- [Features](#features)
  - [Enhanced Graphical User Interface (GUI)](#enhanced-graphical-user-interface-gui)
  - [Improved Console and Storage Handling](#improved-console-and-storage-handling)
  - [Customizable Theme and Font Settings](#customizable-theme-and-font-settings)
  - [Interactive Saving in SQL and Custom file format](#interactive-saving-in-sql-and-custom-file-format)
  - [Expanded SQL Query Support](#expanded-sql-query-support)
- [Running the Project](#running-the-project)
  - [Running on Windows](#running-on-windows)
  - [Running on Linux](#running-on-linux)
  - [Running on macOS](#running-on-macos)

## Project demo

https://github.com/user-attachments/assets/e84dc0f7-8671-4018-b69d-c27408e7efa0

 > **Note:** The demo showcases only some of the features of the project. <br/>
 > The SQL Editor supports additional SQL commands and functionalities that are not showcased here.

## Features

### Enhanced Graphical User Interface (GUI)
The GUI, developed using a combination of JavaFX and CSS, provides a clean and user-friendly environment for executing SQL queries and managing databases. Unlike the previous command-line interface, the graphical approach improves usability by allowing users to:
- Run SQL commands in a dedicated editor window.
- View table structures and results in a structured format.
- Receive interactive real-time feedback, including error messages and execution statuses.
- Adjust interface settings to enhance user comfort, ensuring a more personalized and pleasant experience.

### Improved Console and Storage Handling
The console output system has been optimized for better readability and usability. Now, users can:
- Easily navigate previous query results.
- View formatted messages with improved clarity.
- Better track errors and warnings, reducing debugging time.

### Customizable Theme and Font Settings
The application supports **multiple themes**, allowing users to switch between different visual styles, including:
- A **light mode** optimized for daytime use.
- A **dark mode** for a more comfortable experience in low-light conditions.

In addition, users can personalize their experience by adjusting font settings for both:
- The **SQL editor**, where queries are written.
- The **console output**, where results and logs are displayed.

These customizable settings enhance readability and provide a more personalized experience based on individual preferences.

### Interactive Saving in SQL and Custom file format 
The application provides **interactive saving** features to ensure users don't lose their work. If the work is not saved, the application will notify the user. Users can save their progress in either:
- **SQL format** `.sql`, a standard SQL format supported universally.
- **Custom file format** `.dbexp`, which was specifically created for the purposes of this project and is not used outside of it. This custom format is unique to the application and is tied exclusively to this project.

### Expanded SQL Query Support
The SQL Editor now supports an extended set of queries beyond the original implementation. A complete list of supported queries, including previous ones, can be found [here](https://github.com/JovanMosurovic/SQL_Editor_Console/tree/master?tab=readme-ov-file#sql-command-format).

In addition to those, the editor now supports:
- Aggregate Functions:
  ```sql
  SELECT COUNT(*) FROM <table_name>;
  SELECT COUNT(<columnN>) FROM <table_name>;
  SELECT SUM(<columnN>) FROM <table_name>;
  SELECT AVG(<columnN>) FROM <table_name>;
  SELECT MAX(<columnN>) FROM <table_name>;
  SELECT MIN(<columnN>) FROM <table_name>;
  SELECT MIN(<columnN>), MAX(<columnM>) FROM <table_name>;
  SELECT COUNT(*), SUM(<columnN>), AVG(<columnN>) FROM <table_name>;
  ```

- `DISTINCT` Clause:
  ```sql
  SELECT DISTINCT <columnN> FROM <table_name>;
  SELECT DISTINCT(<columnN>) FROM <table_name>;
  ```

- `GROUP BY` Clause:
  ```sql
  SELECT <columnN>, COUNT(*) FROM <table_name> GROUP BY <columnN>;
  SELECT <columnN>, AVG(<columnM>) FROM <table_name> GROUP BY <columnN>;
  ```

- `HAVING` Clause:
  ```sql
  SELECT <columnN>, COUNT(*) FROM <table_name> 
  GROUP BY <columnN> HAVING COUNT(*) > <valueN>;
  
  SELECT <columnN>, AVG(<columnM>) FROM <table_name> 
  GROUP BY <columnN> HAVING AVG(<columnM>) > <valueN>;
  ```

- `ORDER BY` Clause:
  ```sql
  SELECT * FROM <table_name> ORDER BY <columnN> ASC;
  SELECT * FROM <table_name> ORDER BY <columnN> DESC;
  SELECT * FROM <table_name> ORDER BY <columnN> ASC, <columnM> DESC;
  ```

- `LIMIT` and `OFFSET` Clauses:
  ```sql
  SELECT * FROM <table_name> LIMIT <valueN>;
  SELECT * FROM <table_name> LIMIT <valueN> OFFSET <valueM>;
  ```
- All SQL clauses can be combined with each other as in the command line version of this project (the new clauses support this functionality as well).<br />
For example:
```sql
SELECT COUNT(*), <columnN>  
FROM <table_name> 
GROUP BY <columnN> 
HAVING COUNT(*) = <valueN> 
ORDER BY <columnN> ASC 
LIMIT <valueM> OFFSET <valueK>;
```

## Notes

In the SQL commands listed above, `<table_name>`, `<columnN>`, `<valueN>`, `<condition>`, and `<aliasN>` are placeholders. You should replace them with the actual table names, column names, values, conditions, and aliases that you use in your project. Here's what each placeholder represents:

- `<table_name>`: The name of the table on which you want to perform the operation.
- `<columnN>`: The name of the column in the table on which you want to perform the operation.
- `<valueN>`: The value that you want to insert or compare in the operation.
- `<condition>`: The condition that you want to apply to the operation.
- `<aliasN>`: The alias that you want to use for the table in the operation.

Please replace these placeholders with actual values before executing the SQL commands.

## Running the Project

This guide assumes that Java and JavaFX are already installed and properly configured on your system.<br /> 
The instructions below do not cover the installation of these prerequisites.

To set up and run the project, follow these steps:

1. **Initialize the C++ native environment**  
   Before launching the application, execute the appropriate script based on your operating system:  
   - [**Windows:**](#running-on-windows) `scriptWindows.bat`  
   - [**Linux:**](#running-on-linux) `scriptLinux.sh`
   - [**macOS:**](#running-on-macos) `scriptMacOS.sh`

   🚨 **Important:** Ensure that the script is correctly configured by specifying the **path** to the C++ native code directory within the project. 

2. **Verify JavaFX and Java installation**  
   Confirm that your development environment supports **JavaFX** and that **Java** is properly installed, as the application relies on JavaFX for the GUI.

3. **Run the application**  
   After all of the previous steps have been completed successfully, run the project.

<details>
  <summary>Running on Windows</summary>

  ## Running on Windows  

https://github.com/user-attachments/assets/c3c50f2d-3945-46a1-a70c-1ff5753574a6

</details>

<details>
  <summary>Running on Linux</summary>

  ## Running on Linux  

  To properly set up and run the application on Linux, follow these steps:  

  ### 1. Set the `JAVA_HOME` Environment Variable  
  Define the `JAVA_HOME` variable to point to your JDK installation:  
  ```bash
  JAVA_HOME=/home/hp/.jdks/corretto-1.8.0_412
  ```  

  ### 2. Verify the Presence of JNI Headers  
  Ensure that the required JNI headers are available in the expected directories:  
  ```bash
  ls $JAVA_HOME/include
  ls $JAVA_HOME/include/linux
  ```  
  Expected output:  
  - The `jni.h` file should be present in the `include` directory.  
  - The `jni_md.h` file should be present in the `include/linux` subdirectory.  

  ### 3. Export the `JAVA_HOME` Variable  
  To make the `JAVA_HOME` variable available to subprocesses, export it:  
  ```bash
  export JAVA_HOME
  ```  
  **Note:** Do not use `=` when exporting, as it would reset the variable.  

  ### 4. Add the Java `bin` Directory to `PATH`  
  To ensure that Java binaries can be accessed globally, add the `bin` directory to your `PATH`:  
  ```bash
  export PATH=$JAVA_HOME/bin:$PATH
  ```  

  ### 5. Grant Execution Permission to the Script  
  If running a shell script, ensure it has execution permissions:  
  ```bash
  chmod +x scriptLinux.sh
  ```  
  Then, execute the script:  
  ```bash
  ./scriptLinux.sh
  ```  
  **Note**: All environment variable exports are session-specific. For permanent configuration, add to `~/.bashrc` or equivalent shell profile.

  ### 6. Wait for Script Completion  
  The script will execute multiple commands required for the native environment. **Wait until all commands complete and the counter reaches 0** before proceeding.  

  Once the script has finished, you can run the program in your development environment.  

  💡 **Suggestion:** *You can use any IDE of your choice, but [IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended since the project and all tests were developed and tested in it.*  

  ### 7. Add Configuration for Native Files
To configure the application to support the native files, follow these steps:

- Open the **Run/Debug Configurations** dialog by selecting your current file in the IDE.
- Choose **Edit Configurations** from the menu.
- Click the `+` button and select **Application**.
- In the **Main class** field, enter `app.Main` or search for it by clicking the **Browse** icon or using the shortcut `Shift + Enter` (`app -> Main`).
- Click on **Modify options**, then check **Add VM options**.
- In the **VM options** field, add the following line:
  ```bash
  -Djava.library.path=native
  ```
**Note:** This step is specifically for IntelliJ IDEA. If you're using a different IDE, the process for configuring the application might differ. Generally, look for a way to add VM options or set environment variables in the configuration settings for your specific IDE.

</details>

<details>
  <summary>Running on macOS</summary>

  ## Running on macOS  

   ### Installing Homebrew

   I recommend using [Homebrew](https://brew.sh/) for installing dependencies, as it was the easiest method I used, and it has proven to be the most straightforward.

   To install Homebrew, run the following command in your terminal:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```
   After installation, ensure Homebrew is working by running:
```bash
brew --version
```
   This should return the installed Homebrew version.
   
   ### Compiler Setup 
   
   🚨 By default, macOS uses **Clang** as the compiler. However, **Clang** does not compile C++ native code correctly, so it is necessary to use a different compiler.
   **GCC** is used in the instructions because the project has been tested and works correctly with it. Other compilers can be used, but using a different compiler might lead to 
   errors.

   ### 1. Install GCC via Homebrew
   If you haven't installed GCC, you can install it by running the following command:

   ```bash
   brew install gcc
   ```

   ### 2. Verify GCC Installation
   Once GCC is installed, verify the installation by checking the version of the Homebrew-installed GCC. The command will differ based on your system architecture:

For **Apple Silicon (M1/M2)**:

```bash
/opt/homebrew/bin/g++-14 --version
```

For **Intel Mac**:
```bash
/usr/local/bin/g++-14 --version
```

The expected output should resemble the following:

```plaintext
g++-14 (Homebrew GCC 14.1.0) 14.1.0
```

**Note:**  
If the version returned is different from `14` (e.g., `g++-12`, `g++-13`), you will need to update the script by replacing all occurrences of `/opt/homebrew/bin/g++-14` with the version number shown in your output (e.g., `/opt/homebrew/bin/g++-12`).
  
   ### Running the project
   To set up and run the project on macOS, follow these steps:

### 1. Grant Execution Permission to the Script
For running a shell script, ensure it has execution permissions:
```bash
chmod +x scriptMacOS.sh
```

### 2. Run the Script
After the script has execution permissions, run it:
```bash
./scriptMacOS.sh
```
**Note**: All environment variable exports are session-specific. For permanent configuration, add to `~/.zshrc` (for zsh) or `~/.bash_profile` (for bash).

### 3. Wait for Script Completion  
  The script will execute multiple commands required for the native environment. **Wait until all commands complete and the counter reaches 0** before proceeding.  

  Once the script has finished, you can run the program in your development environment.  

  💡 **Suggestion:** *You can use any IDE of your choice, but [IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended since the project and all tests were developed and tested in it.*  

### 4. Add Configuration for Native Files
To configure the application to support the native files, follow these steps:

- Open the **Run/Debug Configurations** dialog by selecting your current file in the IDE.
- Choose **Edit Configurations** from the menu.
- Click the `+` button and select **Application**.
- In the **Main class** field, enter `app.Main` or search for it by clicking the **Browse** icon or using the shortcut `Shift + Enter` (`app -> Main`).
- Click on **Modify options**, then check **Add VM options**.
- In the **VM options** field, add the following line:
  ```bash
  -Djava.library.path=native
  ```
**Note:** This step is specifically for IntelliJ IDEA. If you're using a different IDE, the process for configuring the application might differ. Generally, look for a way to add VM options or set environment variables in the configuration settings for your specific IDE.

</details>


