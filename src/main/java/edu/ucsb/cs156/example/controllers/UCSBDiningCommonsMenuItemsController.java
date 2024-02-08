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
@RequestMapping("/api/ucsbdiningcommonsmenuitem")
@RestController
@Slf4j
public class UCSBDiningCommonsMenuItemsController extends ApiController {

    @Autowired
    UCSBDiningCommonsMenuItemsRepository ucsbDiningCommonsMenuItemsRepository;

    @Operation(summary= "List all ucsb dining commonsmenuitem")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBDiningCommonsMenuItems> allCommonsMenuItems() {
        Iterable<UCSBDiningCommonsMenuItems> commonsmenuitem = ucsbDiningCommonsMenuItemsRepository.findAll();
        return commonsmenuitem;
    }

    @Operation(summary= "Create a new commonsmenuitem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBDiningCommonsMenuItems postCommonsMenuItems(
        @Parameter(name="code") @RequestParam String code,
        @Parameter(name="name") @RequestParam String name,
        @Parameter(name="station") @RequestParam String station     
        )
        {

        UCSBDiningCommonsMenuItems commonsmenuitem = new UCSBDiningCommonsMenuItems();
        commonsmenuitem.setCode(code);
        commonsmenuitem.setName(name);
        commonsmenuitem.setStation(station);

        UCSBDiningCommonsMenuItems savedCommonsMenuItems = ucsbDiningCommonsMenuItemsRepository.save(commonsmenuitem);

        return savedCommonsMenuItems;
    }

    @Operation(summary= "Get a single commonsmenuitem")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public UCSBDiningCommonsMenuItems getById(
            @Parameter(name="id") @RequestParam Long id) {
        UCSBDiningCommonsMenuItems commonsmenuitem = ucsbDiningCommonsMenuItemsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItems.class, id));

        return commonsmenuitem;
    }
/*
    @Operation(summary= "Delete a UCSBDiningCommonsMenuItems")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteCommonsMenuItems(
            @Parameter(name="code") @RequestParam String code) {
        UCSBDiningCommonsMenuItems commonsmenuitem = ucsbDiningCommonsMenuItemsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItems.class, code));

        ucsbDiningCommonsMenuItemsRepository.delete(commonsmenuitem);
        return genericMessage("UCSBDiningCommonsMenuItems with id %s deleted".formatted(code));
    }

    @Operation(summary= "Update a single commonsmenuitem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public UCSBDiningCommonsMenuItems updateCommonsMenuItems(
            @Parameter(name="code") @RequestParam String code,
            @RequestBody @Valid UCSBDiningCommonsMenuItems incoming) {

        UCSBDiningCommonsMenuItems commonsmenuitem = ucsbDiningCommonsMenuItemsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItems.class, code));


        commonsmenuitem.setName(incoming.getName());  
        commonsmenuitem.setHasSackMeal(incoming.getHasSackMeal());
        commonsmenuitem.setHasTakeOutMeal(incoming.getHasTakeOutMeal());
        commonsmenuitem.setHasDiningCam(incoming.getHasDiningCam());
        commonsmenuitem.setLatitude(incoming.getLatitude());
        commonsmenuitem.setLongitude(incoming.getLongitude());

        ucsbDiningCommonsMenuItemsRepository.save(commonsmenuitem);

        return commonsmenuitem;
    }*/
}
