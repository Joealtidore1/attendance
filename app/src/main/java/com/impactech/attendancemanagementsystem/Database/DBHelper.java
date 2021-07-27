package com.impactech.attendancemanagementsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

import com.impactech.attendancemanagementsystem.Model.Attendance;
import com.impactech.attendancemanagementsystem.Model.AttendanceDetails;
import com.impactech.attendancemanagementsystem.Model.Course;
import com.impactech.attendancemanagementsystem.Model.CourseAssignment;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.Model.Enrollment;
import com.impactech.attendancemanagementsystem.Model.Lecturer;
import com.impactech.attendancemanagementsystem.Model.Student;

import java.util.ArrayList;
import java.util.List;

import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.ATTENDANCE_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.BLUETOOTH_ID;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CODE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.COURSE_ASSIGNMENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.COURSE_COORDINATOR_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.COURSE_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_ATTENDANCE_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_COURSE_ASSIGNMENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_COURSE_COORDINATOR_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_COURSE_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_DEPARTMENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_ENROLLMENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_LECTURER_CLASS_REG_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_LECTURER_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_SESSION_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREATE_STUDENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.CREDIT_UNIT;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DATE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DB_NAME;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DEPARTMENT;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DEPARTMENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DESIGNATION;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DISCIPLINE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.ENROLLMENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.FIRST_NAME;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.LECTURER_CLASS_REG_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.LECTURER_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.NAME;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.OTHER_NAMES;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.PASSWORD;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.REG_NUMBER;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.RELATED_FIELDS;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.ROLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.SEMESTER;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.SESSION;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.SESSION_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STAFF_ID;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STUDENT_TABLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.SURNAME;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.TITLE;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    private ContentValues cv;
    private SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_COURSE_ASSIGNMENT_TABLE);
        db.execSQL(CREATE_COURSE_COORDINATOR_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_DEPARTMENT_TABLE);
        db.execSQL(CREATE_ENROLLMENT_TABLE);
        db.execSQL(CREATE_LECTURER_TABLE);
        db.execSQL(CREATE_SESSION_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_LECTURER_CLASS_REG_TABLE);
        addInitAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Record existence methods
    public boolean isExists(String id, String table, SQLiteDatabase db){
        String where;
        if (table.equals(LECTURER_TABLE))
            where = STAFF_ID + " = ?";
        else
            where = REG_NUMBER + " = ?";
        final Cursor query = db.query(table,
                null,
                where,
                new String[]{id},
                null,
                null,
                null);
        boolean res = query.getCount() != 0;
        query.close();

        return res;

    }
    public boolean isCourse(String code){
        String where = CODE + " = ?";
        final Cursor query = getReadableDatabase().query(COURSE_TABLE, null, where, new String[]{code}, null, null, null);
        final boolean b = query.moveToNext();
        query.close();
        return b;
    }
    public boolean isBluetooth(String bt){
        String where = BLUETOOTH_ID + " = ?";
        final Cursor query = getReadableDatabase().query(STUDENT_TABLE, null, where, new String[]{bt}, null, null, null);
        boolean res = query.moveToNext();
        query.close();
        return res;
    }
    public boolean isAssigned(CourseAssignment as){
        String where = CODE + " = ? AND " + STAFF_ID + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(COURSE_ASSIGNMENT_TABLE, null, where, new String[]{as.getCode(), as.getStaffId(), as.getSession()}, null, null, null);
        final boolean b = query.moveToNext();
        query.close();
        return b;
    }
    public boolean isEnrolled(Enrollment en){
        String where = CODE + " = ? AND " + REG_NUMBER + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(ENROLLMENT_TABLE, null, where, new String[]{en.getCode(), en.getRegNo(), en.getSession()}, null, null, null);

        final boolean b = query.moveToNext();
        query.close();
        return b;
    }
    public boolean isCoordinated(CourseAssignment as){
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(COURSE_COORDINATOR_TABLE, null, where, new String[]{as.getCode(), as.getStaffId(), as.getSession()}, null, null, null);

        final boolean b = query.moveToNext();
        query.close();
        return b;
    }
    public boolean isSession(String session){
        String where = SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(SESSION_TABLE, null, where, new String[]{session}, null, null, null);

        final boolean b = query.moveToNext();
        query.close();
        return b;
    }
    public boolean isAttended(Attendance att){
        String where = CODE + " = ? AND " + REG_NUMBER + " = ? AND " + SESSION + " = ? AND " + DATE + " = ? ";
        final Cursor query = getReadableDatabase().query(ATTENDANCE_TABLE, null, where, new String[]{att.getCode(), att.getRegNo(), att.getSession(), att.getDate()}, null, null, null);

        final boolean b = query.moveToNext();
        query.close();
        return b;
    }
    public boolean isDepartment(String code){
        String where = CODE + " = ?";
        final Cursor query = getReadableDatabase().query(DEPARTMENT_TABLE, null, where, new String[]{code}, null, null, null);
        boolean res = query.moveToNext();
        query.close();
        return res;
    }
    public boolean isRecorded(Attendance att){
        String where = CODE + " = ? AND " + STAFF_ID + " = ? AND " + SESSION + " = ? AND " + DATE + " ? ";
        final Cursor query = getReadableDatabase().query(LECTURER_CLASS_REG_TABLE, null, where, new String[]{att.getCode(), att.getRegNo(), att.getSession(), att.getDate()}, null, null, null);
        boolean res = query.moveToNext();
        query.close();
        return res;
    }


    //Initial Admin Account creation
    public void addInitAdmin(SQLiteDatabase db){
        cv = new ContentValues();
        cv.put(STAFF_ID, "admin");
        cv.put(SURNAME,"Pete");
        cv.put(FIRST_NAME, "Andre");
        cv.put(DESIGNATION, "Admin");
        cv.put(DISCIPLINE, "Management");
        cv.put(DEPARTMENT, "csc");
        cv.put(ROLE, "admin");
        cv.put(PASSWORD, "12345");
        //db = getWritableDatabase();
        if(!isExists("123456789", LECTURER_TABLE, db)) {
            db.insert(LECTURER_TABLE, null, cv);
        }
    }

    //Insertion methods
    public void addLecturer(Lecturer lect){
        SQLiteDatabase db = getReadableDatabase();
        if(isExists(lect.getStaffId(), LECTURER_TABLE, db)){
            showToast("Lecturer already exists");
            return;
        }
        cv = new ContentValues();
        cv.put(STAFF_ID, lect.getStaffId());
        cv.put(SURNAME, lect.getSurname());
        cv.put(FIRST_NAME, lect.getFirstName());
        cv.put(DESIGNATION, lect.getDesignation());
        cv.put(DISCIPLINE, lect.getDiscipline());
        cv.put(RELATED_FIELDS, lect.getDiscipline());
        cv.put(DEPARTMENT, lect.getDepartment());
        cv.put(ROLE, lect.getRole());
        cv.put(PASSWORD, lect.getPassword());

        final long insert = db.insert(LECTURER_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
        }else{
            showToast("Success");
        }
    }

    public void addStudent(Student stud){
        SQLiteDatabase db = getWritableDatabase();
        if(isExists(stud.getRegNo(), STUDENT_TABLE, db)){
            showToast("Student already exists");
            return;
        }
        cv = new ContentValues();
        cv.put(REG_NUMBER, stud.getRegNo());
        cv.put(SURNAME, stud.getSurname());
        cv.put(FIRST_NAME, stud.getFirstName());
        cv.put(OTHER_NAMES, stud.getOtherNames());
        cv.put(DEPARTMENT, stud.getDepartment());
        cv.put(BLUETOOTH_ID, stud.getBluetoothId());

        final long insert = db.insert(STUDENT_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");

    }

    public void addCourse(Course course){
        if(isCourse(course.getCode())){
            showToast("Course already exist");
            return;
        }
        cv = new ContentValues();
        cv.put(CODE, course.getCode());
        cv.put(TITLE, course.getTitle());
        cv.put(SEMESTER, course.getSemester());
        cv.put(DEPARTMENT, course.getDept());
        cv.put(CREDIT_UNIT, course.getCreditUnits());
        final long insert = getWritableDatabase().insert(COURSE_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");

    }

    public void enrollStudent(Enrollment enroll){
        final SQLiteDatabase db = getWritableDatabase();
        if(!isExists(enroll.getRegNo(), STUDENT_TABLE, db)){
            showToast("Student has not been registered");
            return;
        }
        if(!isCourse(enroll.getCode())){
            showToast("Course does not exist");
            return;
        }

        if(isEnrolled(enroll)){
            showToast("Student has enrolled already");
            return;
        }
        cv = new ContentValues();
        cv.put(CODE, enroll.getCode());
        cv.put(REG_NUMBER, enroll.getRegNo());
        cv.put(SESSION, enroll.getSession());
        cv.put(DEPARTMENT, getStudentByCode(enroll.getRegNo()).getDepartment());


        final long insert = db.insert(ENROLLMENT_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed " + insert);
            return;
        }
        showToast("Success");
    }

    public void assignCourse(CourseAssignment assign){
        final SQLiteDatabase db = getWritableDatabase();
        if(!isExists(assign.getStaffId(), LECTURER_TABLE, db)){
            showToast("Lecturer has not been registered");
            return;
        }
        if(!isCourse(assign.getCode())){
            showToast("Course does not exist");
            return;
        }

        if(isAssigned(assign)){
            showToast("This course has already been assigned");
            return;
        }

        cv = new ContentValues();
        cv.put(CODE, assign.getCode());
        cv.put(STAFF_ID, assign.getStaffId());
        cv.put(SESSION, assign.getSession());

        final long insert = db.insert(COURSE_ASSIGNMENT_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");
    }

    public void assignCoordinator(CourseAssignment en){
        final SQLiteDatabase db = getWritableDatabase();
        if(!isExists(en.getStaffId(), LECTURER_TABLE, db)){
            showToast("Lecturer has not been registered");
            return;
        }
        if(!isCourse(en.getCode())){
            showToast("Course does not exist");
            return;
        }

        if(isCoordinated(en)){
            showToast("This course has already been assigned a coordinator");
            return;
        }

        cv = new ContentValues();
        cv.put(CODE, en.getCode());
        cv.put(STAFF_ID, en.getStaffId());
        cv.put(SESSION, en.getSession());

        final long insert = db.insert(COURSE_ASSIGNMENT_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");
    }

    public void addSession (String session){
        if(isSession(session)){
            showToast("Session already exist");
            return;
        }
        cv = new ContentValues();
        cv.put(SESSION, session);

        final long insert = getWritableDatabase().insert(SESSION_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");

    }

    public boolean addAttendance(Attendance att){
        if(isAttended(att)) return true;

        cv = new ContentValues();
        cv.put(CODE, att.getCode());
        cv.put(REG_NUMBER, att.getRegNo());
        cv.put(SESSION, att.getSession());
        cv.put(DATE, att.getDate());

        final long insert = getWritableDatabase().insert(ATTENDANCE_TABLE, null, cv);
        if(insert < 0){
           // showToast("Failed");
            return false;
        }
        return true;
        //showToast("Success");
    }

    public void addDepartment(Department dept){
        if(isDepartment(dept.getCode())){
            showToast("Department already exists");
            return;
        }

        cv = new ContentValues();
        cv.put(CODE, dept.getCode());
        cv.put(NAME, dept.getName());

        final long insert = getWritableDatabase().insert(DEPARTMENT_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");
    }

    public void addLecturerClassReg(Attendance att){
        if(isRecorded(att)) return;

        cv = new ContentValues();
        cv.put(CODE, att.getCode());
        cv.put(STAFF_ID, att.getStaffId());
        cv.put(SESSION, att.getSession());
        cv.put(DATE, att.getDate());

        final long insert = getWritableDatabase().insert(LECTURER_CLASS_REG_TABLE, null, cv);
        if(insert < 0){
            showToast("Failed");
            return;
        }
        showToast("Success");
    }

    //QUERY METHODS
    public List<Course> getAllCourses(String dept){
        List<Course> courses = new ArrayList<>();
        String where = DEPARTMENT + " = ? OR " + DEPARTMENT + " = ?";
        final Cursor query = getReadableDatabase().query(COURSE_TABLE, null, where, new String[]{dept, dept.toUpperCase()}, null, null, null);
        while(query.moveToNext()){
            courses.add(
                    new Course(
                            gS(query, CODE),
                            gS(query, TITLE),
                            gS(query, SEMESTER),
                            gS(query, DEPARTMENT),
                            gS(query, CREDIT_UNIT)
                    )
            );
        }
        query.close();

        return courses;
    }

    public List<Course> getAllCourses(){
        List<Course> courses = new ArrayList<>();
        final Cursor query = getReadableDatabase().query(COURSE_TABLE, null, null,null, null, null, null);
        while(query.moveToNext()){
            courses.add(
                    new Course(
                            gS(query, CODE),
                            gS(query, TITLE),
                            gS(query, SEMESTER),
                            gS(query, DEPARTMENT),
                            gS(query, CREDIT_UNIT)
                    )
            );
        }
        query.close();

        return courses;
    }

    public Course getCourse(String code){
        //Course> courses = new ArrayList<>();
        String where = CODE + " = ?";
        final Cursor query = getReadableDatabase().query(COURSE_TABLE, null, where,new String[]{code}, null, null, null);
        if(query.moveToNext()){
            final Course course = new Course(
                    gS(query, CODE),
                    gS(query, TITLE),
                    gS(query, SEMESTER),
                    gS(query, DEPARTMENT),
                    gS(query, CREDIT_UNIT)

            );
            query.close();

            return course;
        }
        query.close();
        return null;
    }

    public Lecturer getLecturer(String staffId){
        String where = STAFF_ID + " = ?";
        Cursor query = getReadableDatabase().query( LECTURER_TABLE, null,  where, new String[]{staffId}, null, null, null, null);
        if(query.moveToNext()) {
            return new Lecturer(
                    gS(query, STAFF_ID),
                    gS(query, SURNAME),
                    gS(query, FIRST_NAME),
                    gS(query, OTHER_NAMES),
                    gS(query, DESIGNATION),
                    gS(query, DISCIPLINE),
                    gS(query, RELATED_FIELDS),
                    gS(query, DEPARTMENT),
                    gS(query, ROLE),
                    gS(query, PASSWORD)
            );
        }
        return null;
    }

    public List<Lecturer> getLecturerByDept(String dept){
        String where = DEPARTMENT + " = ? OR " + DEPARTMENT + " = ?";
        List<Lecturer> list = new ArrayList<>();
        Cursor query = getReadableDatabase().query( LECTURER_TABLE, null,  where, new String[]{dept.toLowerCase(), dept.toUpperCase()}, null, null, null, null);
        while(query.moveToNext()) {
            list.add(new Lecturer(
                    gS(query, STAFF_ID),
                    gS(query, SURNAME),
                    gS(query, FIRST_NAME),
                    gS(query, OTHER_NAMES),
                    gS(query, DESIGNATION),
                    gS(query, DISCIPLINE),
                    gS(query, RELATED_FIELDS),
                    gS(query, DEPARTMENT),
                    gS(query, ROLE),
                   ""
                )
            );
        }
        return list;
    }

    public int getNumberOfEnrollments(String code, String session){
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(ENROLLMENT_TABLE, null, where, new String[]{code, session}, null, null, null);
        int n = query.getCount();
        query.close();
        return n;
    }

    public int getNumberOfClasses(String code, String session){
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(true, ATTENDANCE_TABLE, new String[]{DATE},  where, new String[]{code, session}, null, null, null, null);
        int n = query.getCount();
        query.close();
        return n;
    }

    public List<Course> getAssignedCourses(String staffId, String session){
        String where = STAFF_ID + " = ? AND " + SESSION + "= ?";
        Log.d("QUERY", where);
        List<Course> list = new ArrayList<>();
        final Cursor query = getReadableDatabase().query( COURSE_ASSIGNMENT_TABLE, null,  where, new String[]{staffId, session}, null, null, null, null);
        while (query.moveToNext()){
            list.add(getCourse(gS(query, CODE)));
        }
        query.close();
        return list;
    }

    public List<Enrollment> getEnrolledStudent(String code, String session){
        List<Enrollment> list = new ArrayList<>();
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query(ENROLLMENT_TABLE, null, where, new String[]{code, session}, null, null, null);
        while(query.moveToNext()){
            list.add(new Enrollment(
                    gS(query, CODE),
                    gS(query, REG_NUMBER),
                    gS(query, SESSION)
            ));
        }

        return list;
    }

    public List<Attendance> getAttendance(String code, String session){
        List<Attendance> list = new ArrayList<>();
        String where = CODE + " = ? AND " + SESSION + " = ?";

        final Cursor query = getReadableDatabase().query(true, ATTENDANCE_TABLE, new String[]{REG_NUMBER}, where, new String[]{code, session}, null, null, "\"" + REG_NUMBER + " ASC\"", null);
        while(query.moveToNext()){
            list.add(new Attendance("", "", gS(query, REG_NUMBER), "", ""));
        }
        query.close();
        return list;
    }
    public int getNoOfAttendaceById(String code, String regNo, String session){
        String where = CODE + " = ? AND " + SESSION + " = ? AND " + REG_NUMBER +  " = ?";

        final Cursor query = getReadableDatabase().query(true, ATTENDANCE_TABLE, new String[]{DATE}, where, new String[]{code, session, regNo}, null, null, null, null);
        int n = query.getCount();
        query.close();
        return n;
    }


    public int getTotalAttendance(String code, String session){
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query( ATTENDANCE_TABLE, null,  where, new String[]{code, session}, null, null, null, null);
        int n = query.getCount();
        query.close();
        return n;
    }

    public double getAverageAttendance(String code, String session){
        int n = getNumberOfClasses(code, session);
        if(n != 0){
            int t = getTotalAttendance(code, session);
            return (double) t/n;
        }
        return 0;
    }

    public int getNoOfAssignedLecturers(String code, String session){
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query( COURSE_ASSIGNMENT_TABLE, null,  where, new String[]{code, session}, null, null, null, null);
        int n = query.getCount();
        query.close();
        return n;
    }

    public Lecturer getCoordinator(String code, String session){
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query( COURSE_COORDINATOR_TABLE, null,  where, new String[]{code, session}, null, null, null, null);
        if(query.moveToNext()){
            return getLecturer(gS(query, STAFF_ID));
        }
        return null;
    }

    public List<Lecturer> getOtherLecturers(String code, String session){
        List<Lecturer> list = new ArrayList<>();
        String where = CODE + " = ? AND " + SESSION + " = ?";
        final Cursor query = getReadableDatabase().query( COURSE_ASSIGNMENT_TABLE, null,  where, new String[]{code, session}, null, null, null, null);
        while(query.moveToNext()){
            list.add(getLecturer(gS(query, STAFF_ID)));
        }
        return list;
    }


    public List<Department> getDepartments(){
        List<Department> list = new ArrayList<>();
        final Cursor query = getReadableDatabase().query( DEPARTMENT_TABLE, null,  null, null, null, null, null, null);
        while(query.moveToNext()){
            list.add(
                    new Department(gS(query, CODE), gS(query, NAME))
            );
        }
        query.close();
        return list;
    }

    public Lecturer login(String staffId, String password){
        String where = STAFF_ID + " = ? AND " + PASSWORD + " = ?";
        final Cursor query = getReadableDatabase().query(LECTURER_TABLE, null, where, new String[]{staffId, password}, null, null, null);
        if (query.moveToNext()) {
            return new Lecturer(
                    gS(query, STAFF_ID),
                    gS(query, SURNAME),
                    gS(query, FIRST_NAME),
                    gS(query, OTHER_NAMES),
                    gS(query, DESIGNATION),
                    gS(query, DISCIPLINE),
                    gS(query, RELATED_FIELDS),
                    gS(query, DEPARTMENT),
                    gS(query, ROLE),
                    ""
            );
        }
        return null;
    }

    public Student getStudentByCode(String regNum){
        Student list = null;
        String where = REG_NUMBER + " = ?";
        final Cursor query = getReadableDatabase().query( STUDENT_TABLE, null,  where, new String[]{regNum}, null, null, null, null);
        if(query.moveToNext()){
            list = new Student(
                    gS(query, REG_NUMBER),
                    gS(query, SURNAME),
                    gS(query, FIRST_NAME),
                    gS(query, OTHER_NAMES),
                    gS(query, DEPARTMENT),
                    gS(query, BLUETOOTH_ID)
            );
        }
        return list;
    }



    private String gS(Cursor cus, String s){
        return cus.getString(cus.getColumnIndex(s));
    }
    private int gI(Cursor cus, String s){
        return cus.getInt(cus.getColumnIndex(s));
    }
    public void showToast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
