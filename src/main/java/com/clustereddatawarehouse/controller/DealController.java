package com.clustereddatawarehouse.controller;

import com.clustereddatawarehouse.annotation.ResponseWrapper;
import com.clustereddatawarehouse.service.DealService;
import com.clustereddatawarehouse.service.dto.DealDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@ResponseWrapper()
@RestController
@RequestMapping("/v1/api/")
public class DealController {
    private final Logger log = LoggerFactory.getLogger(DealController.class);
    private static final String APPLICATION_NAME = "ClusteredDataWarehouse";
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping("/deals")
    public ResponseEntity<DealDTO> createDeal(@Valid @RequestBody DealDTO dealDTO) throws URISyntaxException {
        log.debug("REST request to save deal : {}", dealDTO);
        DealDTO deal = dealService.saveDeal(dealDTO);
        return ResponseEntity
                .created(new URI(String.format("%s%s%d", APPLICATION_NAME, "/deal/", deal.getId())))
                .body(deal);
    }

    @GetMapping("/deal/{id}")
    public ResponseEntity<DealDTO> retrieveDealById( @PathVariable("id") Long id) {
        log.debug("REST request to get deal by : {}", id);
        DealDTO dealDTO = dealService.getDealById(id);
        return ResponseEntity.ok(dealDTO);
    }

    @GetMapping("/deals")
    public ResponseEntity<List<DealDTO>> retrieveAllDeals() {
        log.debug("REST request to get list of deals");
        return ResponseEntity.ok().body(dealService.getAllDeals());
    }
}
