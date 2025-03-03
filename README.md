# SQL Editor (with GUI)

This project encapsulates a **Graphical User Interface (GUI)** based Database Management System (DBMS) developed in **C++** with a **JavaFX** front-end. It offers an interactive interface for users to manipulate databases, tables, and records. The system is designed to interpret and execute SQL-like commands, enabling a comprehensive suite of operations such as creating, reading, updating, and deleting (CRUD) data.

The GUI provides an intuitive and user-friendly environment for interacting with the database, improving upon the original command-line interface. Users can now execute SQL queries directly within the JavaFX application, visualize table structures, and receive real-time feedback on their queries.

Additionally, the system now supports a wider range of SQL queries, enhancing its versatility for database management tasks.\
The **C++ native code** from the original [SQL Editor project](https://github.com/JovanMosurovic/SQL_Editor) has been integrated into JavaFX, offering performance and functionality improvements. The C++ code has been adapted from that project to fit the native format required by this JavaFX application, and the modified version (with minor changes) can be found in the [`cpp`](https://github.com/JovanMosurovic/SQL_Editor_with_GUI/tree/master/src/cpp) folder (`/src/cpp`).

> The primary motivation behind this project was to master **JavaFX** and various data structures in the Java programming language, as well as to gain a deeper understanding of integrating **native C++** with Java. 
>
> This project was developed as the [second university assignment](instructions.pdf) for "Practicum of Object-Oriented Programming" at the University of Belgrade School of Electrical Engineering majoring in Software Engineering. Please refer to the file for detailed assignment instructions.

## Running the Project

To set up and run the project, follow these steps:

1. **Initialize the C++ native environment**  
   Before launching the application, execute the appropriate script based on your operating system:  
   - **Windows:** `scriptWindows.bat`  
   - **Linux:** `scriptLinux.sh`  

   **Note:** Ensure that the script is correctly configured by specifying the **path** to the C++ native code directory within the project.

2. **Verify JavaFX and Java installation**  
   Confirm that your development environment supports **JavaFX** and that **Java** is properly installed, as the application relies on JavaFX for the GUI.

3. **Run the application**  
   After all of the previous steps were completed successfully, simply run the project.
