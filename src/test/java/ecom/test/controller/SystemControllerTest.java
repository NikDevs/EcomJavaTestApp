package ecom.test.controller;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SystemControllerTest {

    private static final String HEALTH_URI = "/system/health";
    private static final String IP_HEADER = "REMOTE_ADDR";
    private static final int LIMITED_STATUS = 502;

    private static final String FIRST_IP = "127.0.0.1";
    private static final String SECOND_IP = "127.0.0.2";

    @Autowired
    private WebApplicationContext context;

    @Value("${limiter.request.maxCount}")
    private int requestMaxCount;

    @Value("${limiter.time.limit}")
    private long timeLimit;

    @Test
    void health() throws Exception {
        final MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();

        final MockHttpServletRequestBuilder healthFirstIp = get(HEALTH_URI).header(IP_HEADER, FIRST_IP);
        final MockHttpServletRequestBuilder healthSecondIp = get(HEALTH_URI).header(IP_HEADER, SECOND_IP);

        final int firstHalfReqCount = requestMaxCount - requestMaxCount / 2;
        final int secondHalfReqCount = requestMaxCount - firstHalfReqCount;

        mvc.perform(healthFirstIp).andExpect(status().isOk());
        mvc.perform(healthSecondIp).andExpect(status().isOk());
        long start = System.currentTimeMillis();

        for (int i = 0; i < firstHalfReqCount - 1; i++) {
            mvc.perform(healthFirstIp).andExpect(status().isOk());
            mvc.perform(healthSecondIp).andExpect(status().isOk());
        }

        for (int i = 0; i < secondHalfReqCount; i++) {
            mvc.perform(healthFirstIp).andExpect(status().isOk());
            mvc.perform(healthSecondIp).andExpect(status().isOk());
        }

        mvc.perform(healthFirstIp).andExpect(status().is(LIMITED_STATUS));
        mvc.perform(healthSecondIp).andExpect(status().is(LIMITED_STATUS));

        TimeUnit.MILLISECONDS.sleep(start + timeLimit - System.currentTimeMillis());

        mvc.perform(healthFirstIp).andExpect(status().isOk());
        mvc.perform(healthSecondIp).andExpect(status().isOk());
    }
}