package com.ilyabuglakov.raise.model.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

@Test
@Log4j2
public class GsonParseTest {

    private static String json = "{\"testName\":\"Первый\",\"characteristics\":[\"MEMORY\"],\"questions\":[{\"content\":\"Да?\",\"answers\":[{\"correct\":false,\"content\":\"Да\"},{\"correct\":true,\"content\":\"Нет\"}]}]}";
    private static String wrongJson = "{\"testaName\":\"Первый\",\"characteristics\":[\"MEMORY\"],\"questions\":[{\"content\":\"Да?\",\"answers\":[{\"correct\":false,\"content\":\"Да\"},{\"correct\":true,\"content\":\"Нет\"}]}]}";

    @Test
    public void testParse(){
        log.info(json);
        Gson gson = new Gson();
        com.ilyabuglakov.raise.domain.Test test = gson.fromJson(json, com.ilyabuglakov.raise.domain.Test.class);
        log.info(test);
    }

    @Test
    public void wrongTestParse(){
        log.info(wrongJson);
        Gson gson = new Gson();
        com.ilyabuglakov.raise.domain.Test test = gson.fromJson(wrongJson, com.ilyabuglakov.raise.domain.Test.class);
        log.info(test);
    }

    @Test
    public void emptyTestParse(){
        String emptyJson = "";
        log.info(emptyJson);
        Gson gson = new Gson();
        com.ilyabuglakov.raise.domain.Test test = gson.fromJson(emptyJson, com.ilyabuglakov.raise.domain.Test.class);
        log.info(test);
    }

}
