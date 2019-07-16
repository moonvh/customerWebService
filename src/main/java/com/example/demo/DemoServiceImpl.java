package com.example.demo;

import com.example.data.CSVToVoParer;
import com.example.data.SAccontInfo;
import com.example.data.SManagementPointInfo;
import com.example.data.STransactionHistory;
import com.example.demo.vo.Customer;
import com.example.demo.vo.ManagementPointInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Configuration
public class DemoServiceImpl implements DemoService {
    @Autowired
    CSVToVoParer parer;


    @Override
    public List<Customer> getTopCustomersPerYear() throws NumberFormatException {
        List<Customer> customerList = new ArrayList<>();
        HashMap<Integer, Map<String, Integer>> yearMap = new LinkedHashMap<>();
        yearMap.put(2018, null);
        yearMap.put(2019, null);
        for (STransactionHistory history : parer.getSTransactionHistoryList()) {
            if (history.isCancel) {
                continue;
            }
            int year = Integer.parseInt(history.date.substring(0, 4));
            Map<String, Integer> acctNoMap = yearMap.get(year);
            if (acctNoMap == null) {
                acctNoMap = new HashMap<>();
            }

            Integer amt = acctNoMap.get(history.acctNo);
            if (amt == null) {
                amt = 0;
            }
            amt += Integer.parseInt(history.pay);
            acctNoMap.put(history.acctNo, amt);
            yearMap.put(year, acctNoMap);
        }

        for (Map.Entry<Integer, Map<String, Integer>> acctNoEntry : yearMap.entrySet()) {
            int max = Integer.MIN_VALUE;
            String acctNo = "";
            for (Map.Entry<String, Integer> entry : acctNoEntry.getValue().entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    acctNo = entry.getKey();
                }
            }

            for (SAccontInfo info : parer.getSAccountInfoList()) {
                if (info.acctNo.equals(acctNo)) {
                    Customer customer = new Customer();
                    customer.acctNo = acctNo;
                    customer.year = acctNoEntry.getKey();
                    customer.name = info.name;
                    customer.sumAmt = max;
                    customerList.add(customer);
                }
            }
        }

        return customerList;
    }

    @Override
    public List<Customer> getCustomersWithNoDeal() {
        List<Customer> customerList = new ArrayList<>();
        HashMap<Integer, Map<String, Integer>> yearMap = new LinkedHashMap<>();
        yearMap.put(2018, null);
        yearMap.put(2019, null);
        for (STransactionHistory history : parer.getSTransactionHistoryList()) {
            if (history.isCancel) {
                continue;
            }
            int year = Integer.parseInt(history.date.substring(0, 4));
            Map<String, Integer> acctNoMap = yearMap.get(year);
            if (acctNoMap == null) {
                acctNoMap = new HashMap<>();
            }

            Integer amt = acctNoMap.get(history.acctNo);
            if (amt == null) {
                amt = 0;
            }
            amt += Integer.parseInt(history.pay);
            acctNoMap.put(history.acctNo, amt);
            yearMap.put(year, acctNoMap);
        }

        Map<String, Boolean> customerMap = new LinkedHashMap<>();
        for (SAccontInfo info : parer.getSAccountInfoList()) {
            customerMap.put(info.acctNo, false);
        }

        for (Map.Entry<Integer, Map<String, Integer>> acctNoEntry : yearMap.entrySet()) {

            for (Map.Entry<String, Integer> entry : acctNoEntry.getValue().entrySet()) {
                customerMap.put(entry.getKey(), entry.getValue() != null);
            }
            for (SAccontInfo info : parer.getSAccountInfoList()) {

                if (!customerMap.get(info.acctNo)) {
                    Customer customer = new Customer();
                    customer.acctNo = info.acctNo;
                    customer.year = acctNoEntry.getKey();
                    customer.name = info.name;
                    customerList.add(customer);
                }
            }
            for (Map.Entry<String, Boolean> entry : customerMap.entrySet()) {
                entry.setValue(false);
            }
        }

        return customerList;
    }

    @Override
    public List<Map<String, Object>> getBigstSumAmtPerManagementInfo() {

        HashMap<Integer, Map<String, ManagementPointInfo>> yearMap = new LinkedHashMap<>();
        yearMap.put(2018, null);
        yearMap.put(2019, null);

        Map<String, SAccontInfo> mAndAMap = new HashMap<>();
        for (SAccontInfo accontInfo : parer.getSAccountInfoList()) {
            mAndAMap.put(accontInfo.acctNo, accontInfo);
        }

        Map<String, String> pointName = new HashMap<>();
        for (SManagementPointInfo pointInfo : parer.getSManagementpointInfoList()) {
            pointName.put(pointInfo.brCode, pointInfo.brName);
        }

        for (STransactionHistory history : parer.getSTransactionHistoryList()) {
            if (history.isCancel) {
                continue;
            }
            int year = Integer.parseInt(history.date.substring(0, 4));
            Map<String, ManagementPointInfo> acctNoMap = yearMap.get(year);
            if (acctNoMap == null) {
                acctNoMap = new LinkedHashMap<>();
            }

            ManagementPointInfo pointInfo = acctNoMap.get(mAndAMap.get(history.acctNo).code);
            if (pointInfo == null) {
                pointInfo = new ManagementPointInfo();
                pointInfo.sumAmt = 0;
                pointInfo.brName = pointName.get(mAndAMap.get(history.acctNo).code);
                pointInfo.brCode = mAndAMap.get(history.acctNo).code;
            }
            pointInfo.sumAmt += Integer.parseInt(history.pay);
            acctNoMap.put(mAndAMap.get(history.acctNo).code, pointInfo);
            yearMap.put(year, acctNoMap);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, ManagementPointInfo>> acctNoEntry : yearMap.entrySet()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("year", acctNoEntry.getKey());
            List<ManagementPointInfo> list = new ArrayList<>();
            for(Map.Entry<String, ManagementPointInfo> entry :acctNoEntry.getValue().entrySet()){
                list.add(entry.getValue());
            }
            Collections.sort(list, Comparator.comparingInt(o -> o.sumAmt));
            map.put("dataList", list);
            result.add(map);
        }
        return result;
    }


    @Override
    public ManagementPointInfo getManagementInfo(String brCode) {
        if (brCode.equals("A")) {
            brCode = "";
        }

        Map<String, String> mAndAMap = new HashMap<>();
        for (SAccontInfo accontInfo : parer.getSAccountInfoList()) {
            mAndAMap.put(accontInfo.acctNo, accontInfo.code);
        }

        ManagementPointInfo resultInfo = null;
        for (SManagementPointInfo info : parer.getSManagementpointInfoList()) {
            if (brCode.equals(info.brCode)) {
                resultInfo = new ManagementPointInfo();
                resultInfo.brCode = brCode;
                resultInfo.brName = info.brName;
            }
        }
        if (resultInfo == null) {
            return null;
        } else {
            for (STransactionHistory history : parer.getSTransactionHistoryList()) {
                if (history.isCancel) {
                    continue;
                }
                if (mAndAMap.get(history.acctNo).contains(brCode)) {
                    resultInfo.sumAmt += Integer.parseInt(history.pay);
                }
            }
        }

        return resultInfo;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));

        model.addAttribute("code", status.toString());
        model.addAttribute("msg", httpStatus.getReasonPhrase());
        model.addAttribute("timestamp", new Date());
        return "error/error";
    }
}
