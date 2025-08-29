package com.taekang.userservletapi.service.employee;

import com.taekang.userservletapi.DTO.employee.SiteDTO;

import java.util.List;

public interface SiteService {

    List<SiteDTO> getAllSite();

    SiteDTO getBySite(String site);
}
