package com.spark.demo;

import java.io.Serializable;

public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3698257951600662434L;

	private String Cno;
	private String Cname;
	private String Tno;

	public Course() {

	}

	public Course(String Cno, String Cname, String Tno) {
		this.Cno = Cno;
		this.Cname = Cname;
		this.Tno = Tno;
	}

	public String getCno() {
		return Cno;
	}

	public void setCno(String cno) {
		Cno = cno;
	}

	public String getCname() {
		return Cname;
	}

	public void setCname(String cname) {
		Cname = cname;
	}

	public String getTno() {
		return Tno;
	}

	public void setTno(String tno) {
		Tno = tno;
	}

	@Override
	public String toString() {
		return "Course [Cno=" + Cno + ", Cname=" + Cname + ", Tno=" + Tno + "]";
	}

}
