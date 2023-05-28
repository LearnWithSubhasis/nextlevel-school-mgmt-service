package org.nextlevel.school;

import org.nextlevel.org.Organisation;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class SchoolModel extends RepresentationModel<SchoolModel> {
    private Long schoolId;
    private String name;
    private String county;
    private Organisation organisation;
    private String address;
    private String schoolAdminEmail;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
    private String streetNameOrArea;
    private String district;
    private String city;
    private String country;
    private Long contactNumber;
    private Integer pinCode;
    private String profileImageURL;



    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
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

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchoolAdminEmail() {
        return schoolAdminEmail;
    }

    public void setSchoolAdminEmail(String schoolAdminEmail) {
        this.schoolAdminEmail = schoolAdminEmail;
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
