package org.nextlevel.bulkload;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.nextlevel.attendance.*;
import org.nextlevel.section.SectionService;
import org.nextlevel.security.oauth.CustomOAuth2User;
import org.nextlevel.student.Student;
import org.nextlevel.student.StudentService;
import org.nextlevel.teacher.Teacher;
import org.nextlevel.teacher.TeacherRepository;
import org.nextlevel.user.User;
import org.nextlevel.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ParseAttendanceSheets {
    @Autowired
    private StudentService studentService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendanceRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TeacherRepository teacherRepo;

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    //private static final String CREDENTIALS_FILE_PATH = "/cred_nextlevel_app_v1.0.json";
    private static final String CREDENTIALS_FILE_PATH = "/nextlevel-test-service-account.json";



//    /**
//     * Prints the names and majors of students in a sample spreadsheet:
//     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
//     */
//    public static void main(String... args) throws IOException, GeneralSecurityException {
//        ParseAttendanceSheets pas = new ParseAttendanceSheets();
//        pas.parseSheet();
//    }
//
//    public List<List<Object>> parseSheet() throws IOException, GeneralSecurityException {
//        final String spreadsheetId = "1fg9aYaA6280yj3f1SX4pTf_D5puALZHiCbLn3eBXCms"; //"1hEsHNUOerAMPz3wpzPYsR9qWK-uiVdVrEvY4ro8oUU0";
//        final String range = "Term 1!C13:CF";
////        final String range = "Term 1!C12:CF";
////        final String range = "Term 3!C12:CF";
//
//        Sheets service = getSheetService();
//        ValueRange response = service.spreadsheets().values()
//                .get(spreadsheetId, range)
//                .execute();
//        List<List<Object>> values = response.getValues();
////        if (values == null || values.isEmpty()) {
////            System.out.println("No data found.");
////        } else {
////            System.out.println("No, Name, P, A");
////            for (List row : values) {
////                try {
//////                    System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(24));
////                    System.out.printf("%s, %s, %s, %s\n", row.get(0), row.get(1), row.get(80), row.get(81));
////                } catch (IndexOutOfBoundsException ex) {
////                    //ex.printStackTrace();
////                }
////            }
////        }
//
//        return values;
//    }
//
    private Sheets getSheetService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials googleCredentials;
        try(InputStream credentialsStream = ParseAttendanceSheets.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            googleCredentials = GoogleCredentials.fromStream(credentialsStream).createScoped(SCOPES);
        }
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service;
    }
//
////    public void createAttendanceRecords(Long orgId, Long schoolId, Long gradeId, Long sectionId
////            , List<List<Object>> attendanceRows) {
////        if (attendanceRows == null || attendanceRows.isEmpty()) {
////            System.out.println("No data found.");
////        } else {
////            List<Student> allStudents = new ArrayList<>();
////
////            System.out.println("No, Name, P, A");
////            for (List row : attendanceRows) {
////                try {
//////                    System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(24));
////                    System.out.printf("%s, %s, %s, %s\n", row.get(0), row.get(1), row.get(80), row.get(81));
////
////                    int rollNo = (row.get(0) != null && row.get(0) != "")
////                            ? parseInteger(row.get(0))
////                            : -1;
////                    String studentName = (String) row.get(1);
////                    if(null == studentName || studentName.trim().length()==0) {
////                        continue;
////                    }
////
////                    int colNo = 2;
////                    Student student = studentService.getByName(studentName, rollNo);
////                    if(null != student) {
////                        System.out.printf("Student exists: %s\n", student.getName());
////                        colNo = 10;
////                    } else {
////                        System.out.printf("Student doesn't exist: %s\n", studentName);
////                        student = new Student();
////
////                        student.setSection(sectionService.get(sectionId));
////                        student.setName(studentName);
////                        student.setRoleNo(rollNo);
////
////                        student.setSex((String) row.get(colNo++));
////                        student.setAge(parseInteger(row.get(colNo++)));
////                        student.setReligion((String) row.get(colNo++));
////                        student.setNationality((String) row.get(colNo++));
////                        student.setOrphanStatus((String) row.get(colNo++));
////                        student.setSocialNeeds((String) row.get(colNo++));
////                        student.setRepeaters((String) row.get(colNo++));
////                        student.setParentName((String) row.get(colNo++));
////
////                        //studentService.save(student);
////                        //allStudents.add(student);
////                    }
////
////                    createAttendanceRecords(student, row, colNo);
////                } catch (IndexOutOfBoundsException ex) {
////                    //ex.printStackTrace();
////                }
////            }
////
//////            for (Student student:
//////                 allStudents) {
//////                createAttendanceRecords(student, row, colNo);
//////            }
////        }
////    }

    private void createAttendanceRecords(Student student, List row, int colNo, List<LocalDate> attendanceDates,
                                         AttendanceRequest ar) {
        List<Attendance> allAttendance = new ArrayList<>();

        Teacher loggedInTeacher = getLoggedInUser();

        for(int indx=10; indx<80; indx++) {
            System.out.printf("%s, %s\n", attendanceDates.get(indx-10), row.get(indx));

            Attendance attendance = new Attendance();
            attendance.setStudent(student);

            AttendanceStatus attendanceStatus = parseAttendanceStatus((String) row.get(indx));
            attendance.setAttendanceStatus(attendanceStatus);
            attendance.setAttendanceDate(attendanceDates.get(indx-10));
            attendance.setAttendanceCollectionDate(LocalDate.now());
            attendance.setTerm(ar.getTerm());
            attendance.setAttendanceCollector(loggedInTeacher);
            attendance.setWeekDayNo( fetchDayOfWeek(attendanceDates.get(indx-10)) );

            allAttendance.add(attendance);
        }

        attendanceRepo.saveAll(allAttendance);
    }

    private int fetchDayOfWeek(LocalDate attendanceDate) {
        return attendanceDate.getDayOfWeek().getValue();
    }

    private AttendanceStatus parseAttendanceStatus(String strAttendanceStatus) {
        if(null == strAttendanceStatus || strAttendanceStatus.trim().length() == 0) {
            return AttendanceStatus.NA;
        }

        if (strAttendanceStatus.equalsIgnoreCase("p")) return AttendanceStatus.P;
        else if (strAttendanceStatus.equalsIgnoreCase("a")) return AttendanceStatus.A;
        else if (strAttendanceStatus.equalsIgnoreCase("h")) return AttendanceStatus.H;
        else return AttendanceStatus.NA;
    }

    private Integer parseInteger(Object objVal) {
        try {
            if (null != objVal) {
                return Integer.parseInt((String) objVal);
            }
        } catch (NumberFormatException ex) {

        }

        return -1;
    }

    public void parseAndMarkHistoricalAttendance(AttendanceRequest ar) throws GeneralSecurityException, IOException {
        try {
            String term = ar.getTerm();

            String yearCell = "Q6";
            String year = getValue(term, yearCell);
            System.out.printf("%s", year);
            System.out.println();

            String datesCell = "K11:CD11";
            List<String> attendanceDatesStr = getValues(term, datesCell);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
            List<LocalDate> attendanceDates = new ArrayList<>();
            for (String dateStr :
                    attendanceDatesStr) {
                dateStr = dateStr.trim() + " " + year.trim();
                //convert String to LocalDate
                LocalDate localDate = LocalDate.parse(dateStr, formatter);

//            System.out.printf("%s", dateStr);
//            System.out.println();
//            System.out.printf("%s", localDate.toString());
//            System.out.println();

                attendanceDates.add(localDate);
            }

            List<List<Object>> attendanceRows = parseAttendanceRecordsForStudents(ar);
            createAttendanceRecords(attendanceRows, attendanceDates, ar);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void createAttendanceRecords(List<List<Object>> attendanceRows, List<LocalDate> attendanceDates, AttendanceRequest ar) {
        if (attendanceRows == null || attendanceRows.isEmpty()) {
            System.out.println("No data found.");
        } else {
            List<Student> allStudents = new ArrayList<>();

            System.out.println("No, Name, P, A");
            for (List row : attendanceRows) {
                try {
//                    System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(24));
                    System.out.printf("%s, %s, %s, %s\n", row.get(0), row.get(1), row.get(80), row.get(81));

                    int rollNo = (row.get(0) != null && row.get(0) != "")
                            ? parseInteger(row.get(0))
                            : -1;
                    String studentName = (String) row.get(1);
                    if(null == studentName || studentName.trim().length()==0) {
                        continue;
                    }

                    int colNo = 2;
                    Student student = studentService.getByName(studentName, rollNo, ar);
                    if(null != student) {
                        System.out.printf("Student exists: %s\n", student.getName());
                        colNo = 10;
                    } else {
                        System.out.printf("Student doesn't exist: %s\n", studentName);
                        student = new Student();

                        student.setSection(sectionService.get(ar.getSectionId()));
                        student.setName(studentName);
                        student.setRoleNo(rollNo);

                        student.setSex((String) row.get(colNo++));
                        student.setAge(parseInteger(row.get(colNo++)));
                        student.setReligion((String) row.get(colNo++));
                        student.setNationality((String) row.get(colNo++));
                        student.setOrphanStatus((String) row.get(colNo++));
                        student.setSocialNeeds((String) row.get(colNo++));
                        student.setRepeaters((String) row.get(colNo++));
                        student.setParentName((String) row.get(colNo++));

                        studentService.save(student);
                        //allStudents.add(student);
                    }

                    createAttendanceRecords(student, row, colNo, attendanceDates, ar);
                } catch (IndexOutOfBoundsException ex) {
                    //ex.printStackTrace();
                }
            }
        }



//        if (attendanceRows == null || attendanceRows.isEmpty()) {
//            System.out.println("No data found.");
//        } else {
//            System.out.println("No, Name, P, A");
//            for (List row : attendanceRows) {
//                try {
////                    System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(24));
//                    //System.out.printf("%s, %s, %s, %s\n", row.get(0), row.get(1), row.get(80), row.get(81));
//
//                    int rollNo = (row.get(0) != null && row.get(0) != "")
//                            ? parseInteger(row.get(0))
//                            : -1;
//                    String studentName = (String) row.get(1);
//                    if(null == studentName || studentName.trim().length()==0) {
//                        continue;
//                    }
//
//                    //row.get(10)...row.get(79)
//                    System.out.println(studentName);
//                    System.out.println("=====================");
//                    for(int indx=10; indx<80; indx++) {
//                        System.out.printf("%s, %s\n", attendanceDates.get(indx-10), row.get(indx));
//                        //System.out.println();
//                    }
//
////                    int colNo = 2;
////
////
////                    try {
////                        while(true) {
////                            String attendanceStatusStr = (String) row.get(colNo++);
////                            try {
////                                int val = Integer.parseInt(attendanceStatusStr);
////                                if (val >= 0) {
////                                    continue;
////                                }
////                            } catch (NumberFormatException ex) {}
////                        }
////                    } catch (Exception ex) {
////                        //ex.printStackTrace();
////                    }
//                } catch (IndexOutOfBoundsException ex) {
//                    //ex.printStackTrace();
//                }
//            }
//
////            for (Student student:
////                 allStudents) {
////                createAttendanceRecords(student, row, colNo);
////            }
//        }
    }

    public List<List<Object>> parseAttendanceRecordsForStudents(AttendanceRequest ar) throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1fg9aYaA6280yj3f1SX4pTf_D5puALZHiCbLn3eBXCms"; //"1hEsHNUOerAMPz3wpzPYsR9qWK-uiVdVrEvY4ro8oUU0";
        final String range = ar.getTerm() + "!C13:CF";

        Sheets service = getSheetService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();

        return values;
    }
    private String getValue(String term, String cell) throws GeneralSecurityException, IOException {
        List<List<Object>> attendanceRows = parseAttendanceRecords(term, cell);
        String value = fetchAttendanceForYear(attendanceRows);
        return value;
    }

    private List<String> getValues(String term, String cell) throws GeneralSecurityException, IOException {
        List<List<Object>> attendanceRows = parseAttendanceRecords(term, cell);
        List<String> values = fetchAttendanceDates(attendanceRows);
        return values;
    }

    private List<String> fetchAttendanceDates(List<List<Object>> attendanceRows) {
        List<String> attendanceDates = new ArrayList<>();
        if (attendanceRows == null || attendanceRows.isEmpty()) {
            System.out.println("No data found.");
        } else {
            //System.out.println("No, Name, P, A");
            for (List row : attendanceRows) {
                try {
                    int colNo = 2;
                    //createAttendanceRecords(row, colNo);

                    try {
                        while(true) {
                            String attendanceStatusStr = (String) row.get(colNo++);
                            try {
                                int val = Integer.parseInt(attendanceStatusStr);
                                if (val >= 0) {
                                    continue;
                                }
                            } catch (NumberFormatException ex) {}

                            attendanceDates.add(attendanceStatusStr);
                        }
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                    }
                } catch (IndexOutOfBoundsException ex) {
                    //ex.printStackTrace();
                }

                break;
            }
        }

        return attendanceDates;
    }

    public List<List<Object>> parseAttendanceRecords(String term, String cell) throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1fg9aYaA6280yj3f1SX4pTf_D5puALZHiCbLn3eBXCms"; //"1hEsHNUOerAMPz3wpzPYsR9qWK-uiVdVrEvY4ro8oUU0";
        final String year = term + "!" + cell;

        Sheets service = getSheetService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, year)
                .execute();
        List<List<Object>> values = response.getValues();

        return values;
    }

    public String fetchAttendanceForYear(List<List<Object>> attendanceRows) {
        if (attendanceRows == null || attendanceRows.isEmpty()) {
            System.out.println("No data found.");
        } else {
            //System.out.println("No, Name, P, A");
            for (List row : attendanceRows) {
                try {
                    String year = (String) row.get(0);
                    return year;
                } catch (IndexOutOfBoundsException ex) {
                    //ex.printStackTrace();
                }
            }
        }

        return "";
    }

    private Teacher getLoggedInUser() {
        SecurityContext secureContext = SecurityContextHolder.getContext();
        Authentication auth = secureContext.getAuthentication();
        Object principal = auth.getPrincipal();

        String userName = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            userName = userDetails.getUsername();
        } else if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User userDetails = (CustomOAuth2User) principal;
            userName = userDetails.getEmail();
        } else {
            userName = principal.toString();
        }

        Teacher loggedInTeacher = resolveLoggedInUserId(userName);

        return loggedInTeacher;
    }

    private Teacher resolveLoggedInUserId(String userEmail) {
        User user = userRepo.findByEmail(userEmail);
        Teacher teacher = null;
        if (null != user.getTeacher()) {
            teacher = teacherRepo.findById(user.getTeacher().getTeacherId()).get();
        }

        return teacher;
    }
}
