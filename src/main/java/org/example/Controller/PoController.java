package org.example.Controller;

import org.example.Dto.ResponseDto;
import org.example.Entity.ItemEntity;
import org.example.Entity.PoHeaderEntity;
import org.example.Service.ItemService;
import org.example.Service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/api/po")
public class PoController {

    @Autowired
    private PoService poService;

    @PostMapping("/create-update")
    public ResponseEntity<ResponseDto> createPo(@RequestBody PoHeaderEntity po) {
        return poService.createUpdatePo(po);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseDto> getAllPo() {
        return poService.getAllPo();
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ResponseDto> getPoById(@PathVariable Long id) {
        return poService.getPoById(id);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<ResponseDto> deletePoById(@PathVariable Long id) {
        return poService.deletePoById(id);
    }
}
