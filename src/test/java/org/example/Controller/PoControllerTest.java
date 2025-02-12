package org.example.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Dto.ResponseDto;
import org.example.Entity.PoDetailEntity;
import org.example.Entity.PoHeaderEntity;
import org.example.Service.PoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PoService poService;

    @InjectMocks
    private PoController poController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(poController).build();
    }

    @Test
    void testCreatePo() throws Exception {
        PoHeaderEntity poHeaderEntity = new PoHeaderEntity();
        poHeaderEntity.setDateTime(new Date());
        poHeaderEntity.setDescription("testing po");

        PoDetailEntity poDetail1 = new PoDetailEntity();
        poDetail1.setItemId(1L);
        poDetail1.setQuantity(1);

        PoDetailEntity poDetail2 = new PoDetailEntity();
        poDetail2.setItemId(2L);
        poDetail2.setQuantity(1);

        poHeaderEntity.setPoDetailEntityList(Arrays.asList(poDetail1, poDetail2));

        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Create PO");
        responseDto.setData(poHeaderEntity);

        when(poService.createUpdatePo(poHeaderEntity)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.CREATED));

        mockMvc.perform(post("/v1/api/po/create-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(poHeaderEntity)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success Create PO"));
    }
}
