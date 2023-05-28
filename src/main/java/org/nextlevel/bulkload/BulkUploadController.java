package org.nextlevel.bulkload;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.nextlevel.grade.Grade;
import org.nextlevel.grade.GradeRepository;
import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationRepository;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolRepository;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.Section;
import org.nextlevel.section.SectionRepository;
import org.nextlevel.student.Student;
import org.nextlevel.student.StudentRepository;
import org.nextlevel.student.StudentService;
import org.nextlevel.teacher.Teacher;
import org.nextlevel.teacher.TeacherRepository;
import org.nextlevel.teacher.TeacherService;
import org.nextlevel.user.User;
import org.nextlevel.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class BulkUploadController {

    @Autowired
    SchoolRepository schoolRepo;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    OrganisationRepository orgRepo;
    @Autowired
    GradeRepository gradeRepo;
    @Autowired
    SectionRepository sectionRepo;
    @Autowired
    StudentRepository studentRepo;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherRepository teacherRepo;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UserService userService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    public static final Logger LOG = LoggerFactory.getLogger(BulkUploadController.class);

    @GetMapping("/uploadSchoolsCSV") public String displayUploadSchoolForm(@RequestParam (name = "orgId") String orgId) {
        //return "schools";
        return "redirect:/school/list?orgId=" + String.valueOf(orgId);
    }

    @PostMapping("/uploadSchools") public String uploadSchoolsCSV(Model model, @RequestParam("csv") MultipartFile file,
            @RequestParam (name = "orgId") String orgId) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes(), StandardOpenOption.CREATE);
        model.addAttribute("msg", "Uploaded CSVs: " + fileNames.toString());

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            uploadSchoolFile(model, fileNameAndPath.toFile(), Long.parseLong(orgId));
        }

        //return "schools";
        return "redirect:/listSchools/" + String.valueOf(orgId);
    }

    private void uploadSchoolFile(Model model, File file, long orgId) {
        // parse CSV file to create a list of `School` objects
        CSVReader reader = getCSVReader(file);

        try {
            // Hashmap to map CSV data to Bean attributes
            Map<String, String> mapping = new HashMap<String, String>();
            mapping.put("School Name", "name");
            mapping.put("County", "county");

            // HeaderColumnNameTranslateMappingStrategy
            // for School class
            HeaderColumnNameTranslateMappingStrategy<School> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<School>();
            strategy.setType(School.class);
            strategy.setColumnMapping(mapping);

            // create csv bean reader
            //reader = getCSVReader(file);
            CsvToBean<School> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(School.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withSeparator(',')
                    .build();

            // convert `CsvToBean` object to list of schools
            List<School> schools = csvToBean.parse();
            for (School school: schools) {
                school.setOrganisation(orgRepo.getOne(orgId));
                System.out.println(school.getName()+ ","+ school.getCounty());
            }

            // TODO: save schools in DB?
            schoolRepo.saveAll(schools);

            // save users list on model
            model.addAttribute("schools", schools);
            model.addAttribute("status", true);

        } catch (Exception ex) {
            model.addAttribute("message", "An error occurred while processing the CSV file (Schools).");
            model.addAttribute("status", false);
        }
    }

    private CSVReader getCSVReader(File file) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reader;
    }


    @GetMapping("/uploadGradesCSV") public String displayUploadGradeForm(@RequestParam (name = "orgId") String orgId
        , @RequestParam (name = "schoolId") String schoolId) {
        //return "schools";
        return "redirect:/grade/list?orgId=" + String.valueOf(orgId) + "&schoolId=" + String.valueOf(schoolId);
    }

    @PostMapping("/uploadGrades") public String uploadGradesCSV(Model model, @RequestParam("csv") MultipartFile file
            , @RequestParam (name = "orgId") Long orgId
            , @RequestParam (name = "schoolId") Long schoolId) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes(), StandardOpenOption.CREATE);
        model.addAttribute("msg", "Uploaded CSVs (Grades): " + fileNames.toString());

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload (Grades).");
            model.addAttribute("status", false);
        } else {
            uploadGradeFile(model, fileNameAndPath.toFile(), orgId, schoolId);
        }

        return "redirect:/school/listGrades/" + String.valueOf(schoolId) + "?orgId=" + String.valueOf(orgId);
    }

    private void uploadGradeFile(Model model, File file, long orgId, long schoolId) {
        // parse CSV file to create a list of `Grade` objects
        CSVReader reader = getCSVReader(file);

        try {
            // Hashmap to map CSV data to Bean attributes
            Map<String, String> mapping = new HashMap<String, String>();
            mapping.put("Grade Name", "name");

            // HeaderColumnNameTranslateMappingStrategy
            // for Grade class
            HeaderColumnNameTranslateMappingStrategy<Grade> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Grade.class);
            strategy.setColumnMapping(mapping);

            // create csv bean reader
            CsvToBean<Grade> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Grade.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withSeparator(',')
                    .build();

            // convert `CsvToBean` object to list of schools
            List<Grade> grades = csvToBean.parse();
            for (Grade grade: grades) {
                grade.setSchool(schoolRepo.getOne(schoolId));
                System.out.println(grade.getName());
            }

            // TODO: save grades in DB?
            gradeRepo.saveAll(grades);

            // save users list on model
            model.addAttribute("grades", grades);
            model.addAttribute("status", true);

        } catch (Exception ex) {
            model.addAttribute("message", "An error occurred while processing the CSV file (Grades).");
            model.addAttribute("status", false);
        }
    }

    @PostMapping("/uploadStudents") public String uploadStudentsCSV(Model model, @RequestParam("csv") MultipartFile file
            , @RequestParam (name = "orgId") Long orgId
            , @RequestParam (name = "schoolId") Long schoolId
            , @RequestParam (name = "gradeId") Long gradeId
            , @RequestParam (name = "sectionId") Long sectionId) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes(), StandardOpenOption.CREATE);
        model.addAttribute("msg", "Uploaded CSVs (Students): " + fileNames.toString());

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload (Students).");
            model.addAttribute("status", false);
        } else {
            uploadStudentFile(model, fileNameAndPath.toFile(), orgId, schoolId, gradeId, sectionId);
        }

        return "redirect:/section/listStudents/" + String.valueOf(sectionId) + "?orgId=" + String.valueOf(orgId)
                + "&schoolId=" + schoolId + "&gradeId=" + gradeId;
    }

    private void uploadStudentFile(Model model, File file, long orgId, long schoolId, long gradeId, long sectionId) {
        // parse CSV file to create a list of `Student` objects
        CSVReader reader = getCSVReader(file);

        try {
            // Hashmap to map CSV data to Bean attributes
            Map<String, String> mapping = new HashMap<String, String>();
            mapping.put("Student Name", "name");
            mapping.put("Sex", "sex");
            mapping.put("Role No", "roleNo");

            // HeaderColumnNameTranslateMappingStrategy
            // for Grade class
            HeaderColumnNameTranslateMappingStrategy<Student> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Student.class);
            strategy.setColumnMapping(mapping);

            // create csv bean reader
            CsvToBean<Student> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Student.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withSeparator(',')
                    .build();

            // convert `CsvToBean` object to list of students
            List<Student> students = csvToBean.parse();
            for (Student student: students) {
                student.setSection(sectionRepo.getOne(sectionId));
                System.out.println(student.getName());
            }

            // TODO: save students in DB?
            studentRepo.saveAll(students);

            // save users list on model
            model.addAttribute("students", students);
            model.addAttribute("status", true);

        } catch (Exception ex) {
            model.addAttribute("message", "An error occurred while processing the CSV file (Students).");
            model.addAttribute("status", false);
        }
    }



    //TODO: Bulk upload APIs

    @RequestMapping(value = "/api/v1/orgs/{orgId}/schools/bulkupload", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> bulkUploadSchools(@RequestParam("csv") MultipartFile file, @PathVariable("orgId") Long orgId) {
        try {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes(), StandardOpenOption.CREATE);

            // validate file
            if (file.isEmpty()) {
                LOG.error("Please select a CSV file to upload.");
            } else {
                uploadSchoolFile(fileNameAndPath.toFile(), orgId);
            }
        } catch (Exception ex){
            LOG.error(ex.getMessage());
        }
        String message = "Successfully uploaded file (schools - bulk upload): " + file.getOriginalFilename();
        LOG.info(message);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    private void uploadSchoolFile(File file, long orgId) {
        // parse CSV file to create a list of `School` objects
        CSVReader reader = getCSVReader(file);
        Organisation org = orgRepo.getOne(orgId);

        try {
            // Hashmap to map CSV data to Bean attributes
            Map<String, String> mapping = new HashMap<String, String>();
            mapping.put("School Name", "name");
            mapping.put("County", "county");
            mapping.put("School Admin Email", "schoolAdminEmail");

            // HeaderColumnNameTranslateMappingStrategy
            // for School class
            HeaderColumnNameTranslateMappingStrategy<School> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<School>();
            strategy.setType(School.class);
            strategy.setColumnMapping(mapping);

            // create csv bean reader
            //reader = getCSVReader(file);
            CsvToBean<School> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(School.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withSeparator(',')
                    .build();

            // convert `CsvToBean` object to list of schools
            List<School> schools = csvToBean.parse();
            for (School school: schools) {
                school.setOrganisation(org);

                LocalDateTime timeNow = LocalDateTime.now();
                school.setDateCreated(timeNow);
                school.setLastUpdated(timeNow);

                System.out.println(school.getName()+ ","+ school.getCounty()+ ","+ school.getSchoolAdminEmail());
            }

            // TODO: save schools in DB?
            schoolRepo.saveAll(schools);

            for (School school:
                 schools) {
                schoolService.registerSchoolAdmin(school);
            }

        } catch (Exception ex) {
            LOG.error(ex.getMessage());;
        }
    }

    @RequestMapping(value = "/api/v1/sections/{sectionId}/students/bulkupload", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> bulkUploadStudents(@RequestParam("csv") MultipartFile file, @PathVariable("sectionId") Long sectionId) {
        try {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes(), StandardOpenOption.CREATE);

            // validate file
            if (file.isEmpty()) {
                LOG.error("Please select a CSV file to upload.");
            } else {
                uploadStudentFile(fileNameAndPath.toFile(), sectionId);
            }
        } catch (Exception ex){
            LOG.error(ex.getMessage());
        }
        String message = "Successfully uploaded file (students - bulk upload): " + file.getOriginalFilename();
        LOG.info(message);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    private void uploadStudentFile(File file, long sectionId) {
        // parse CSV file to create a list of `Student` objects
        CSVReader reader = getCSVReader(file);
        Section section = sectionRepo.getOne(sectionId);

        try {
            // Hashmap to map CSV data to Bean attributes
            Map<String, String> mapping = new HashMap<String, String>();
            mapping.put("Student Name", "name");
            mapping.put("Sex", "sex");
            mapping.put("Role No", "roleNo");
            mapping.put("Age", "age");
            mapping.put("Religion", "religion");
            mapping.put("Nationality", "nationality");
            mapping.put("Orphan Status", "orphanStatus");
            mapping.put("Social Needs", "socialNeeds");
            mapping.put("Repeaters", "repeaters");
            mapping.put("Parent Name", "parentName");
            mapping.put("Parent Email", "parentEmail");
            mapping.put("Parent Phone No", "parentContactNo");

            HeaderColumnNameTranslateMappingStrategy<Student> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Student.class);
            strategy.setColumnMapping(mapping);

            // create csv bean reader
            CsvToBean<Student> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Student.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withSeparator(',')
                    .build();

            // convert `CsvToBean` object to list of students
            List<Student> students = csvToBean.parse();
            for (Student student: students) {
                student.setSection(section);
                System.out.println(student.getName());
            }

            // TODO: save students in DB?
            studentRepo.saveAll(students);

            for (Student student:
                    students) {
                studentService.registerParent(student);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }

    //TODO::Teacher bulk upload
    @RequestMapping(value = "/api/v1/schools/{schoolId}/teachers/bulkupload", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<HttpStatus> bulkUploadTeachers(@RequestParam("csv") MultipartFile file, @PathVariable("schoolId") Long schoolId) throws Exception {
        try {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes(), StandardOpenOption.CREATE);

            // validate file
            if (file.isEmpty()) {
                LOG.error("Please select a CSV file to upload.");
            } else {
                uploadTeacherFile(fileNameAndPath.toFile(), schoolId);
            }
        } catch (Exception ex){
            LOG.error(ex.getMessage());
            throw ex;
        }
        String message = "Successfully uploaded file (teachers - bulk upload): " + file.getOriginalFilename();
        LOG.info(message);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    private void uploadTeacherFile(File file, long schoolId) {
        // parse CSV file to create a list of `Student` objects
        CSVReader reader = getCSVReader(file);
        School school = schoolService.get(schoolId);

        try {
            // Hashmap to map CSV data to Bean attributes
            Map<String, String> mapping = new HashMap<String, String>();
            mapping.put("Teacher Name", "name");
            mapping.put("Gender", "gender");
            mapping.put("Email", "teacherEmail");
            mapping.put("Phone No", "teacherContactNo");
            mapping.put("DOB", "dateOfBirth");
            mapping.put("Subject", "subject");
            mapping.put("Qualification", "qualification");
            mapping.put("Employment Type", "employmentType");
            mapping.put("Years of Experience", "yearsOfExperience");

            HeaderColumnNameTranslateMappingStrategy<Teacher> strategy =
                    new HeaderColumnNameTranslateMappingStrategy<>();
            strategy.setType(Teacher.class);
            strategy.setColumnMapping(mapping);

            // create csv bean reader
            CsvToBean<Teacher> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Teacher.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withSeparator(',')
                    .build();

            // convert `CsvToBean` object to list of students
            List<Teacher> teachers = csvToBean.parse();
            for (Teacher teacher: teachers) {
                User user = userService.getOrCreateUser(teacher);
                teacher.setUser(user);
                teacher.setSchool(school);

                LocalDateTime timeNow = LocalDateTime.now();
                teacher.setDateCreated(timeNow);
                teacher.setLastUpdated(timeNow);

                System.out.println(teacher.getName());
            }

            // TODO: save students in DB?
            teacherRepo.saveAll(teachers);

//            for (Teacher teacher:
//                    teachers) {
//                teacherService.registerTeacher(teacher);
//            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw ex;
        }
    }
}