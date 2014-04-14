package cn.damai.boss.projectreport.job.impl.task;

import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
	private static final Logger log = LoggerFactory
			.getLogger(MessageSender.class);
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	@Resource
	private JmsTemplate jmsTemplate;
	
	@Resource
	private Destination destination;

	public void send(final java.io.Serializable sendData) {
		try {
			// jmsTemplate.setExplicitQosEnabled(true);
			// jmsTemplate.setTimeToLive(3000);
			jmsTemplate.send(destination, new MessageCreator() {
				public Message createMessage(Session session)
						throws JMSException {
					BytesMessage objectMessage = session.createBytesMessage();
					
					//添加类型属性
					String synClassName = sendData.getClass().getName();
					objectMessage.setStringProperty("dataType", synClassName);
					
					try {
						byte[] bytes = jsonMapper.writeValueAsString(sendData)
								.getBytes("UTF-8");
						String synData = new String(bytes, "UTF-8");
						objectMessage.writeBytes(synData.getBytes());
						
						if (log.isDebugEnabled()) {
							log.debug("开始发送消息·········");
							log.debug("synClassName=" + synClassName);
							log.debug("synData=\n" + synData);
							log.debug("消息发送结束··········");
						}
					} catch (Exception e) {
						if (log.isErrorEnabled()) {
							log.error("MessageSender,发送消息  ERROR");
						}
						e.printStackTrace();
					}
					objectMessage.setJMSMessageID(UUID.randomUUID().toString());
					return objectMessage;
				}
			});
			
		} catch (Exception ex) {
			if (log.isErrorEnabled()) {
				log.error("MessageSender,发送消息  ERROR");
			}
			ex.printStackTrace();
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}