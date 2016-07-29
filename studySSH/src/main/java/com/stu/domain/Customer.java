package com.stu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="T_Customer")
public class Customer extends BaseEntity{

	private static final long serialVersionUID = 4217639484911340588L;

	
	@Column(name = "CustName", length = 50)
	private String custName;
	
	@Column(name = "score",precision=15,scale=2)
	private Double score;
	
	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
