package org.example.Controller;

import org.example.Dto.ResponseDto;
import org.example.Entity.PoHeaderEntity;
import org.example.Entity.PoDetailEntity;
import org.example.Service.PoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@SpringBootTest
public class PoControllerTest {

    @InjectMocks
    private PoController poController;

    @MockBean
    private PoService poService;

    private PoHeaderEntity poHeaderEntity;

    private ResponseDto responseDto;

    @BeforeEach
    void setUp() {
        poHeaderEntity = new PoHeaderEntity();
        poHeaderEntity.setId(1L);
        poHeaderEntity.setDateTime(new Date());
        poHeaderEntity.setDescription("Test PO");
        poHeaderEntity.setTotalPrice(1000);
        poHeaderEntity.setTotalCost(800);

        PoDetailEntity poDetailEntity = new PoDetailEntity();
        poDetailEntity.setItemId(1L);
        poDetailEntity.setQuantity(10);
        poDetailEntity.setPrice(100);
        poDetailEntity.setCost(80);

        poHeaderEntity.setPoDetailEntityList(Arrays.asList(poDetailEntity));
    }

    @Test
    void testCreatePo_Success() {
        when(poService.createUpdatePo(any(PoHeaderEntity.class)))
                .thenReturn(new ResponseEntity<>(new ResponseDto(true, "Success", poHeaderEntity), HttpStatus.CREATED));

        ResponseEntity<ResponseDto> response = poController.createPo(poHeaderEntity);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(poHeaderEntity, response.getBody().getData());

        verify(poService, times(1)).createUpdatePo(any(PoHeaderEntity.class));
    }

    @Test
    void testCreatePo_Failure_ItemNotFound() {
        PoHeaderEntity invalidPoHeader = new PoHeaderEntity();
        PoDetailEntity invalidPoDetail = new PoDetailEntity();
        invalidPoDetail.setItemId(null);
        invalidPoHeader.setPoDetailEntityList(Arrays.asList(invalidPoDetail));

        when(poService.createUpdatePo(any(PoHeaderEntity.class)))
                .thenReturn(new ResponseEntity<>(new ResponseDto(false, "Item is required", null), HttpStatus.BAD_REQUEST));

        ResponseEntity<ResponseDto> response = poController.createPo(invalidPoHeader);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals("Item is required", response.getBody().getMessage());

        verify(poService, times(1)).createUpdatePo(any(PoHeaderEntity.class));
    }

    @Test
    void testCreatePo_NotFoundPoHeader() {
        // Arrange
        PoHeaderEntity invalidPoHeader = new PoHeaderEntity();
        invalidPoHeader.setId(99L); // Non-existing PoHeader ID

        when(poService.createUpdatePo(any(PoHeaderEntity.class)))
                .thenReturn(new ResponseEntity<>(new ResponseDto(false, "PO not found", null), HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<ResponseDto> response = poController.createPo(invalidPoHeader);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getSuccess());
        assertEquals("PO not found", response.getBody().getMessage());

        verify(poService, times(1)).createUpdatePo(any(PoHeaderEntity.class));
    }
}

