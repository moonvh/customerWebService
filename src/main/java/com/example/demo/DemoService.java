package com.example.demo;

import com.example.demo.vo.Customer;
import com.example.demo.vo.ManagementPointInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DemoService {
    List<Customer> getTopCustomersPerYear();
    List<Customer> getCustomersWithNoDeal();
    List<Map<String, Object>> getBigstSumAmtPerManagementInfo();

    ManagementPointInfo getManagementInfo(String brCode);

}
