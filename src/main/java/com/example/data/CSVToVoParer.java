package com.example.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Configuration

public class CSVToVoParer {
    private List<SAccontInfo> accontInfoList;
    private List<SManagementPointInfo> managementpointInfoList;
    private List<STransactionHistory> transactionHistoryList;

    public List<SAccontInfo> getSAccountInfoList(){
        File input = new File("data/account_information.csv");
        if(accontInfoList == null){
            accontInfoList = new ArrayList<>();
        }else{
            return accontInfoList;
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader(input));
            // read line by line
            String line;
            reader.readLine(); // 헤더부분 건너뜀
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(",");
                if(strs.length != 3){
                    return null;
                }
                SAccontInfo info = new SAccontInfo();
                info.acctNo = strs[0];
                info.name = strs[1];
                info.code = strs[2];
                accontInfoList.add(info);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return accontInfoList;
    }

    public List<SManagementPointInfo> getSManagementpointInfoList(){
        File input = new File("data/management_point_information.csv");
        if(managementpointInfoList == null){
            managementpointInfoList = new ArrayList<>();
        }else{
            return managementpointInfoList;
        }
        Pattern pattern = Pattern.compile(",");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(input));
            // read line by line
            String line;
            reader.readLine(); // 헤더부분 건너뜀
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(",");
                if(strs.length != 2){
                    return null;
                }
                SManagementPointInfo info = new SManagementPointInfo();
                info.brCode = strs[0];
                info.brName = strs[1];
                managementpointInfoList.add(info);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return managementpointInfoList;
    }

    public List<STransactionHistory> getSTransactionHistoryList(){
        File input = new File("data/transaction_history.csv");
        if(transactionHistoryList == null){
            transactionHistoryList = new ArrayList<>();
        }else{
            return transactionHistoryList;
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader(input));
            // read line by line
            String line;
            reader.readLine(); // 헤더부분 건너뜀
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(",");
                if(strs.length != 6){
                    return null;
                }
                STransactionHistory info = new STransactionHistory();
                info.date = strs[0];
                info.acctNo = strs[1];
                info.dealNum = strs[2];
                info.pay = strs[3];
                info.fee = strs[4];
                info.isCancel = strs[5].toUpperCase().equals("Y");
                transactionHistoryList.add(info);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return transactionHistoryList;
    }
}
