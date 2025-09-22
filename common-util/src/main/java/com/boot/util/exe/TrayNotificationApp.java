package com.boot.util.exe;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * TrayNotificationApp
 *
 * @author yuez
 * @since 2025/7/18
 */
public class TrayNotificationApp extends Application {
    private static boolean traySupported = SystemTray.isSupported();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 主界面
        Button btn = new Button("显示弹窗");
        btn.setOnAction(event -> showNotification("提示", "这是一个右下角弹窗"));

        Scene scene = new Scene(new StackPane(btn), 300, 200);
        primaryStage.setTitle("JavaFX 托盘弹窗示例");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("系统托盘支持：" + traySupported);

        // 初始化系统托盘
        if (traySupported) {
            initTrayIcon(primaryStage);
        }
    }

    private void initTrayIcon(Stage stage) throws URISyntaxException, IOException {
        if (!traySupported) return;

        SystemTray tray = SystemTray.getSystemTray();
        //根目录
//        String path = System.getProperty("user.dir");//D:\project\code\self_util_plugin\scaffold\muti-scaffold
//        String path = Paths.get("").toAbsolutePath().toString(); //D:\project\code\self_util_plugin\scaffold\muti-scaffold
        // 获取当前文件所在的目录class
//        String path = this.getClass().getClassLoader().getResource("").getPath();///D:/project/code/self_util_plugin/scaffold/muti-scaffold/common-util/target/classes/
//        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();///D:/project/code/self_util_plugin/scaffold/muti-scaffold/common-util/target/classes/
//        String path = ResourceUtils.getURL("classpath:").getPath();
//        String path = Paths.get(this.getClass().getProtectionDomain()
//                .getCodeSource().getLocation().toURI()).toString();
//        String dir = MyUtil.class
//                .getProtectionDomain()
//                .getCodeSource()
//                .getLocation()
//                .getPath();
        //资源加载
//        Resource resource = new ClassPathResource("");
//        String path = resource.getFile().getAbsolutePath();

        //获取当前类所在目录
//        String path = TrayNotificationApp.class.getResource("").getPath();///D:/project/code/self_util_plugin/scaffold/muti-scaffold/common-util/target/classes/com/boot/util/exe/
        String path = "D:\\project\\code\\self_util_plugin\\scaffold\\muti-scaffold\\common-util\\src\\main\\java\\com\\boot\\util\\exe\\icon.png";
//        InputStream resourceAsStream = getClass().getResourceAsStream(filename);
        //流转字节数组
//        byte[] bytes = IOUtils.toByteArray(resourceAsStream);
//        String path = Paths.get(this.getClass().getProtectionDomain()
//                .getCodeSource().getLocation().toURI()).getParent().toString();
        System.out.println(path);
        Image image = Toolkit.getDefaultToolkit().getImage(path ); // 替换为你自己的图标
//        Image image = Toolkit.getDefaultToolkit().createImage(bytes);

        PopupMenu popup = new PopupMenu();
        MenuItem showItem = new MenuItem("显示窗口");
        MenuItem exitItem = new MenuItem("退出");
        showItem.setLabel(new String("显示窗口".getBytes("UTF-8"), "GBK"));
        exitItem.setLabel(new String("退出".getBytes("UTF-8"), "GBK"));

        Font font = new Font("黑体", Font.PLAIN, 12);
        popup.setFont(font);
        showItem.setFont(font);
        exitItem.setFont(font);

        showItem.addActionListener(e -> Platform.runLater(stage::show));
        exitItem.addActionListener(e -> {
            tray.remove(tray.getTrayIcons()[0]);
            System.exit(0);
        });

        popup.add(showItem);
        popup.addSeparator();
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, "JavaFX 托盘应用", popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String title, String message) {
        if (traySupported) {
            SystemTray.getSystemTray().getTrayIcons()[0].displayMessage(title, message, TrayIcon.MessageType.INFO);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        // 设置系统属性以解决中文乱码问题
        System.setProperty("file.encoding", "UTF-8");
        // 设置默认的字体为支持中文的字体，例如 '宋体'
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}
