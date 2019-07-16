package com.example.demo;

import com.example.demo.vo.Customer;
import com.example.demo.vo.ManagementPointInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class API {


    @Autowired
    DemoService demoService;

    @RequestMapping(value = "/topCustomersPerYear")
    public List<Customer> getAccontInfoList(){
        return demoService.getTopCustomersPerYear();
    }
    @RequestMapping(value = "/customersWithNoDeal")
    public List<Customer> getCustomersWithNoDeal(){
        return demoService.getCustomersWithNoDeal();
    }
    @RequestMapping(value = "/bigstSumAmtPerManagementInfo")
    public List<Map<String, Object>>  getBigstSumAmtPerManagementInfo(){
        return demoService.getBigstSumAmtPerManagementInfo();
    }



    @RequestMapping(value = "/managementInfo/{code}")
    public ResponseEntity getManagementInfo(@PathVariable(required = false) String code){
        ManagementPointInfo info = demoService.getManagementInfo(code);
        if(info == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{“code”:”404”,\n" +
                    "“메세지”:”br code not found error”}");
        }else{
            return ResponseEntity.ok(info);
        }
    }


}
