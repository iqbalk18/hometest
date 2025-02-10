package com.manulife.hometest.service;

import com.manulife.hometest.entity.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    public JasperPrint generateReport(List<User> users) throws JRException {
        InputStream reportStream = getClass().getResourceAsStream("/reports/userReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "User List");

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }
}
