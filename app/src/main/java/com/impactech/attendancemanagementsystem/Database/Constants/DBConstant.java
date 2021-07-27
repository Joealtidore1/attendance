package com.impactech.attendancemanagementsystem.Database.Constants;

public class DBConstant {
    //DATABASE NAME
    public static String DB_NAME = "attendance.db";

    //DATABASE TABLES
    public static String LECTURER_TABLE = "lecturer";
    public static String STUDENT_TABLE = "student";
    public static String COURSE_TABLE = "course";
    public static String ENROLLMENT_TABLE = "enrollment";
    public static String COURSE_ASSIGNMENT_TABLE = "course_assignment";
    public static String COURSE_COORDINATOR_TABLE = "course_coordinator";
    public static String SESSION_TABLE = "session";
    public static String ATTENDANCE_TABLE = "attendance";
    public static String DEPARTMENT_TABLE = "department";
    public static String LECTURER_CLASS_REG_TABLE = "lecturer_class_reg";

    //COMMON FIELD NAMES
    public static String ID = "id";
    public static String SURNAME = "surname";
    public static String FIRST_NAME = "first_name";
    public static String OTHER_NAMES = "other_names";
    public static String DEPARTMENT = "department";
    public static String CODE = "code";
    public static String SESSION = "session";
    public static String DATE = "date";

    //LECTURER FIELD NAMES
    public static String STAFF_ID = "staff_id";
    public static String DESIGNATION = "designation";
    public static String DISCIPLINE = "discipline";
    public static String RELATED_FIELDS = "related_fields";
    public static String ROLE = "role";
    public static String PASSWORD = "password";

    //STUDENT FIELD NAMES
    public static String REG_NUMBER = "registration_number";
    public static String BLUETOOTH_ID = "bluetooth";

    //COURSE FIELD NAMES
    public static String TITLE = "title";
    public static String CREDIT_UNIT = "credit_unit";
    public static String SEMESTER = "semester";


    //COURSE ASSIGNMENT FIELDS
    public static String LECTURER = "lecturer";

    //COURSE COORDINATOR FIELDS
    public static String COORDINATOR = "coordinator";

    //COURSE DEPARTMENT FIELDS
    public static String NAME = "name";

    //COMMON CREATES
    public static String CREATE = "CREATE TABLE IF NOT EXISTS ";
    public static String IDS  = "(" + ID + " INTEGER PRIMARY KEY NOT NULL, ";
    public static String TEXT_N = " TEXT NOT NULL, ";
    public static String TEXT = " TEXT, ";
    public static String INTEGER_N = " INTEGER NOT NULL, ";
    public static String INTEGER = " INTEGER, ";

    //CREATE LECTURER TABLE
    public static String CREATE_LECTURER_TABLE = CREATE
            + LECTURER_TABLE
            + IDS
            + STAFF_ID + " TEXT UNIQUE NOT NULL, "
            + SURNAME + TEXT_N
            + FIRST_NAME + TEXT_N
            + OTHER_NAMES + TEXT
            + DESIGNATION + TEXT
            + DISCIPLINE + TEXT_N
            + RELATED_FIELDS + TEXT
            + DEPARTMENT + TEXT_N
            + ROLE + TEXT_N
            + PASSWORD + " TEXT NOT NULL);";

    public static String CREATE_STUDENT_TABLE = CREATE
            + STUDENT_TABLE
            + IDS
            + REG_NUMBER + " TEXT UNIQUE NOT NULL, "
            + SURNAME + TEXT_N
            + FIRST_NAME + TEXT_N
            + OTHER_NAMES + TEXT
            + DEPARTMENT +  TEXT_N
            + BLUETOOTH_ID + " TEXT NOT NULL);";

    public static String CREATE_COURSE_TABLE = CREATE
            + COURSE_TABLE
            + IDS
            + CODE + " TEXT UNIQUE NOT NULL, "
            + TITLE + TEXT_N
            + SEMESTER + INTEGER_N
            + DEPARTMENT + TEXT_N
            + CREDIT_UNIT + " INTEGER NOT NULL);";

    public static String CREATE_ENROLLMENT_TABLE = CREATE
            + ENROLLMENT_TABLE
            + IDS
            + CODE + TEXT_N
            + DEPARTMENT + TEXT_N
            + REG_NUMBER + TEXT_N
            + SESSION + " TEXT NOT NULL);";

    public static String CREATE_COURSE_ASSIGNMENT_TABLE = CREATE
            + COURSE_ASSIGNMENT_TABLE
            + IDS
            + CODE + TEXT_N
            + STAFF_ID + TEXT_N
            + SESSION + " TEXT NOT NULL);";

    public static String CREATE_COURSE_COORDINATOR_TABLE = CREATE
            + COURSE_COORDINATOR_TABLE
            + IDS
            + CODE + TEXT_N
            + STAFF_ID + TEXT_N
            + SESSION + " TEXT NOT NULL);";

    public static String CREATE_SESSION_TABLE = CREATE
            + SESSION_TABLE
            + IDS
            + SESSION + " TEXT NOT NULL);";


    public static String CREATE_ATTENDANCE_TABLE = CREATE
            + ATTENDANCE_TABLE
            + IDS
            + CODE + TEXT_N
            + REG_NUMBER + TEXT_N
            + SESSION + TEXT_N
            + DATE + " TEXT NOT NULL);";

    public static String CREATE_DEPARTMENT_TABLE = CREATE
            + DEPARTMENT_TABLE
            + IDS
            + CODE + TEXT_N
            + NAME +  " TEXT NOT NULL);";

    public static String CREATE_LECTURER_CLASS_REG_TABLE = CREATE
            + LECTURER_CLASS_REG_TABLE
            + IDS
            + CODE + TEXT_N
            + STAFF_ID + TEXT_N
            + SESSION + TEXT_N
            + DATE + " TEXT NOT NULL);";












}
