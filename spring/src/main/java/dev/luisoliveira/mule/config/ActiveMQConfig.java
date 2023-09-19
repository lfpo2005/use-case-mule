package dev.luisoliveira.mule.config;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;

/*@Configuration
@EnableJms*/
public class ActiveMQConfig {

    @Bean
    public BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.setPersistent(false);
        return broker;
    }
}