package edu.vtc.kurs.controllers;

import edu.vtc.kurs.dto.FilterDTO;
import edu.vtc.kurs.dto.StreetDTO;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.services.SettlementService;
import edu.vtc.kurs.services.StreetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/street")
public class StreetController {
    private final StreetService streetService;
    private final SettlementService settlementService;

    public StreetController(StreetService streetService, SettlementService settlementService) {
        this.streetService = streetService;
        this.settlementService = settlementService;
    }

    @GetMapping()
    public String getStreets(Model model) {
        model.addAttribute("streets", streetService.findAll());
        model.addAttribute("regions", settlementService.findAll().stream()
                .map(Settlement::getRegion).collect(Collectors.toSet()));
        model.addAttribute("filter", new FilterDTO());
        return "street/all";
    }

    @GetMapping("/sorted")
    public String getStreets(Model model, @ModelAttribute FilterDTO filter) {
        model.addAttribute("filter", filter);
        model.addAttribute("streets", streetService.findSortedAndFiltered
                (filter.getSort(), filter.getRegion(), filter.getSettlement()));
        model.addAttribute("regions", settlementService.findAll().stream()
                .map(Settlement::getRegion).collect(Collectors.toSet()));
        return "street/all";
    }

    @GetMapping("/newStreet")
    public String newStreet(Model model) {
        model.addAttribute("street", new StreetDTO());
        model.addAttribute("settlements", settlementService.findAll());
        return "street/newStreet";
    }

    @PostMapping("/newStreet")
    public String newStreet(@ModelAttribute("street") @Valid StreetDTO streetDTO,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlements", settlementService.findAll());
            return "street/newStreet";
        }
        streetService.save(streetDTO);
        return "redirect:/street";
    }

    @GetMapping("/{id}/edit")
    public String editStreet(@PathVariable long id, Model model) {
        model.addAttribute("settlements", settlementService.findAll());
        model.addAttribute("street", streetService.findById(id));
        return "street/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editStreet(@PathVariable long id, @Valid StreetDTO streetDTO,
                             BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlements", settlementService.findAll());
            model.addAttribute("street", streetService.findById(id));
            return "/street/edit";
        }
        streetService.edit(id, streetDTO);
        return "redirect:/street";
    }

    @GetMapping("/{id}/delete")
    public String deleteStreet(@PathVariable long id) {
        streetService.deleteById(id);
        return "redirect:/street";
    }
}