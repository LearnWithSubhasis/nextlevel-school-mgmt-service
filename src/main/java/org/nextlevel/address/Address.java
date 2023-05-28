package org.nextlevel.address;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    private String address;

    private String county;

    private float latitude;
    private float longitude;

    @Column(name = "zip_code")
    private int zipCode;

//    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "address")
//    @JoinColumn(name = "address_id")
//    @JsonIgnore
//    private Organisation organisation;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

//    public Organisation getOrganisation() {
//        return organisation;
//    }
//
//    public void setOrganisation(Organisation organisation) {
//        this.organisation = organisation;
//    }

//    public School getSchool() {
//        return school;
//    }
//
//    public void setSchool(School school) {
//        this.school = school;
//    }

//    @OneToOne(optional = true, fetch = FetchType.LAZY, mappedBy = "address")
//    @JoinColumn(name = "addressId")
//    @JsonIgnore
//    private School school;

    public Long getId() {
        return addressId;
    }

    public void setId(Long id) {
        this.addressId = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}
