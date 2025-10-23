package studentapp.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {

    public static void generateStudentReport(int fromNim, int toNim) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            // Lokasi file .jrxml dan output PDF
            String reportPath = "src/studentapp/reports/student_report.jrxml";
            String compiledPath = "src/studentapp/reports/student_report.jasper";

            // Compile JRXML (jika belum dikompilasi)
            JasperCompileManager.compileReportToFile(reportPath, compiledPath);

            // Parameter
            Map<String, Object> params = new HashMap<>();
            params.put("FROM_NIM", fromNim);
            params.put("TO_NIM", toNim);

            // Fill data dan tampilkan preview
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledPath, params, conn);

            // Export ke PDF otomatis
            JasperExportManager.exportReportToPdfFile(jasperPrint, "student_report.pdf");

            // (opsional) tampilkan ke viewer
            JasperViewer.viewReport(jasperPrint, false);

            System.out.println("✅ Laporan berhasil dibuat: student_report.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
