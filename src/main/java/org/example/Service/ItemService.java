package org.example.Service;

import org.example.Dto.ResponseDto;
import org.example.Entity.ItemEntity;
import org.example.Repository.ItemRepo;
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
public class ItemService {
    private Logger log = LoggerFactory.getLogger(ItemService.class);

    private final static String ITEM = "Item";

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private Validator validator;

        public ResponseEntity<ResponseDto> createUpdateItem(ItemEntity request) {
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
                if(itemRepo.existsByName(request.getName())) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + ResponseUtils.EXISTS, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }

                request.setCreatedDate(new Date());
                itemRepo.save(request);
                responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_CREATE + ITEM, request);
                return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
            } else {
                Optional<ItemEntity> dataItem = itemRepo.findById(request.getId());
                if(dataItem.isEmpty()) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + ResponseUtils.NOT_FOUND, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
                }
                if(itemRepo.existsByIdNotAndName(request.getId(), request.getName())) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + ResponseUtils.EXISTS, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }

                ItemEntity dataItemUpdate = dataItem.get();
                dataItemUpdate.setName(request.getName());
                dataItemUpdate.setDescription(request.getDescription());
                dataItemUpdate.setPrice(request.getPrice());
                dataItemUpdate.setCost(request.getCost());
                dataItemUpdate.setUpdatedDate(new Date());
                itemRepo.save(dataItemUpdate);
                responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_UPDATE + ITEM, dataItemUpdate);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            responseDto = ResponseUtils.getResponse(false, e.getMessage(), null);
            return ResponseEntity.status(500).body(responseDto);
        }
    }

    public ResponseEntity<ResponseDto> getAllItems() {
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_GET + ITEM, itemRepo.findAll());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> getItemById(Long id) {
        Optional<ItemEntity> dataItem = itemRepo.findById(id);
        if(dataItem.isEmpty()) {
            ResponseDto responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + ResponseUtils.NOT_FOUND, null);
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_GET + ITEM, dataItem.get());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> deleteItemById(Long id) {
        Optional<ItemEntity> dataItem = itemRepo.findById(id);
        if(dataItem.isEmpty()) {
            ResponseDto responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + ResponseUtils.NOT_FOUND, null);
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        itemRepo.deleteById(id);
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_DELETE + ITEM, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
