package unionlock.iceserver.mqtt;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
public class Send {

	public static void main(String[] args) {
        int qos             = 2;
        String broker       = "tcp://10.192.2.154:1883";// "tcp://iot.eclipse.org:1883";
        String clientId     = "server001";
        String topic        = "tokudu/client001";
        String content      = "这是的给["+topic+"]的消息";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("服务器地址: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("已连接");
            System.out.println("消息内容: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("发送完成！");
            sampleClient.disconnect();
            System.out.println("断开连接");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }

	}

}
