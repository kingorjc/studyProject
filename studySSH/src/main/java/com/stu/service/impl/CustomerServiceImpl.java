package com.stu.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stu.dao.CustomerDao;
import com.stu.domain.Customer;
import com.stu.service.ICustomerService;
import com.stu.util.ObjectHelper;

@Service(value="customerService")
@Transactional(rollbackOn=Exception.class)
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private CustomerDao  customerDao;
	
	
	public Customer addorUpdateCustomer(Customer customer) {
		
		if(ObjectHelper.isEmpty(customer.getId())){
			customer=customerDao.saveEntity(customer);
		}else{
			customer=customerDao.updateEntity(customer);
		}
		
		return customer;
	}
	
	
	public Customer getCustomer(String custId) {
		
		return customerDao.getEntity(Customer.class, custId);
	}

}
