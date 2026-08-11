package com.sunnyvet.main.web;

import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorWebControllerTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private Model model;

    @InjectMocks
    private DoctorWebController doctorWebController;

    @Test
    void testDeleteDoctor_Unit() {
        UUID docId = UUID.randomUUID();
        Doctor doctor = new Doctor();
        doctor.setId(docId);

        when(doctorRepository.findById(docId)).thenReturn(java.util.Optional.of(doctor));

        String viewName = doctorWebController.deleteDoctor(docId);

        assertEquals("redirect:/dashboard", viewName);
        verify(doctorRepository, times(1)).deleteById(docId);
    }
}