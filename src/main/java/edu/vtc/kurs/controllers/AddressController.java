package edu.vtc.kurs.controllers;

import edu.vtc.kurs.dto.AddressDTO;
import edu.vtc.kurs.dto.FilterDTO;
import edu.vtc.kurs.exceptions.Status403WrongStreetException;
import edu.vtc.kurs.exceptions.Status404StreetNotFoundException;
import edu.vtc.kurs.models.Address;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.services.AddressService;
import edu.vtc.kurs.services.SettlementService;
import edu.vtc.kurs.services.StreetService;
import edu.vtc.kurs.util.AddressValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;
    private final SettlementService settlementService;
    private final AddressValidator addressValidator;

    public AddressController(AddressService addressService, SettlementService settlementService, AddressValidator addressValidator) {
        this.addressService = addressService;
        this.settlementService = settlementService;
        this.addressValidator = addressValidator;
    }

    @GetMapping()
    public String getAddresses(Model model) {
        model.addAttribute("addresses", addressService.findAll());
        model.addAttribute("regions", settlementService.findAll().stream()
                .map(Settlement::getRegion).collect(Collectors.toSet()));
        model.addAttribute("filter", new FilterDTO());
        return "address/all";
    }
    @GetMapping("/sorted")
    public String getAddresses(Model model, @ModelAttribute @Valid FilterDTO filter,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("addresses", addressService.findAll());
            model.addAttribute("regions", settlementService.findAll().stream()
                    .map(Settlement::getRegion).collect(Collectors.toSet()));
            model.addAttribute("filter", new FilterDTO());
        }
        model.addAttribute("filter", filter);
        model.addAttribute("addresses", addressService.findSortedAndFiltered
                (filter.getSort(),filter.getRegion(),filter.getSettlement(),filter.getStreet()
                        ,filter.getHouseNumberMore(),filter.getHouseNumberLess()));
        model.addAttribute("regions", settlementService.findAll().stream()
                .map(Settlement::getRegion).collect(Collectors.toSet()));
        return "address/all";
    }

    @GetMapping("/newAddress")
    public String newAddress(Model model) {
        model.addAttribute("address", new AddressDTO());
        model.addAttribute("settlements", settlementService.findAll());
        return "address/newAddress";
    }

    @PostMapping("/newAddress")
    public String newAddress(@ModelAttribute("address") @Valid AddressDTO addressDTO,
                             BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlements", settlementService.findAll());
            return "address/newAddress";
        }
        Address address;
        try {
            address = addressService.map(addressDTO);
        }
        catch (Status404StreetNotFoundException ex){
            model.addAttribute("settlements", settlementService.findAll());
            bindingResult.rejectValue("street","404","This street is not found");
            return "address/newAddress";
        } catch (Status403WrongStreetException e) {
            model.addAttribute("settlements", settlementService.findAll());
            bindingResult.rejectValue("street","403","There isn`t such street in this settlement");
            return "address/newAddress";
        }
        addressValidator.validate(address, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlements", settlementService.findAll());
            return "address/newAddress";
        }
        addressService.save(address);
        return "redirect:/address";
    }

    @GetMapping("/{id}/edit")
    public String editAddress(@PathVariable long id, Model model) {
        model.addAttribute("settlements", settlementService.findAll());
        model.addAttribute("address", addressService.findForEdit(id));
        return "address/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editAddress(@PathVariable long id,@ModelAttribute("address") @Valid AddressDTO addressDTO,
                              BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlements", settlementService.findAll());
            model.addAttribute("address", addressService.findById(id));
            return "address/edit";
        }
        try{
            addressService.save(id,addressDTO,bindingResult);
        }
        catch (Status404StreetNotFoundException ex){
            model.addAttribute("settlements", settlementService.findAll());
            model.addAttribute("address", addressService.findById(id));
            bindingResult.rejectValue("street","404","This street is not found");
            return "address/edit";
        } catch (Status403WrongStreetException e) {
            model.addAttribute("settlements", settlementService.findAll());
            bindingResult.rejectValue("street","403",e.getMessage());
            return "address/newAddress";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlements", settlementService.findAll());
            model.addAttribute("address", addressService.findById(id));
            return "address/edit";
        }
        return "redirect:/address";
    }

    @GetMapping("/{id}/delete")
    public String deleteAddress(@PathVariable long id) {
        addressService.delete(id);
        return "redirect:/address";
    }
}