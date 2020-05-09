package lv.iljakorneckis.webloans.acceptance;

import cucumber.api.java.Before;
import lv.iljakorneckis.webloans.Launcher;
import org.springframework.boot.SpringApplication;

public class HarnessDefs {

    @Before("@spring-start")
    public void before() {
        SpringApplication.run(Launcher.class, new String[]{});
    }
}
