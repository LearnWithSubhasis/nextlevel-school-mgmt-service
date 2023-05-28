package org.nextlevel.tools.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaConnectionProvider {
	private static final Logger log = LoggerFactory.getLogger(KafkaConnectionProvider.class);

	private static KafkaConnectionProvider instance = new KafkaConnectionProvider();

	@Value("${kafka-config.topic}")
	private static String ES_REALTIME_METADATA_QUEUE_NAME;

	@Value("${kafka-config.bootstrap-server-host}")
	private String bootstrapServerHost;

	@Value("${kafka-config.bootstrap-server-host}")
	private String bootstrapServerPort;

	private KafkaProducer<String, String> producer;
	private Properties props = new Properties();

	private boolean isInitialisedAlready = false;

	@Autowired
	KafkaProperties kafkaProperties;

	private KafkaConnectionProvider(){
		try {
			props.put("bootstrap.servers", kafkaProperties.getBootstrapServerHost() +
					":" + kafkaProperties.getBootstrapServerPort());
			props.put("group.id", "test");
			props.put("enable.auto.commit", "true");
			props.put("auto.commit.interval.ms", "1000");
			props.put("session.timeout.ms", "30000");
			props.put("key.deserializer",
					"org.apache.kafka.common.serialization.StringDeserializer");
			props.put("value.deserializer",
					"org.apache.kafka.common.serialization.StringDeserializer");

			props.setProperty("key.serializer", StringSerializer.class.getName());
			props.setProperty("value.serializer", StringSerializer.class.getName());

			producer = new KafkaProducer<String, String>(props);

			isInitialisedAlready = true;
		} catch (Exception e) {
			isInitialisedAlready = false;
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void sendRequestsToMQ(HashMap<String, String> mapNewItem) throws Exception {
		if(!isInitialisedAlready) {

			try {
				props.put("bootstrap.servers", "localhost:9092");
				props.put("group.id", "test");
				props.put("enable.auto.commit", "true");
				props.put("auto.commit.interval.ms", "1000");
				props.put("session.timeout.ms", "30000");
				props.put("key.deserializer",
						"org.apache.kafka.common.serialization.StringDeserializer");
				props.put("value.deserializer",
						"org.apache.kafka.common.serialization.StringDeserializer");

				props.setProperty("key.serializer", StringSerializer.class.getName());
				props.setProperty("value.serializer", StringSerializer.class.getName());

				producer = new KafkaProducer<String, String>(props);

				if(null == ES_REALTIME_METADATA_QUEUE_NAME) {
					ES_REALTIME_METADATA_QUEUE_NAME = "NEXTLEVEL_ES_REALTIME_METADATA_QUEUE_NAME";
				}

				isInitialisedAlready = true;

			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}

		this.producer.send(new ProducerRecord<String, String>(ES_REALTIME_METADATA_QUEUE_NAME, new JSONObject(mapNewItem).toJSONString()));
	}
	
	public KafkaProducer<String, String> getProducer() {
		return producer;
	}
	
	private void setProducer(Producer<String, String> producer) {
		this.producer = (KafkaProducer<String, String>) producer;
	}
	
	public static KafkaConnectionProvider getInstance() {
		return instance;
	}
}

@Component
@ConfigurationProperties(prefix = "kafka-config")
@ConfigurationPropertiesScan
@Getter
@Setter
@NoArgsConstructor
class KafkaProperties {
	private String topic;
	private String bootstrapServerHost;
	private String bootstrapServerPort;
}
