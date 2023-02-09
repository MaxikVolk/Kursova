package edu.vtc.kurs.controllers;

import edu.vtc.kurs.dto.SettlementDTO;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.services.SettlementService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/settlement")
public class SettlementController {
    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @GetMapping()
    public String getSettlements(Model model) {
        model.addAttribute("settlements", settlementService.findAll());
        model.addAttribute("regions", settlementService.findAll().stream()
                .map(Settlement::getRegion).collect(Collectors.toSet()));
        return "settlement/all";
    }

    @GetMapping("/{id}")
    public String getSettlement(@PathVariable long id, Model model) {
        model.addAttribute("settlement", settlementService.getSettlement(id));
        return "settlement/settlement";
    }

    @GetMapping("/sorted")
    public String getSettlements(Model model, @RequestParam @Nullable String sort, @RequestParam @Nullable String region) {
        model.addAttribute("settlements", settlementService.findSortedAndFiltered(sort, region));
        model.addAttribute("regions", settlementService.findAll().stream()
                .map(Settlement::getRegion).collect(Collectors.toSet()));
        return "settlement/all";
    }

    @GetMapping("/newSettlement")
    public String newSettlement(Model model) {
        model.addAttribute("settlement", new SettlementDTO());
        return "settlement/newSettlement";
    }

    @PostMapping("/newSettlement")
    public String newSettlement(@ModelAttribute("settlement") @Valid SettlementDTO settlementDTO,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "settlement/newSettlement";
        }
        settlementService.save(settlementDTO);
        return "redirect:/settlement";
    }

    @GetMapping("/{id}/edit")
    public String editSettlement(@PathVariable long id, Model model) {
        model.addAttribute("settlement", settlementService.findById(id));
        return "settlement/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editSettlement(@PathVariable long id, @Valid SettlementDTO settlementDTO,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("settlement", settlementService.findById(id));
            return "settlement/edit";
        }
        settlementService.edit(id, settlementDTO);
        return "redirect:/settlement";
    }

    @GetMapping("/{id}/delete")
    public String deleteSettlement(@PathVariable long id) {
        settlementService.deleteById(id);
        return "redirect:/settlement";
    }
}