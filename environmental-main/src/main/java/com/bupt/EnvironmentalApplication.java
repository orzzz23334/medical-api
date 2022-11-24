package com.bupt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.Inet4Address;

@SpringBootApplication(scanBasePackages = {"com.bupt.*"})
@ComponentScan(value = {"com.bupt.*"})
public class EnvironmentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnvironmentalApplication.class, args);
        Logger logger = LoggerFactory.getLogger(EnvironmentalApplication.class);
        try{
            //ip
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //获取端口
            String prot = "8090";
            String ip = "http://" + hostAddress + ":" +prot + "/api/v1/swagger-ui.html";
            logger.info(ip);
            String doc = "http://"+hostAddress+":"+prot+"/api/v1/doc.html";
            logger.info(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
