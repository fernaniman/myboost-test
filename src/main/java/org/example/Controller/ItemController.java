package org.example.Controller;

import org.example.Dto.ResponseDto;
import org.example.Entity.ItemEntity;
import org.example.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/create-update")
    public ResponseEntity<ResponseDto> createItem(@RequestBody ItemEntity item) {
        return itemService.createUpdateItem(item);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseDto> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ResponseDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<ResponseDto> deleteItemById(@PathVariable Long id) {
        return itemService.deleteItemById(id);
    }
}
