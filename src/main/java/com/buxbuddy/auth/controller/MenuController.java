package com.buxbuddy.auth.controller;

import com.buxbuddy.auth.dto.menu.MenuRequest;
import com.buxbuddy.auth.dto.menu.MenuResponse;
import com.buxbuddy.auth.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuResponse create(
            @RequestBody MenuRequest request){
        return menuService.create(request);
    }

    @GetMapping
    public List<MenuResponse> getAll(){
        return menuService.getAll();
    }

    @GetMapping("/{id}")
    public MenuResponse getById(
            @PathVariable Long id){
        return menuService.getById(id);
    }

    @PutMapping("/{id}")
    public MenuResponse update(
            @PathVariable Long id,
            @RequestBody MenuRequest request){
        return menuService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id){
        menuService.delete(id);
        return "Menu deleted successfully";
    }
}