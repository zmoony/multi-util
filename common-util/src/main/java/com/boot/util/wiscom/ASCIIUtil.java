/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

/**
 *
 * @author Administrator
 */
public class ASCIIUtil {
    public static String stringToASCII(String str){
        char[] chars = str.toCharArray();
        StringBuilder sbu = new StringBuilder(); 
        for(char c:chars){
            int ascii=c;
            sbu.append(ascii);
        }
        return sbu.toString();
    }
    
}
