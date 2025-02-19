package fr.cleanarchitecture.easyteach;

import org.springframework.boot.SpringApplication;

public class TestEasyTeachApplication {

    public static void main(String[] args) {
        SpringApplication.from(EasyTeachApplication::main).with(PostgresSQLTestConfiguration.class).run(args);
    }

}
