package org.example.Service;

import org.example.Dto.ResponseDto;
import org.example.Entity.ItemEntity;
import org.example.Entity.PoDetailEntity;
import org.example.Entity.PoHeaderEntity;
import org.example.Repository.ItemRepo;
import org.example.Repository.PoDetailRepo;
import org.example.Repository.PoHeaderRepo;
import org.example.Utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PoService {

    private final static String PO = "Purchase order";
    private final static String ITEM = "Item";

    private Logger log = LoggerFactory.getLogger(PoService.class);

    @Autowired
    private PoHeaderRepo poHeaderRepo;

    @Autowired
    private PoDetailRepo poDetailRepo;

    @Autowired
    private ItemRepo itemRepo;

    public ResponseEntity<ResponseDto> createUpdatePo(PoHeaderEntity request) {
        ResponseDto responseDto;
        try {

            // validate data
            for(PoDetailEntity item : request.getPoDetailEntityList()) {
                if(item.getItemId()==null) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + " is required", null);
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }
                if(!itemRepo.existsById(item.getItemId())) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + " not found", null);
                    return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
                }
            }
            Map<Long, Long> itemCount = request.getPoDetailEntityList().stream()
                    .collect(Collectors.groupingBy(PoDetailEntity::getItemId, Collectors.counting()));
            if(itemCount.values().stream().anyMatch(count -> count > 1)) {
                responseDto = ResponseUtils.getResponse(Boolean.FALSE, ITEM + " duplicate", null);
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }

            if(request.getId()==null) {
                return this.savePo(request);
            } else {
                Optional<PoHeaderEntity> dataPo = poHeaderRepo.findById(request.getId());
                if(dataPo.isEmpty()) {
                    responseDto = ResponseUtils.getResponse(Boolean.FALSE, PO + ResponseUtils.NOT_FOUND, null);
                    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                }
                for(PoDetailEntity item : request.getPoDetailEntityList()) {
                    if(item.getId()!=null) {
                        Optional<PoDetailEntity> dataDetail = poDetailRepo.findById(item.getId());
                        if(dataDetail.isEmpty()) {
                            responseDto = ResponseUtils.getResponse(Boolean.FALSE, PO + ResponseUtils.NOT_FOUND, null);
                            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
                        }
                    }
                }
                return this.savePo(request);
            }
        } catch (Exception e) {
            responseDto = ResponseUtils.getResponse(Boolean.FALSE, e.getMessage(), null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseDto> savePo(PoHeaderEntity request) {
        ResponseDto responseDto;
        Integer totalPrice = 0;
        Integer totalCost = 0;
        for(PoDetailEntity dtl : request.getPoDetailEntityList()) {
            ItemEntity dataItem = itemRepo.findById(dtl.getItemId()).get();
            totalPrice += dataItem.getPrice() * dtl.getQuantity();
            totalCost += dataItem.getCost() * dtl.getQuantity();
        }
        PoHeaderEntity poHeader = new PoHeaderEntity();
        if(request.getId()==null) {
            poHeader.setDateTime(request.getDateTime());
            poHeader.setDescription(request.getDescription());
            poHeader.setTotalPrice(totalPrice);
            poHeader.setTotalCost(totalCost);
            poHeader.setCreatedDate(new Date());
        } else {
            poHeader = poHeaderRepo.findById(request.getId()).get();
            poHeader.setDateTime(request.getDateTime());
            poHeader.setDescription(request.getDescription());
            poHeader.setTotalPrice(totalPrice);
            poHeader.setTotalCost(totalCost);
            poHeader.setUpdatedDate(new Date());
        }
        poHeaderRepo.save(poHeader);

        for(PoDetailEntity item : request.getPoDetailEntityList()) {
            if(item.getId()==null) {
                ItemEntity dataItem = itemRepo.findById(item.getItemId()).get();
                PoDetailEntity poDetail = new PoDetailEntity();
                poDetail.setPoHeaderId(poHeader.getId());
                poDetail.setItemId(dataItem.getId());
                poDetail.setQuantity(item.getQuantity());
                poDetail.setPrice(dataItem.getPrice() * item.getQuantity());
                poDetail.setCost(dataItem.getCost() * item.getQuantity());
                poDetailRepo.save(poDetail);
            } else {
                ItemEntity dataItem = itemRepo.findById(item.getItemId()).get();
                PoDetailEntity dataDetail = poDetailRepo.findById(item.getId()).get();
                if(!dataDetail.getItemId().equals(item.getItemId()) ||
                        !dataDetail.getQuantity().equals(item.getQuantity())) {
                    dataDetail.setItemId(dataItem.getId());
                    dataDetail.setQuantity(item.getQuantity());
                    dataDetail.setPrice(dataItem.getPrice() * item.getQuantity());
                    dataDetail.setCost(dataItem.getCost() * item.getQuantity());
                    poDetailRepo.save(dataDetail);
                }
            }
        }

        responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_CREATE + PO, poHeader);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseDto> getAllPo() {
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_GET + PO, poHeaderRepo.findAll());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> getPoById(Long id) {
        Optional<PoHeaderEntity> dataPo = poHeaderRepo.findById(id);
        if(dataPo.isEmpty()) {
            ResponseDto responseDto = ResponseUtils.getResponse(Boolean.FALSE, PO + ResponseUtils.NOT_FOUND, null);
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_GET + PO, dataPo.get());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> deletePoById(Long id) {
        Optional<PoHeaderEntity> dataPo = poHeaderRepo.findById(id);
        if(dataPo.isEmpty()) {
            ResponseDto responseDto = ResponseUtils.getResponse(Boolean.FALSE, PO + ResponseUtils.NOT_FOUND, null);
            return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
        }
        poHeaderRepo.deleteById(id);
        for(PoDetailEntity item : dataPo.get().getPoDetailEntityList()) {
            poDetailRepo.deleteById(item.getId());
        }
        ResponseDto responseDto = ResponseUtils.getResponse(Boolean.TRUE, ResponseUtils.SUCCESS_DELETE + PO, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
