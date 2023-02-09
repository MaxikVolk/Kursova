package edu.vtc.kurs.services;

import edu.vtc.kurs.dto.StreetDTO;
import edu.vtc.kurs.models.Settlement;
import edu.vtc.kurs.models.Street;
import edu.vtc.kurs.repositories.StreetRepository;
import edu.vtc.kurs.services.StreetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StreetServiceTest {

    @Mock
    private StreetRepository streetRepository;

    @InjectMocks
    private StreetService streetService;

    @Test
    public void testFindAll() {
        List<Street> expectedStreets = List.of(new Street(), new Street());
        when(streetRepository.findAll()).thenReturn(expectedStreets);

        List<Street> actualStreets = streetService.findAll();

        assertNotNull(actualStreets);
        assertEquals(expectedStreets.size(), actualStreets.size());
        assertEquals(expectedStreets, actualStreets);
    }

    @Test
    public void testSave() {
        StreetDTO streetDTO = StreetDTO.builder().name("name").settlement(new Settlement()).build();

        when(streetRepository.save(any())).thenReturn(any());

        streetService.save(streetDTO);

        verify(streetRepository).save(any());
    }

    @Test
    public void testFindById() {
        Street expectedStreet = new Street();
        when(streetRepository.findById(1L)).thenReturn(Optional.of(expectedStreet));

        Street actualStreet = streetService.findById(1L);

        assertNotNull(actualStreet);
        assertEquals(expectedStreet, actualStreet);
    }
}