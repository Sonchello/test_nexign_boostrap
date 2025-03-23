package com.example.demo.controller;

import com.example.demo.service.CdrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CdrController.class)
public class CdrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CdrService cdrService;

    @Test
    public void testGenerateCdrRecords() throws Exception {
        // Подготовка данных
        doNothing().when(cdrService).generateCdrRecords();

        // Вызов тестируемого метода и проверка результата
        mockMvc.perform(post("/cdr/generate"))
                .andExpect(status().isOk())
                .andExpect(content().string("CDR записи успешно сгенерированы!"));
    }
}