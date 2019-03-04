package com.orbitview.salesmanagement.model;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("date")
	private String date;

	@SerializedName("file_name")
	private String fileName;

	@SerializedName("employee_id")
	private String employeeId;

	@SerializedName("customer_phone")
	private String customerPhone;

	@SerializedName("employee_name")
	private String employeeName;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("location")
	private String location;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("time")
	private String time;

	@SerializedName("email")
	private String email;

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return fileName;
	}

	public void setEmployeeId(String employeeId){
		this.employeeId = employeeId;
	}

	public String getEmployeeId(){
		return employeeId;
	}

	public void setCustomerPhone(String customerPhone){
		this.customerPhone = customerPhone;
	}

	public String getCustomerPhone(){
		return customerPhone;
	}

	public void setEmployeeName(String employeeName){
		this.employeeName = employeeName;
	}

	public String getEmployeeName(){
		return employeeName;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return customerName;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"latitude = '" + latitude + '\'' +
			"longitude = '" + longitude + '\'' +
			"date = '" + date + '\'' +
			",file_name = '" + fileName + '\'' +
			",employee_id = '" + employeeId + '\'' + 
			",customer_phone = '" + customerPhone + '\'' + 
			",employee_name = '" + employeeName + '\'' + 
			",phone_number = '" + phoneNumber + '\'' + 
			",location = '" + location + '\'' + 
			",customer_name = '" + customerName + '\'' + 
			",time = '" + time + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}