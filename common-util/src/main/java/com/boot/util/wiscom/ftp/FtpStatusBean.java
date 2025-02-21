package com.boot.util.wiscom.ftp;

import lombok.Data;

@Data
public class FtpStatusBean {
    private boolean flag;
    /**flag为false的原因*/
    private String errormsg;
}