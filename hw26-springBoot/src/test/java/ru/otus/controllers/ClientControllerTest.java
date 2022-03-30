package ru.otus.controllers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.crm.services.DBClientService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DBClientService clientService;

    @ParameterizedTest
    @CsvSource({"/, Клиенты", "/client/list, Клиенты"})
    void clientsListView(String path, String value) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(value)));
    }

    @ParameterizedTest
    @CsvSource({"/client/create, New Client"})
    void clientCreateView(String path, String value) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("client"))
                .andExpect(content().string(containsString(value)));
    }

    @ParameterizedTest
    @CsvSource({"/client/save"})
    void clientSaveWhenFieldsFillingThenRedirect(String path) throws Exception {
        mockMvc.perform(post(path)
                        .param("name","Kesha")
                        .param("address.street", "Butlerova")
                        .param("phonesAsList[0].clientPhone", "123-009"))
                .andExpect(redirectedUrl("/"));
    }

    @ParameterizedTest
    @CsvSource({"/client/save, Please provide a client name."})
    void clientSaveWhenEmptyFieldExistThenReturnErrorMessage(String path, String value) throws Exception {
        mockMvc.perform(post(path)
                        .param("address.street", "Butlerova")
                        .param("phonesAsList[0].clientPhone", "123-009"))
                .andExpect(content().string(containsString(value)));
    }

    @ParameterizedTest
    @CsvSource({"/addPhone, add phone number"})
    void addPhone(String path, String value) throws Exception {
        mockMvc.perform(post(path))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(value)));
    }
}