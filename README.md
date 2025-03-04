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

   🚨 **Important:** Ensure that the script is correctly configured by specifying the **path** to the C++ native code directory within the project.

2. **Verify JavaFX and Java installation**  
   Confirm that your development environment supports **JavaFX** and that **Java** is properly installed, as the application relies on JavaFX for the GUI.

3. **Run the application**  
   After all of the previous steps were completed successfully, simply run the project.

<details>
  <summary>Running on Windows</summary>

  ## Running on Windows  

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

</details>


