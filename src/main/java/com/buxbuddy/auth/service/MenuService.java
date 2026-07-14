package com.buxbuddy.auth.service;

import com.buxbuddy.auth.dto.menu.MenuRequest;
import com.buxbuddy.auth.dto.menu.MenuResponse;
import java.util.List;

public interface MenuService {

    MenuResponse create(MenuRequest request);

    List<MenuResponse> getAll();

    MenuResponse getById(Long id);

    MenuResponse update(Long id, MenuRequest request);

    void delete(Long id);
}