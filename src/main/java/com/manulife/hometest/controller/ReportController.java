package com.manulife.hometest.controller;

import com.manulife.hometest.entity.User;
import com.manulife.hometest.service.JasperReportService;
import com.manulife.hometest.service.UserService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/user-report")
    public ResponseEntity<InputStreamResource> generateUserReport(@RequestParam(required = false) List<Long> ids) throws JRException, IOException {
        List<User> users;

        if (ids != null && !ids.isEmpty()) {
            users = userService.getUsersByIds(ids);
        } else {
            users = userService.getAllUsers();
        }

        JasperPrint jasperPrint = jasperReportService.generateReport(users);
        return generatePdfResponse(jasperPrint);
    }

    @GetMapping("/user-report/{id}")
    public ResponseEntity<InputStreamResource> generateUserReportById(@PathVariable Long id) throws JRException, IOException {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            JasperPrint jasperPrint = jasperReportService.generateReport(List.of(userOpt.get()));
            return generatePdfResponse(jasperPrint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<InputStreamResource> generatePdfResponse(JasperPrint jasperPrint) throws JRException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource resource = new InputStreamResource(bis);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(out.size())
                .body(resource);
    }
}
