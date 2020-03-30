package com.es.demo;

import com.es.demo.utils.IpAddrUtil;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.SocketException;

@SpringBootApplication(scanBasePackages={"com.es.demo.*"})
@EnableSwagger2
public class ElasticsearchDemoApplication {

    public static void main(String[] args) {
        try {
            String localIp = IpAddrUtil.getIpAddress().getHostAddress();
            System.setProperty("local-ip", localIp);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        SpringApplication.run(ElasticsearchDemoApplication.class, args);
    }
}
