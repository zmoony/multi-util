package com.boot.util.wiscom.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SimpleMqttExample implements MqttCallback {

    private static final String BROKER_URL = "tcp://your_mqtt_broker_address:1883";
    private static final String CLIENT_ID = "JavaSample";
    private static final String TOPIC_SUBSCRIBE = "your/topic/sub";
    private static final String TOPIC_PUBLISH = "your/topic/pub";

    public static void main(String[] args) {
        SimpleMqttExample example = new SimpleMqttExample();
        try {
            example.connect();
            example.subscribe();
            example.publish("Hello MQTT from Java!");
            Thread.sleep(5000); // 等待一段时间后断开连接
            example.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MqttClient client;

    public void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        client = new MqttClient(BROKER_URL,CLIENT_ID, new MemoryPersistence());
        client.setCallback(this);
        client.connect(options);
    }

    public void subscribe() throws MqttException {
        client.subscribe(TOPIC_SUBSCRIBE);
    }

    /**
     * QoS（服务质量）：MQTT 支持三种不同的服务质量等级：
     *
     * QoS 0：最多发送一次 (At most once)
     * QoS 1：至少发送一次 (At least once)
     * QoS 2：只发送一次 (Exactly once)这使得发送者和接收者可以根据需要选择合适的服务质量等级。
     * @param message
     * @throws MqttException
     */
    public void publish(String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(2); // 设置服务质量（例如：0、1或2）
        client.publish(TOPIC_PUBLISH, mqttMessage);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection to MQTT broker lost.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.printf("Received message on topic %s: %s%n", topic, new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Message delivered successfully.");
    }

    public void disconnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
            client.close();
        }
    }
}

