package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by HP on 2/17/2018.
 */

//Entity and table in sql are same
@Entity
public class BasicInfo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    public String driverUid;

    public String vehicle_manufacturer, vehicle_model,
            vehicle_registration_authority, vehicle_fitness_number, vehicle_registration_number,
            vehicle_tax_token_number, email, full_name, home_address, driving_license, nid,
            registration_city, vehicle_type, driverOrBiker, mobile, serviceType;

    public String vehicle_production_year;


    public BasicInfo(@NonNull String driverUid, String vehicle_manufacturer, String vehicle_model,
                     String vehicle_production_year, String vehicle_registration_authority,
                     String vehicle_fitness_number, String vehicle_registration_number,
                     String vehicle_tax_token_number, String email, String full_name,
                     String home_address, String driving_license, String nid,
                     String registration_city, String vehicle_type,
                     String mobile, String driverOrBiker, String serviceType) {
        this.driverUid = driverUid;
        this.full_name = full_name;
        this.email = email;
        this.mobile = mobile;
        this.nid = nid;
        this.driving_license = driving_license;
        this.home_address = home_address;
        this.registration_city = registration_city;
        this.vehicle_type = vehicle_type;
        this.vehicle_manufacturer = vehicle_manufacturer;
        this.vehicle_model = vehicle_model;
        this.vehicle_production_year = vehicle_production_year;
        this.vehicle_registration_authority = vehicle_registration_authority;
        this.vehicle_registration_number = vehicle_registration_number;
        this.vehicle_tax_token_number = vehicle_tax_token_number;
        this.vehicle_fitness_number = vehicle_fitness_number;
        this.driverOrBiker = driverOrBiker;
        this.serviceType = serviceType;
    }

    public String getVehicle_production_year() {
        return vehicle_production_year;
    }

    public void setVehicle_production_year(String vehicle_production_year) {
        this.vehicle_production_year = vehicle_production_year;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BasicInfo() {

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDriverOrBiker() {
        return driverOrBiker;
    }

    public void setDriverOrBiker(String driverOrBiker) {
        this.driverOrBiker = driverOrBiker;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public String getVehicle_manufacturer() {
        return vehicle_manufacturer;
    }

    public void setVehicle_manufacturer(String vehicle_manufacturer) {
        this.vehicle_manufacturer = vehicle_manufacturer;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }


    public String getVehicle_registration_authority() {
        return vehicle_registration_authority;
    }

    public void setVehicle_registration_authority(String vehicle_registration_authority) {
        this.vehicle_registration_authority = vehicle_registration_authority;
    }

    public String getVehicle_fitness_number() {
        return vehicle_fitness_number;
    }

    public void setVehicle_fitness_number(String vehicle_fitness_number) {
        this.vehicle_fitness_number = vehicle_fitness_number;
    }

    public String getVehicle_registration_number() {
        return vehicle_registration_number;
    }

    public void setVehicle_registration_number(String vehicle_registration_number) {
        this.vehicle_registration_number = vehicle_registration_number;
    }

    public String getVehicle_tax_token_number() {
        return vehicle_tax_token_number;
    }

    public void setVehicle_tax_token_number(String vehicle_tax_token_number) {
        this.vehicle_tax_token_number = vehicle_tax_token_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getDriving_license() {
        return driving_license;
    }

    public void setDriving_license(String driving_license) {
        this.driving_license = driving_license;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getRegistration_city() {
        return registration_city;
    }

    public void setRegistration_city(String registration_city) {
        this.registration_city = registration_city;
    }


}
