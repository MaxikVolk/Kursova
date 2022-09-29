package edu.vtc.kurs.util;

import edu.vtc.kurs.models.Address;
import edu.vtc.kurs.repositories.AddressRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddressValidator implements Validator {
    private final AddressRepository addressRepository;

    public AddressValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Address address = (Address) target;
        if(addressRepository.existsBySettlementAndStreetAndHouseNumberAndFlatNumber
                (address.getSettlement(),address.getStreet(),address.getHouseNumber(),address.getFlatNumber())){
            errors.rejectValue("settlement","","This address already exists");
        }
    }
}
