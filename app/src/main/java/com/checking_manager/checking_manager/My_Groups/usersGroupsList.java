package com.checking_manager.checking_manager.My_Groups;

public class usersGroupsList {
    private String groupName;
    private String groupStatus;

    public usersGroupsList(String groupName, String groupStatus) {
        this.groupName = groupName;
        this.groupStatus = groupStatus;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }
}
