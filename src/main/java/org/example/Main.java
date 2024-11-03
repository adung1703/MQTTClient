package org.example;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String broker = "ssl://694db29983f8479bafa92337d5de0db1.s1.eu.hivemq.cloud:8883";
        String clientId = "JavaClient";
        String topic = "IOT_2B";
        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("adung1703");
            options.setPassword("Adung1703".toCharArray());
            options.setCleanSession(true); // Tạo phiên làm việc mới mỗi khi kết nối

            // Nhận tin nhắn
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

            // Nhập tin nhắn từ bàn phím và publish lên
            String messageContent = "";
            while (true) {
                Scanner scanner = new Scanner(System.in);
                messageContent = scanner.nextLine();
                if (messageContent.equals("#")) { // Cho tới khi nhận được "#" thì đóng kết nối
                    break;
                }
                MqttMessage message = new MqttMessage(messageContent.getBytes());
                message.setQos(1); // Chất lượng dịch vụ QoS
                client.publish(topic, message);
                System.out.println("Message published: " + messageContent);
            }

            // Đóng kết nối sau khi hoàn thành
            client.disconnect();
            System.out.println("Disconnected from broker");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
