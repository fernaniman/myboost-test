package org.example.Service;

import org.example.Dto.ResponseDto;
import org.example.Entity.UsersEntity;
import org.example.Repository.UserRepo;
import org.example.Utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.*;

@Service
public class UserService {
    private Logger log = LoggerFactory.getLogger(UserService.class);

    private final static String USER = "User";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Validator validator;

    public ResponseEntity<ResponseDto> createUpdateUserRepo(UsersEntity request) {
        ResponseDto responseDto;
        try {
            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                List<String> validateHeader = new ArrayList<>();
                for (var violation : violations) {
                    log.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validateHeader.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationHeaderList.add(data);
                }
                responseDto = ResponseUtils.getResponse(false, validateHeader.get(0), null);
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }

            if(request.getId()==null) {
                if(userRepo.existsByFirstNameAndLastNameAndEmail(request.getFirstName(), request.getLastName(), request.getEmail())) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, USER + ResponseUtils.EXISTS, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }

                request.setCreatedDate(new Date());
                userRepo.save(request);
                responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_CREATE + USER, request);
                return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
            } else {
                Optional<UsersEntity> dataUser = userRepo.findById(request.getId());
                if(dataUser.isEmpty()) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, USER + ResponseUtils.NOT_FOUND, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
                }
                if(userRepo.existsByIdNotAndFirstNameAndLastNameAndEmail(request.getId(), request.getFirstName(), request.getLastName(), request.getEmail())) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, USER + ResponseUtils.EXISTS, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }

                UsersEntity dataUserUpdate = dataUser.get();
                dataUserUpdate.setFirstName(request.getFirstName());
                dataUserUpdate.setLastName(request.getLastName());
                dataUserUpdate.setEmail(request.getEmail());
                dataUserUpdate.setPhone(request.getPhone());
                dataUserUpdate.setUpdatedDate(new Date());
                userRepo.save(dataUserUpdate);
                responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_UPDATE + USER, dataUserUpdate);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            responseDto = ResponseUtils.getResponse(false, e.getMessage(), null);
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    public ResponseEntity<ResponseDto> getAllUsers() {
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_GET + USER, userRepo.findAll());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> getUserById(Long id) {
        Optional<UsersEntity> dataUser = userRepo.findById(id);
        if(dataUser.isEmpty()) {
            ResponseDto responseDto = ResponseUtils.getResponse(Boolean.FALSE, USER + ResponseUtils.NOT_FOUND, null);
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_GET + USER, dataUser.get());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> deleteUserById(Long id) {
        Optional<UsersEntity> dataUser = userRepo.findById(id);
        if(dataUser.isEmpty()) {
            ResponseDto responseDto = ResponseUtils.getResponse(Boolean.FALSE, USER + ResponseUtils.NOT_FOUND, null);
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        userRepo.deleteById(id);
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_DELETE + USER, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
