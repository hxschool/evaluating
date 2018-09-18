package com.greathiit.evaluating.servlet.vo;

public class Student {
	private String studentNumber;
	private String name;
	private String birthday;
	private String gender;
	private String nation;
	private String political;
	private String phone;
	private String address;
	private String mail;
	private String longGoal;
	private String midGoal;
	private String shortGoal;
	private String selfEngIntroduce;
	private String selfIntroduce;
	private String edu;
	private String nativePlace;
	private String studentLength;
	private String classno;
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPolitical() {
		return political;
	}
	public void setPolitical(String political) {
		this.political = political;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getLongGoal() {
		return longGoal;
	}
	public void setLongGoal(String longGoal) {
		this.longGoal = longGoal;
	}
	public String getMidGoal() {
		return midGoal;
	}
	public void setMidGoal(String midGoal) {
		this.midGoal = midGoal;
	}
	public String getShortGoal() {
		return shortGoal;
	}
	public void setShortGoal(String shortGoal) {
		this.shortGoal = shortGoal;
	}
	public String getSelfEngIntroduce() {
		return selfEngIntroduce;
	}
	public void setSelfEngIntroduce(String selfEngIntroduce) {
		this.selfEngIntroduce = selfEngIntroduce;
	}
	public String getSelfIntroduce() {
		return selfIntroduce;
	}
	public void setSelfIntroduce(String selfIntroduce) {
		this.selfIntroduce = selfIntroduce;
	}
	public String getEdu() {
		return edu;
	}
	public void setEdu(String edu) {
		this.edu = edu;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getStudentLength() {
		return studentLength;
	}
	public void setStudentLength(String studentLength) {
		this.studentLength = studentLength;
	}
	
	public String getClassno() {
		return classno;
	}
	public void setClassno(String classno) {
		this.classno = classno;
	}
	@Override
	public String toString() {
		return "Student [studentNumber=" + studentNumber + ", name=" + name + ", birthday=" + birthday + ", gender="
				+ gender + ", nation=" + nation + ", political=" + political + ", phone=" + phone + ", address="
				+ address + ", mail=" + mail + ", longGoal=" + longGoal + ", midGoal=" + midGoal + ", shortGoal="
				+ shortGoal + ", selfEngIntroduce=" + selfEngIntroduce + ", selfIntroduce=" + selfIntroduce + ", edu="
				+ edu + ", nativePlace=" + nativePlace + ", studentLength=" + studentLength + "]";
	}

}
