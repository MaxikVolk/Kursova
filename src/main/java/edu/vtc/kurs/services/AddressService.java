package edu.vtc.kurs.services;

import edu.vtc.kurs.dto.AddressDTO;
import edu.vtc.kurs.exceptions.Status403WrongStreetException;
import edu.vtc.kurs.exceptions.Status404StreetNotFoundException;
import edu.vtc.kurs.models.Address;
import edu.vtc.kurs.models.Street;
import edu.vtc.kurs.repositories.AddressRepository;
import edu.vtc.kurs.repositories.StreetRepository;
import edu.vtc.kurs.util.AddressValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final StreetRepository streetRepository;
    private final AddressValidator addressValidator;

    public AddressService(AddressRepository addressRepository, StreetRepository streetRepository, AddressValidator addressValidator) {
        this.addressRepository = addressRepository;
        this.streetRepository = streetRepository;
        this.addressValidator = addressValidator;
    }
    public Address map(AddressDTO addressDTO) throws Status404StreetNotFoundException, Status403WrongStreetException {
        Address address = new Address();
        address.setSettlement(addressDTO.getSettlement());
        address.setFlatNumber(addressDTO.getFlatNumber());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setStreet(streetRepository.findStreetByNameAndSettlement(addressDTO.getStreet(),addressDTO.getSettlement()));
        if (address.getStreet() == null) {
            throw new Status404StreetNotFoundException();
        }
        if(address.getStreet().getSettlement().getId()!=address.getSettlement().getId()){
            throw new Status403WrongStreetException();
        }
        return address;
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    @Transactional
    public void save(long id, AddressDTO addressDTO, Errors errors) throws Status404StreetNotFoundException, Status403WrongStreetException {
        Address address = map(addressDTO);
        addressValidator.validate(address, errors);
        if (errors.hasErrors()) {
            return;
        }
        edit(id, address);
    }

    public void edit(long id, Address addressDTO) {
        Address address = addressRepository.findById(id).orElseThrow();
        address.setStreet(addressDTO.getStreet());
        address.setSettlement(addressDTO.getSettlement());
        address.setFlatNumber(addressDTO.getFlatNumber());
        address.setHouseNumber(addressDTO.getHouseNumber());
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public List<Address> findAll(String sort) {
        List<Address> addresses = addressRepository.findAll();
        switch (sort){
            case "settlementName"-> addresses.sort(Comparator.comparing(a -> a.getSettlement().getName()));
            case "regionName"->addresses.sort(Comparator.comparing(a->a.getSettlement().getRegion()));
            case "streetName"->addresses.sort(Comparator.comparing(a->a.getStreet().getName()));
            case "houseNumber"->addresses.sort(Comparator.comparing(Address::getHouseNumber));
        }
        return addresses;
    }

    public void delete(long id) {
        addressRepository.deleteById(id);
    }

    public Address findById(long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public List<Address> findSortedAndFiltered(String sort, String region,
                                               String settlement, String street,
                                               int houseNumberMore, int houseNumberLess) {
        List<Address> addresses;
        if (sort == null) {
            addresses = findAll();
        } else {
            addresses = findAll(sort);
        }
        if(region!=null && !region.isBlank()){
            addresses = addresses.stream().filter(address ->
                    region.equals(address.getSettlement().getRegion())).collect(Collectors.toList());
        }
        if(settlement!=null && !settlement.isBlank()){
            addresses = addresses.stream().filter(address ->
                    address.getSettlement().getName().startsWith(settlement)).collect(Collectors.toList());
        }
        if(street!=null && !street.isBlank()){
            addresses = addresses.stream().filter(address ->
                    address.getStreet().getName().startsWith(street)).collect(Collectors.toList());
        }
        if(houseNumberMore!=0){
            addresses = addresses.stream().filter(address -> houseNumberMore<address.getHouseNumber())
                    .collect(Collectors.toList());
        }
        if(houseNumberLess!=0){
            addresses = addresses.stream().filter(address -> houseNumberLess>address.getHouseNumber())
                    .collect(Collectors.toList());
        }
        return addresses;
    }

    public AddressDTO findForEdit(long id) {
        Address address = findById(id);
        return new AddressDTO(address.getSettlement(),
                address.getStreet().getName(),address.getHouseNumber(),address.getFlatNumber());
    }
}
