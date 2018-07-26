package cn.kkl.mall.testactivemq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javassist.expr.NewArray;

/**
 * @author Admin
 * use activemq step :
 * 1. create connectionFactory instance need specific service address(ip+port).the function of connectionFactory instance is create connection
 * 2. use the connectionFactory instance build connection
 * 3. open the connection by invoking the connection instance start() method.
 * 4. create session instance.
 * 5. use the session instance create destination instance.the destination instance has two difference style queue and topic
 * 6. use the session instance create producer instance
 * 7. create message instance,total type of data has five kinds :streamMessage MapMessage testMessage objectMessage bytesMessage
 * 8. send message 
 * 9. close resource ,like producer,destination ,session,connection and so on 
 */
public class ActivemqTest {
	
	private String brokerURL="tcp://192.168.25.133:61616";
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Queue queue;
	private MessageProducer producer;
	private TextMessage message1;
	private TextMessage message2;
	private MessageConsumer consumer;
	
	@Before
	public void init() {
		connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			/*connection.createSession(boolean arg1,int arg2);
			 * arg1 represent :whether to open transaction which if the message does not send successful again.
			 *                 generally do net open transaction.if open transaction the second parameter arg2 
			 *                 not valid automatically ignored arg2
			 * arg2 represent :answer mode,auto answer and manual answer.normally auto answer
			 */
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void queueProducer() {
		try {
			queue = session.createQueue("test-queue-01");
			producer = session.createProducer(queue);
			
			//the way one
			message1 = new ActiveMQTextMessage();
			message1.setText("hello ActiveMQ,hello world");
			//the way two
			message2 = session.createTextMessage("hello ActiveMQ, i am coming");
			
			producer.send(message1);
			producer.send(message2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * point-to-point send message 
	 * use step :
	 * 1. create connectionFactory to create connection instance connect activeMQ server
	 * 2. open connection instance 
	 * 3. create session instance
	 * 4. create destination instance by session instance
	 * 5. use session instance create consumer instance
	 * 6. receive message by consumer then print message content
	 * 7. close resource
	 */
	@Test
	public void queueConsumer() {
		try {
			Queue queue2 = session.createQueue("test-queue-01");
			consumer = session.createConsumer(queue2);
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message arg0) {
					TextMessage message=(TextMessage) arg0;
					try {
						String string = message.getText();
						System.out.println(string);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void topicProducer() {
		MessageProducer producer2=null;
		try {
			Topic topic = session.createTopic("test-topic-01");
			producer2 = session.createProducer(topic);
			TextMessage message = session.createTextMessage("hello topic activemq,i am coming");
			producer2.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			try {
				if (producer2!=null) {
					producer2.close();
				}
			} catch (Exception e2) {
			}
		}
	}
	
	@Test
	public void topicConsumer() {
		MessageConsumer consumer2=null;
		try {
			Topic topic = session.createTopic("test-topic-01");
			consumer2 = session.createConsumer(topic);
			consumer2.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message arg0) {
					TextMessage message = (TextMessage) arg0;
					try {
						String string = message.getText();
						System.out.println(string);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			try {
				System.out.println("topic consumer three already boot>>>>>>>");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			try {
				if (consumer2!=null) {
					consumer2.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	@After
	public void destory() {
		try {
			if (producer!=null) {
				producer.close();
			}
			if (consumer!=null) {
				consumer.close();
			}
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
