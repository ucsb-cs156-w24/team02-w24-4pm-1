package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItems;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "UCSBDiningCommonsMenuItems")
@RequestMapping("/api/ucsbdiningcommonsmenuitems")
@RestController
@Slf4j
public class UCSBDiningCommonsMenuItemsController extends ApiController {

    @Autowired
    UCSBDiningCommonsMenuItemsRepository ucsbDiningCommonsMenuItemsRepository;

    @Operation(summary= "List all ucsb dining commonsmenuitems")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBDiningCommonsMenuItems> allCommonsMenuItemss() {
        Iterable<UCSBDiningCommonsMenuItems> commonsmenuitems = ucsbDiningCommonsMenuItemsRepository.findAll();
        return commonsmenuitems;
    }

    @Operation(summary= "Create a new commonsmenuitems")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBDiningCommonsMenuItems postCommonsMenuItems(
        @Parameter(name="code") @RequestParam String code,
        @Parameter(name="name") @RequestParam String name,
        @Parameter(name="station") @RequestParam String station     
        )
        {

        UCSBDiningCommonsMenuItems commonsmenuitems = new UCSBDiningCommonsMenuItems();
        commonsmenuitems.setCode(code);
        commonsmenuitems.setName(name);
        commonsmenuitems.setStation(station);

        UCSBDiningCommonsMenuItems savedCommonsMenuItems = ucsbDiningCommonsMenuItemsRepository.save(commonsmenuitems);

        return savedCommonsMenuItems;
    }
/*
    @Operation(summary= "Get a single commonsmenuitems")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public UCSBDiningCommonsMenuItems getById(
            @Parameter(name="code") @RequestParam String code) {
        UCSBDiningCommonsMenuItems commonsmenuitems = ucsbDiningCommonsMenuItemsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItems.class, code));

        return commonsmenuitems;
    }

    @Operation(summary= "Delete a UCSBDiningCommonsMenuItems")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteCommonsMenuItems(
            @Parameter(name="code") @RequestParam String code) {
        UCSBDiningCommonsMenuItems commonsmenuitems = ucsbDiningCommonsMenuItemsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItems.class, code));

        ucsbDiningCommonsMenuItemsRepository.delete(commonsmenuitems);
        return genericMessage("UCSBDiningCommonsMenuItems with id %s deleted".formatted(code));
    }

    @Operation(summary= "Update a single commonsmenuitems")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public UCSBDiningCommonsMenuItems updateCommonsMenuItems(
            @Parameter(name="code") @RequestParam String code,
            @RequestBody @Valid UCSBDiningCommonsMenuItems incoming) {

        UCSBDiningCommonsMenuItems commonsmenuitems = ucsbDiningCommonsMenuItemsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItems.class, code));


        commonsmenuitems.setName(incoming.getName());  
        commonsmenuitems.setHasSackMeal(incoming.getHasSackMeal());
        commonsmenuitems.setHasTakeOutMeal(incoming.getHasTakeOutMeal());
        commonsmenuitems.setHasDiningCam(incoming.getHasDiningCam());
        commonsmenuitems.setLatitude(incoming.getLatitude());
        commonsmenuitems.setLongitude(incoming.getLongitude());

        ucsbDiningCommonsMenuItemsRepository.save(commonsmenuitems);

        return commonsmenuitems;
    }*/
}
