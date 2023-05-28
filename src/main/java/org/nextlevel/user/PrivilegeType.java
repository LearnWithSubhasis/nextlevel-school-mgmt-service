package org.nextlevel.user;

import java.util.stream.Stream;

public enum PrivilegeType {
    READ_PRIVILEGE("Write"),
    WRITE_PRIVILEGE("Read"),
    ;
    private final String privilegeType;

    PrivilegeType(String privilegeType) {
        this.privilegeType = privilegeType;
    }

    public static Stream<PrivilegeType> stream() {
        return Stream.of(PrivilegeType.values());
    }

    public PrivilegeType getPrivilegeType() {
        return this;
    }

    public String getPrivilegeName() {
        return this.privilegeType;
    }
}
