package org.nextlevel.user;

import java.util.stream.Stream;

public enum RoleType {
    ANY_PUBLIC_USER("Public User"), // For participation in events
    NEXTLEVEL_SUPER_ADMIN("NextLevel Super Administrator"),
    NEXTLEVEL_USER("NextLevel User"),

    //GOVT_GLOBAL_ADMIN ("Global School Administrator"), // For Govt/Org level School Administrator
    //GOVT_GLOBAL_USER ("Global School Viewer"), // For Govt/Org level School User

    NGO_GLOBAL_ADMIN("NGO Global Administrator"), // NGO Admin - For one or more schools
    NGO_GLOBAL_USER("NGO Regular User"), // NGO Viewer - For one or more schools

    ORG_ADMIN("Organisation Administrator"), // For Govt/Org level School Administrator
    SCHOOL_ADMIN("School Administrator"), // School Level Administrator
    GRADE_ADMIN("Grade Administrator"),
    SECTION_ADMIN("Section Administrator"),

    GLOBAL_EVENT_ADMIN("Global Event Administrator"),
    ORG_EVENT_ADMIN("Organisation Event Administrator"),
    SCHOOL_EVENT_ADMIN("School Event Administrator"),
    GRADE_EVENT_ADMIN("Grade Event Administrator"),
    SECTION_EVENT_ADMIN("Section Event Administrator"),

    SCHOOL_PRINCIPAL_ADMIN("Principal"), //SCHOOL_ADMIN + SCHOOL_EVENT_ADMIN + SCHOOL_TEACHER + SCHOOL_FINANCE_ADMIN + SCHOOL_SPORTS_ADMIN
    SCHOOL_TEACHER_ADMIN("Teacher"),
    SCHOOL_FINANCE_ADMIN("School Finance Administrator"),
    SCHOOL_SPORTS_ADMIN("School Sports Administrator"),

    STUDENT_USER("Student"),
    PARENT_USER("Parent"),
    ;

    private final String roleType;

    RoleType(String roleType) {
        this.roleType = roleType;
    }

    public static Stream<RoleType> stream() {
        return Stream.of(RoleType.values());
    }

    public RoleType getRoleType() {
        return this;
    }

    public String getRoleName() {
        return this.name();
    }

    public String getRoleTitle() {
        return this.roleType;
    }
}
