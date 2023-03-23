package com.boot.util.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageMergeUtil {
    public static void main(String[] args) throws IOException {
        String text = "测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字测试文字";

        ImageIO.write(addTextToPic(text,
                "C:\\Users\\zaiji\\Desktop\\杂物\\aaaa.png"),
                "png",
                new File("C:\\Users\\zaiji\\Desktop\\杂物\\aaaa" + UUID.randomUUID() + ".png"));

    }


    public static BufferedImage addTextToPic(String text, String fileDir) throws IOException {
        //读取源文件
        File oldFile = new File(fileDir);
        Image image = ImageIO.read(oldFile);

        //****************预先测试当前文字会占的大小
        BufferedImage tempTextBi = new BufferedImage(image.getWidth(null), 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D tempTextGraphic = tempTextBi.createGraphics();
        //防止生成的文字带有锯齿
        tempTextGraphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //字体
        Font font = new Font("黑体", Font.ROMAN_BASELINE, 25);
        tempTextGraphic.setFont(font);
        Rectangle2D bound = font.getStringBounds(text, tempTextGraphic.getFontRenderContext());
        //因为字体的y是baseline的位置，那么文字所在位置，就是图片高度+|baseline上半部分位置|，即图片高度-bound.getMinY()
        //结果
        final int textInResultY = (int) Math.ceil(image.getHeight(null) - bound.getMinY());
        final int textHeight = (int) Math.ceil(bound.getHeight());



        //****************开始叠加字，创建图片大小加上文字高度
        BufferedImage resultImage = new BufferedImage(image.getWidth(null), image.getHeight(null) + textHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = resultImage.createGraphics();
        //设置背景为黑色
        graphic.setBackground(Color.BLACK);
        //像结果中绘制图片
        graphic.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
        //设置字体
        graphic.setFont(font);
        //设置字体颜色
        graphic.setPaint(Color.RED);
        //防止生成的文字带有锯齿
        graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //在图片上生成文字
        graphic.drawString(text, 0, textInResultY);
        graphic.dispose();

        return resultImage;
    }

}

