/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;

/**
 * apache commons-csv
 * @author 13900
 */
@Log4j2
public class CommonCSVUtil {
    public static void readFile(){
        try{
            
            
            Reader in = new FileReader("data/RY_SWXXJBXX-20190927152014211.csv");
            Iterable<CSVRecord> records =  CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                String RYBH = record.get("RYBH");
                String GMSFHM = record.get("GMSFHM");
                String XM = record.get("XM");
                log.info(RYBH+"," +GMSFHM+","+XM);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    
    }
    public static void readFile2(){
        try{
            InputStream is=new FileInputStream("data/RY_HCYJBXX.csv");
            Reader reader = new InputStreamReader(new BOMInputStream(is), "UTF-8");
            CSVFormat csvFileFormat = CSVFormat.RFC4180.withFirstRecordAsHeader().withQuote(null);
            CSVParser parser = new CSVParser(reader, csvFileFormat);
            for (CSVRecord record : parser) {
                
                log.info(  record.getRecordNumber()  +" "+record.toString());
                
//                String RYBH = record.get("RYBH");
//                String GMSFHM = record.get("GMSFHM");
//                String XM = record.get("XM");
//                String XB = record.get("XB");
//                String CSRQ = record.get("CSRQ");
//                String HJQH = record.get("HJQH");
//                String HJXZ = record.get("HJXZ");
//                String HJZRQ = record.get("HJZRQ");
//                String ZYWT = record.get("ZYWT");
//                String ZGSWJB = record.get("ZGSWJB");
//                String DJSJ = record.get("DJSJ");
//                String XGSJ = record.get("XGSJ");
                //StringBuilder sb=new StringBuilder();
            }
            parser.close();
            reader.close();
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    
    }

}
