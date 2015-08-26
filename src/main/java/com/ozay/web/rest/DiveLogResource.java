package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Building;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.MemberRepository;
import com.ozay.web.rest.dto.DiveLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class DiveLogResource {

    private final Logger log = LoggerFactory.getLogger(DiveLogResource.class);

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private BuildingRepository buildingRepository;

    /**
     * GET  /rest/DiveLog/building/{buildingId}/{id} -> get the "Building" by bu
     */
    @RequestMapping(value = "/DiveLog",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiveLog> getMemberDetail(@RequestParam(value = "building") Long buildingId) {
        DiveLog DiveLog = new DiveLog();

        Building building = buildingRepository.getBuilding(buildingId);

        DiveLog.setTotalUnits(building.getTotalUnits());
        DiveLog.setActiveUnits(memberRepository.countActiveUnits(buildingId));

        return new ResponseEntity<DiveLog>(DiveLog,  new HttpHeaders(), HttpStatus.OK);
    }
}
