package fr.iss.soa.presencemicroservice;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PresenceMicroserviceApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PresenceDetectorController presenceDetectorController;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(presenceDetectorController).isNotNull();
    }

    @Test
    void testJsonFormat() throws Exception {
        mockMvc.perform(get("/detectors")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void testGetDetector() throws Exception {
        mockMvc.perform(get("/detectors"));
        mockMvc.perform(get("/detectors/11"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(11),
                        jsonPath("$.manufacturer").value("Toto")
                );
    }

    @Test
    void testGetUnknownPresenceDetectorGives404() throws Exception {
        mockMvc.perform(get("/detectors"));
        mockMvc.perform(get("/detectors/123456")).andExpect(status().isNotFound());
    }

}
