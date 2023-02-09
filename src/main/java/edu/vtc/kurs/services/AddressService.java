package edu.vtc.kurs.services;

import edu.vtc.kurs.dto.AddressDTO;
import edu.vtc.kurs.exceptions.Status410WrongStreetException;
import edu.vtc.kurs.exceptions.Status404StreetNotFoundException;
import edu.vtc.kurs.models.Address;
import edu.vtc.kurs.repositories.AddressRepository;
import edu.vtc.kurs.repositories.StreetRepository;
import edu.vtc.kurs.util.AddressValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Address service.
 */
@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final StreetRepository streetRepository;
    private final AddressValidator addressValidator;

    /**
     * Instantiates a new Address service.
     *
     * @param addressRepository the address repository
     * @param streetRepository  the street repository
     * @param addressValidator  the address validator
     */
    public AddressService(AddressRepository addressRepository, StreetRepository streetRepository, AddressValidator addressValidator) {
        this.addressRepository = addressRepository;
        this.streetRepository = streetRepository;
        this.addressValidator = addressValidator;
    }

    /**
     * Maps addressDTO to an address object
     *
     * @param addressDTO dto which we want to map
     * @return Mapped address
     * @throws Status404StreetNotFoundException the status 404 street not found exception
     * @throws Status410WrongStreetException    the status 403 wrong street exception
     */
    public Address map(AddressDTO addressDTO) throws Status404StreetNotFoundException, Status410WrongStreetException {
        Address address = new Address();
        address.setSettlement(addressDTO.getSettlement());
        address.setFlatNumber(addressDTO.getFlatNumber());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setStreet(streetRepository.findStreetByNameAndSettlement(addressDTO.getStreet(), addressDTO.getSettlement()));
        if (address.getStreet() == null) {
            throw new Status404StreetNotFoundException();
        }
        if (address.getStreet().getSettlement().getId() != address.getSettlement().getId()) {
            throw new Status410WrongStreetException();
        }
        return address;
    }

    /**
     * Saves address to DB
     *
     * @param address address which we are saving
     */
    public void save(Address address) {
        addressRepository.save(address);
    }

    /**
     * Validates addressDTO and if everything is ok, using edit method
     *
     * @param id         id of address which we want to change
     * @param addressDTO new data about address which we want to change
     * @param errors     errors variable
     * @throws Status404StreetNotFoundException the status 404 street not found exception
     * @throws Status410WrongStreetException    the status 403 wrong street exception
     */
    @Transactional
    public void save(long id, AddressDTO addressDTO, Errors errors) throws Status404StreetNotFoundException, Status410WrongStreetException {
        Address address = map(addressDTO);
        addressValidator.validate(address, errors);
        if (errors.hasErrors()) {
            return;
        }
        edit(id, address);
    }

    /**
     * Changes chosen address using given addressDTO info
     *
     * @param id         id of address which we want to change
     * @param addressDTO new data about address which we want to change
     * @see Address
     */
    public void edit(long id, Address addressDTO) {
        Address address = addressRepository.findById(id).orElseThrow();
        address.setStreet(addressDTO.getStreet());
        address.setSettlement(addressDTO.getSettlement());
        address.setFlatNumber(addressDTO.getFlatNumber());
        address.setHouseNumber(addressDTO.getHouseNumber());
    }

    /**
     * Returns list of all addresses from database
     *
     * @return list of all addresses , sorted in the way we need
     * @see Address
     */
    public List<Address> findAll() {
        List<Address> addresses = addressRepository.findAll();
        Collections.reverse(addresses);
        return addresses;
    }

    /**
     * Returns list of all addresses from database,
     * sorted in the way which we send as param
     *
     * @param sort type of sorting
     * @return list of all addresses , sorted in the way we need
     * @see Address
     */
    public List<Address> findAll(String sort) {
        List<Address> addresses = addressRepository.findAll();
        switch (sort) {
            case "settlementName" -> addresses.sort(Comparator.comparing(a -> a.getSettlement().getName()));
            case "regionName" -> addresses.sort(Comparator.comparing(a -> a.getSettlement().getRegion()));
            case "streetName" -> addresses.sort(Comparator.comparing(a -> a.getStreet().getName()));
            case "houseNumber" -> addresses.sort(Comparator.comparing(Address::getHouseNumber));
        }
        return addresses;
    }

    /**
     * Deletes address from database
     *
     * @param id id of address we want to delete
     * @see Address
     */
    public void delete(long id) {
        addressRepository.deleteById(id);
    }

    /**
     * Looking for address in database for id
     *
     * @param id id of address we want to find
     * @return found address
     * @see Address
     */
    public Address findById(long id) {
        return addressRepository.findById(id).orElse(null);
    }

    /**
     * Returns list of addresses using filters and sorting from params
     *
     * @param sort            sorting type
     * @param region          region filter
     * @param settlement      settlement filter
     * @param street          street filter
     * @param houseNumberMore filter, house number must be more than this
     * @param houseNumberLess filter, house number must be less than this
     * @return list of addresses
     * @see Address
     */
    public List<Address> findSortedAndFiltered(String sort, String region,
                                               String settlement, String street,
                                               int houseNumberMore, int houseNumberLess) {
        List<Address> addresses;
        if (sort == null) {
            addresses = findAll();
        } else {
            addresses = findAll(sort);
        }
        if (region != null && !region.isBlank()) {
            addresses = addresses.stream().filter(address ->
                    region.equals(address.getSettlement().getRegion())).collect(Collectors.toList());
        }
        if (settlement != null && !settlement.isBlank()) {
            addresses = addresses.stream().filter(address ->
                    address.getSettlement().getName().startsWith(settlement)).collect(Collectors.toList());
        }
        if (street != null && !street.isBlank()) {
            addresses = addresses.stream().filter(address ->
                    address.getStreet().getName().startsWith(street)).collect(Collectors.toList());
        }
        if (houseNumberMore > 0) {
            addresses = addresses.stream().filter(address -> houseNumberMore < address.getHouseNumber())
                    .collect(Collectors.toList());
        }
        if (houseNumberLess > 0) {
            addresses = addresses.stream().filter(address -> houseNumberLess > address.getHouseNumber())
                    .collect(Collectors.toList());
        }
        return addresses;
    }

    /**
     * Looking for address in database for id and mapping it to DTO
     *
     * @param id id of address we want to find
     * @return found address mapped to DTO
     * @see Address
     */
    public AddressDTO findAddressAndMapToDTO(long id) {
        Address address = findById(id);
        return new AddressDTO(address.getSettlement(),
                address.getStreet().getName(), address.getHouseNumber(), address.getFlatNumber());
    }
}
