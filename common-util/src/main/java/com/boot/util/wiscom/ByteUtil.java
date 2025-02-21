package com.boot.util.wiscom;

/**
 * 字节处理功能
 *
 * @author hwang
 * */
public class ByteUtil {
    /**
     * ascii字节数组到时间的转换.
     */
    public static String byteToTime(byte[] b,int offset) {
        String strCurDate = DateToString.getCurrentDateTime("yyyy-MM-dd");
        StringBuilder stringBuilder = new StringBuilder(strCurDate);
        stringBuilder.append(" ");
        stringBuilder.append((char)(b[offset] & 0xff));
        stringBuilder.append((char)(b[offset+1] & 0xff));
        stringBuilder.append((char)(b[offset+2] & 0xff));
        stringBuilder.append((char)(b[offset+3] & 0xff));
        stringBuilder.append((char)(b[offset+4] & 0xff));
        stringBuilder.append((char)(b[offset+5] & 0xff));
        stringBuilder.append((char)(b[offset+6] & 0xff));
        stringBuilder.append((char)(b[offset+7] & 0xff));

        return stringBuilder.toString().trim();
    }
    /**
     * ascii字节数组到ip的转换
     */
    public static String byteToIp(byte[] b,int offset) {
        StringBuilder stringBuilder = new StringBuilder((char)(b[offset] & 0xff));
        stringBuilder.append((char)(b[offset+1] & 0xff));
        stringBuilder.append((char)(b[offset+2] & 0xff));
        stringBuilder.append((char)(b[offset+3] & 0xff));
        stringBuilder.append((char)(b[offset+4] & 0xff));
        stringBuilder.append((char)(b[offset+5] & 0xff));
        stringBuilder.append((char)(b[offset+6] & 0xff));
        stringBuilder.append((char)(b[offset+7] & 0xff));
        stringBuilder.append((char)(b[offset+8] & 0xff));
        stringBuilder.append((char)(b[offset+9] & 0xff));
        stringBuilder.append((char)(b[offset+10] & 0xff));
        stringBuilder.append((char)(b[offset+11] & 0xff));
        stringBuilder.append((char)(b[offset+12] & 0xff));
        stringBuilder.append((char)(b[offset+13] & 0xff));
        stringBuilder.append((char)(b[offset+14] & 0xff));
        stringBuilder.append((char)(b[offset+15] & 0xff));

        return stringBuilder.toString().trim();
    }
    /**
     * ascii字节数组到告警码的转换
     */
    public static String byteToCode(byte[] b,int offset) {
        return String.format("%c%c%c%c",(char)(b[offset] & 0xff),(char)(b[offset+1] & 0xff),
                (char)(b[offset+2] & 0xff),(char)(b[offset+3] & 0xff));
    }
    /**
     * 字节数组到float的转换.
     */
    public static float getFloat(byte[] b,int offset) {
        // 4 bytes
        int accum = 0;
        for ( int shiftBy = 0; shiftBy < 8; shiftBy++ ) {
            accum |= (b[offset + shiftBy] & 0xff) << shiftBy * 8;
        }
        return Float.intBitsToFloat(accum);
    }
    /**
     * 字节数组到double的转换.*/
    public static double getDouble(byte[] b,int offset) {
        long m;
        m = b[offset];
        m &= 0xff;
        m |= ((long) b[offset+1] << 8);
        m &= 0xffff;
        m |= ((long) b[offset+2] << 16);
        m &= 0xffffff;
        m |= ((long) b[offset+3] << 24);
        m &= 0xffffffffL;
        m |= ((long) b[offset+4] << 32);
        m &= 0xffffffffffL;
        m |= ((long) b[offset+5] << 40);
        m &= 0xffffffffffffL;
        m |= ((long) b[offset+6] << 48);
        m &= 0xffffffffffffffL;
        m |= ((long) b[offset+7] << 56);
        return Double.longBitsToDouble(m);
    }

    /**
     * 字节数组到int的转换.
     */
    public static int byteToInt(byte[] b,int offset) {
        int s = 0;
        int s0 = b[offset] & 0xff;// 最低位
        int s1 = b[offset+1] & 0xff;
        int s2 = b[offset+2] & 0xff;
        int s3 = b[offset+3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }
    /**
     * 字节数组到short的转换.
     */
    public static short byteToShort(byte[] b,int offset) {
        short s = 0;
        short s0 = (short) (b[offset] & 0xff);// 最低位
        short s1 = (short) (b[offset+1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }
    /**
     * 字节数组到GpsId的转换.
     */
    public static String byteToGpsId(byte[] b,int offset) {
        return String.format("%02d%02d%02d%02d%02d%02d",b[offset],b[offset+1],b[offset+2],b[offset+3],b[offset+4],b[offset+5]);
    }
}
