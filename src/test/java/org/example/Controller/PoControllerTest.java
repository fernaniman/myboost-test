package org.example.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Dto.ResponseDto;
import org.example.Entity.ItemEntity;
import org.example.Entity.PoDetailEntity;
import org.example.Entity.PoHeaderEntity;
import org.example.Entity.UsersEntity;
import org.example.Service.ItemService;
import org.example.Service.PoService;
import org.example.Service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PoControllerTest {

    private MockMvc mockMvcUser;
    private MockMvc mockMvcItem;
    private MockMvc mockMvcPo;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @Mock
    private PoService poService;

    @InjectMocks
    private PoController poController;

    @InjectMocks
    private ItemController itemController;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvcUser = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvcItem = MockMvcBuilders.standaloneSetup(itemController).build();
        mockMvcPo = MockMvcBuilders.standaloneSetup(poController).build();
    }

    @Test
    void testCreateUser() throws Exception {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setFirstName("John");
        usersEntity.setLastName("Doe");
        usersEntity.setEmail("3uV3P@example.com");
        usersEntity.setPhone("08123456789");

        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Create User");
        responseDto.setData(usersEntity);

        when(userService.createUpdateUserRepo(usersEntity)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.CREATED));

        mockMvcUser.perform(post("/v1/api/user/create-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersEntity)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success Create User"));
    }

    @Test
    void testCreateItem() throws Exception {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("Item 1");
        itemEntity.setDescription("Item Description");
        itemEntity.setPrice(1000);
        itemEntity.setCost(500);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Create Item");
        responseDto.setData(itemEntity);

        when(itemService.createUpdateItem(itemEntity)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.CREATED));

        mockMvcItem.perform(post("/v1/api/item/create-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemEntity)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success Create Item"));
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

        mockMvcPo.perform(post("/v1/api/po/create-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(poHeaderEntity)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success Create PO"));
    }

    @Test
    void testGetAllUser() throws Exception {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Get User");
        responseDto.setData(new UsersEntity());

        when(userService.getAllUsers()).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        mockMvcUser.perform(get("/v1/api/user/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success Get User"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(1L);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Get User");
        responseDto.setData(usersEntity);

        when(userService.getUserById(1L)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        mockMvcUser.perform(get("/v1/api/user/get-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success Get User"));
    }

    @Test
    void testGetAllItem() throws Exception {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Get Item");
        responseDto.setData(new ItemEntity());

        when(itemService.getAllItems()).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        mockMvcItem.perform(get("/v1/api/item/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success Get Item"));
    }

    @Test
    void testGetItemById_Success() throws Exception {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Get Item");
        responseDto.setData(itemEntity);

        when(itemService.getItemById(1L)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        mockMvcItem.perform(get("/v1/api/item/get-by-id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success Get Item"));
    }

    @Test
    void testGetAllPo() throws Exception {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Get PO");
        responseDto.setData(Arrays.asList(new PoHeaderEntity()));

        when(poService.getAllPo()).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        mockMvcPo.perform(get("/v1/api/po/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success Get PO"));
    }

    @Test
    void testGetPoById_Success() throws Exception {
        PoHeaderEntity poHeaderEntity = new PoHeaderEntity();
        poHeaderEntity.setId(6L);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("Success Get PO");
        responseDto.setData(poHeaderEntity);

        when(poService.getPoById(6L)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        mockMvcPo.perform(get("/v1/api/po/get-by-id/6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success Get PO"));
    }

}
