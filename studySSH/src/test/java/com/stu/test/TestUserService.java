package com.stu.test;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stu.domain.Customer;
import com.stu.service.ICustomerService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xmlConfig/spring-context.xml"})
public class TestUserService {

	private static final Logger LOGGER = Logger.getLogger(TestUserService.class);

	 @Autowired
	 public ICustomerService customerService;
	
	 @Test
	 public void getCustomerTest() {
		 Customer  customer=customerService.getCustomer("4028a29f4de14616014de1461ab40000");
		 System.out.println(customer.getCustName());
		 LOGGER.info("ddddddddddd");
	 }
	 
	@Test
	public void saveCustomerTest() {
		Customer  customer=new Customer();
		customer.setCustName("jinxx");
		customerService.addorUpdateCustomer(customer);
		LOGGER.info("ddddddddddd");
	}
	
	@Test
	public void updateCustomerTest() {
		Customer  customer=customerService.getCustomer("4028a29f4de14616014de1461ab40000");
		customer.setCustName("xx");
		customerService.addorUpdateCustomer(customer);
	}

}