package com.taekang.userservletapi.service.employee.impl;

import com.taekang.userservletapi.DTO.employee.SiteDTO;
import com.taekang.userservletapi.entity.employee.Site;
import com.taekang.userservletapi.repository.employee.SiteRepository;
import com.taekang.userservletapi.service.employee.SiteService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SiteServiceImpl(SiteRepository siteRepository, ModelMapper modelMapper) {
        this.siteRepository = siteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SiteDTO> getAllSite() {
        return siteRepository.findByDeleteDateTimeIsNull().stream()
                .map(site -> modelMapper.map(site, SiteDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SiteDTO getBySite(String site) {
        Site siteObject = siteRepository.findBySite(site).orElseThrow();
        return modelMapper.map(siteObject, SiteDTO.class);
    }
}

