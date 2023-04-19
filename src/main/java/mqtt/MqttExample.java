package main.java.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttExample {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String TOPIC1 = "mytopic1";
    private static final String TOPIC2 = "mytopic2";
    private static final int QOS = 1;

    public static void main(String[] args) {

        // Create MQTT client objects
        String clientId1 = "Publisher1";
        String clientId2 = "Publisher2";
        MemoryPersistence persistence1 = new MemoryPersistence();
        MemoryPersistence persistence2 = new MemoryPersistence();
        try {
            MqttClient client1 = new MqttClient(BROKER_URL, clientId1, persistence1);
            MqttClient client2 = new MqttClient(BROKER_URL, clientId2, persistence2);

            // Set callback handlers for subscribers
            MqttCallback callback1 = new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {}
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received message on topic " + topic + ": " + message);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {}
            };
            MqttCallback callback2 = new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {}
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received message on topic " + topic + ": " + message);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {}
            };
            client1.setCallback(callback1);
            client2.setCallback(callback2);

            // Connect to the broker and subscribe to topics
            client1.connect();
            client1.subscribe(TOPIC1, QOS);
            client2.connect();
            client2.subscribe(TOPIC2, QOS);

            // Publish messages to topics
            MqttMessage message1 = new MqttMessage("Hello from Publisher1".getBytes());
            MqttMessage message2 = new MqttMessage("Hello from Publisher2".getBytes());
            message1.setQos(QOS);
            message2.setQos(QOS);
            client1.publish(TOPIC2, message1);
            client2.publish(TOPIC1, message2);

            // Disconnect from broker
            client1.disconnect();
            client2.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
