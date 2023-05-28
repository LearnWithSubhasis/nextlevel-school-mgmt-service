package org.nextlevel.teacher;

import org.nextlevel.email.EmailService;
import org.nextlevel.user.RoleRepository;
import org.nextlevel.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;

    public static final Logger LOG = LoggerFactory.getLogger(TeacherService.class);

    public List<Teacher> listAll() {
        return repo.findAll();
    }

    public List<Teacher> listAll(Long orgId, Long schoolId) {
        return repo.findAll()
                .stream()
				.filter(teacher -> teacher.getSchool().getSchoolId() == schoolId)
                .collect(Collectors.toList());
    }

    public void save(Teacher teacher) {
        repo.save(teacher);
    }

    public Teacher get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) { repo.deleteById(id); }

    public Teacher getByName(String name) {
        Teacher teacherSelected = null;
        List<Teacher> teacherList = new ArrayList<>();
        Supplier<Stream<Teacher>> teachersStreamSupp = () -> repo.findAll()
                .stream()
                .filter(teacher -> teacher.getName().equalsIgnoreCase(name));

        teacherList = teachersStreamSupp.get().collect(Collectors.toList());

        if (null != teacherList && teacherList.size() == 1) {
            teacherSelected = teacherList.get(0);
        }

        return teacherSelected;
    }

    public Teacher getByUserId(Long userId) {
        return repo.findByUserId(userId);
    }
}
