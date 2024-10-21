package org.example;

import org.eclipse.paho.client.mqttv3.*;

public class Main {

    public static void main(String[] args) {
        String broker = "ssl://694db29983f8479bafa92337d5de0db1.s1.eu.hivemq.cloud:8883"; // Broker qua TLS/SSL
        String clientId = MqttClient.generateClientId(); // Tạo client ID ngẫu nhiên
        String topic = "test/20215545"; // Chủ đề muốn subscribe/publish

        try {
            // Khởi tạo MqttClient
            MqttClient client = new MqttClient(broker, clientId);

            // Thiết lập các tùy chọn kết nối
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("tranducthien");  // Thay bằng username của bạn
            options.setPassword("Adung1703".toCharArray());  // Thay bằng password của bạn
            options.setCleanSession(true); // Tạo phiên làm việc mới mỗi khi kết nối

            // Cài đặt callback để nhận tin nhắn
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost!");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Message received:\n\t" + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Delivery complete!");
                }
            });

            // Kết nối tới broker sử dụng các tùy chọn
            client.connect(options);
            System.out.println("Connected to broker: " + broker);

            // Subscribe tới một chủ đề
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);

            // Tạo tin nhắn và publish tới topic
            String messageContent = "Hello MQTT from Java!";
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(1); // Chất lượng dịch vụ QoS
            client.publish(topic, message);
            System.out.println("Message published: " + messageContent);

            // Giữ kết nối mở để nhận tin nhắn
            Thread.sleep(5000);

            // Đóng kết nối sau khi hoàn thành
            client.disconnect();
            System.out.println("Disconnected from broker");

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
