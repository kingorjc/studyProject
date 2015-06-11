package com.stu.service;

import com.stu.domain.Customer;

public interface ICustomerService {
	public Customer  addorUpdateCustomer(Customer customer);
	public Customer getCustomer(String custId);
}
