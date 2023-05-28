package org.nextlevel.org;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class OrgModel extends RepresentationModel<OrgModel> {
    private Long orgId;
    private String name;
    private String county;
//    private List<School> schools;
//    private List<Event> events;
    private String orgAdminEmail;
    private String address;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
    private int totalSchools;
    private String streetNameOrArea;
    private String district;
    private String city;
    private String country;
    private Long contactNumber = 0L;
    private Integer pinCode;
    private String profileImageURL;


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

//    public List<School> getSchools() {
//        return schools;
//    }
//
//    public void setSchools(List<School> schools) {
//        this.schools = schools;
//    }
//
//    public List<Event> getEvents() {
//        return events;
//    }
//
//    public void setEvents(List<Event> events) {
//        this.events = events;
//    }

    public String getOrgAdminEmail() {
        return orgAdminEmail;
    }

    public void setOrgAdminEmail(String orgAdminEmail) {
        this.orgAdminEmail = orgAdminEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getTotalSchools() {
        return totalSchools;
    }

    public void setTotalSchools(int totalSchools) {
        this.totalSchools = totalSchools;
    }

    public String getStreetNameOrArea() {
        return streetNameOrArea;
    }

    public void setStreetNameOrArea(String streetNameOrArea) {
        this.streetNameOrArea = streetNameOrArea;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
