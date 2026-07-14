package com.buxbuddy.auth.service.impl;

import com.buxbuddy.auth.dto.menu.MenuRequest;
import com.buxbuddy.auth.dto.menu.MenuResponse;
import com.buxbuddy.auth.entity.Menu;
import com.buxbuddy.auth.repository.MenuRepository;
import com.buxbuddy.auth.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {


    private final MenuRepository menuRepository;

    @Override
    public MenuResponse create(MenuRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .path(request.getPath())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder())
                .active(request.isActive())
                .businessCategory(request.getBusinessCategory())
                .role(request.getRole())
                .build();


        return mapToResponse(menuRepository.save(menu));
    }

    @Override
    public List<MenuResponse> getAll() {

        return menuRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MenuResponse getById(Long id) {

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Menu not found"));

        return mapToResponse(menu);
    }

    @Override
    public MenuResponse update(Long id, MenuRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Menu not found"));
        menu.setName(request.getName());
        menu.setPath(request.getPath());
        menu.setIcon(request.getIcon());
        menu.setDisplayOrder(request.getDisplayOrder());
        menu.setActive(request.isActive());
        menu.setBusinessCategory(request.getBusinessCategory());
        menu.setRole(request.getRole());
        return mapToResponse(menuRepository.save(menu));
    }

    @Override
    public void delete(Long id) {

        if(!menuRepository.existsById(id)){
            throw new RuntimeException("Menu not found");
        }
        menuRepository.deleteById(id);
    }



    private MenuResponse mapToResponse(Menu menu){
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .path(menu.getPath())
                .icon(menu.getIcon())
                .displayOrder(menu.getDisplayOrder())
                .active(menu.isActive())
                .businessCategory(menu.getBusinessCategory())
                .role(menu.getRole())
                .build();
    }
}