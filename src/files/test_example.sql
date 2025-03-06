CREATE TABLE Students (student_id, first_name, last_name, subject);
INSERT INTO Students (student_id, first_name, last_name, subject) VALUES ("1", "John", "Doe", "Math");
INSERT INTO Students (student_id, first_name, last_name, subject) VALUES ("2", "Alice", "Smith", "Science");
INSERT INTO Students (student_id, first_name, last_name, subject) VALUES ("3", "Bob", "Brown", "History");
INSERT INTO Students (student_id, first_name, last_name, subject) VALUES ("4", "Eve", "Davis", "Math");
INSERT INTO Students (student_id, first_name, last_name, subject) VALUES ("5", "Charlie", "Wilson", "Science");

CREATE TABLE Grades (grade_id, student_id, subject, grade);
INSERT INTO Grades (grade_id, student_id, subject, grade) VALUES ("1", "1", "Math", "5");
INSERT INTO Grades (grade_id, student_id, subject, grade) VALUES ("2", "2", "Science", "4");
INSERT INTO Grades (grade_id, student_id, subject, grade) VALUES ("3", "3", "History", "3");
INSERT INTO Grades (grade_id, student_id, subject, grade) VALUES ("4", "4", "Math", "2");
INSERT INTO Grades (grade_id, student_id, subject, grade) VALUES ("5", "5", "Science", "1");

CREATE TABLE Courses (course_id, name, hours);
INSERT INTO Courses (course_id, name, hours) VALUES ("1", "Mathematics", "120");
INSERT INTO Courses (course_id, name, hours) VALUES ("2", "Physics", "100");
INSERT INTO Courses (course_id, name, hours) VALUES ("3", "Literature", "80");
INSERT INTO Courses (course_id, name, hours) VALUES ("4", "Chemistry", "110");
INSERT INTO Courses (course_id, name, hours) VALUES ("5", "Biology", "90");

CREATE TABLE Enrollments (enrollment_id, student_id, course_id, year);
INSERT INTO Enrollments (enrollment_id, student_id, course_id, year) VALUES ("1", "1", "1", "2023");
INSERT INTO Enrollments (enrollment_id, student_id, course_id, year) VALUES ("2", "2", "2", "2023");
INSERT INTO Enrollments (enrollment_id, student_id, course_id, year) VALUES ("3", "3", "3", "2023");
INSERT INTO Enrollments (enrollment_id, student_id, course_id, year) VALUES ("4", "4", "4", "2023");
INSERT INTO Enrollments (enrollment_id, student_id, course_id, year) VALUES ("5", "5", "5", "2023");
