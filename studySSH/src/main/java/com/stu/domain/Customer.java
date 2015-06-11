package com.stu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Customer extends BaseEntity{

	private static final long serialVersionUID = 4217639484911340588L;

	
	@Column(name = "CustName", length = 50)
	private String custName;


	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
}
