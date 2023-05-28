package org.nextlevel.common;

public enum DeleteStatus {
    DeleteSuccess ("Deleted successfully"),
    DeleteFailed ("Delete failed"),
    ;
    private final String status;

    DeleteStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
