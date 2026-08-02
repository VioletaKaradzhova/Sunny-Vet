package com.sunnyvet.main.service.impl;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.sunnyvet.main.domain.dto.AppointmentDto;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfExportService {

    public byte[] generateAppointmentPdf(AppointmentDto appointmentDto) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        document.add(new Paragraph("Sunny Vet Clinic - Appointment Details"));
        document.add(new Paragraph("Appointment ID: " + appointmentDto.getId()));
        document.add(new Paragraph("Reason: " + appointmentDto.getReason()));
        document.add(new Paragraph("Time: " + appointmentDto.getAppointmentTime().toString()));
        document.close();

        return outputStream.toByteArray();
    }
}