package net.pechorina.kontempl;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class KontApp {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(KontApp.class).web(true).run(args);
    }

}
