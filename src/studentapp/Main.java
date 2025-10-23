package studentapp;

import studentapp.ui.MainFrame;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperCompileManager;

public class Main {
    public static void main(String[] args) {
        try {
            // 🔹 Kompilasi file laporan (jalankan sekali saja)
            String sourceFile = "src/studentapp/reports/student_report.jrxml";
            String destFile = "src/studentapp/reports/student_report.jasper";
            JasperCompileManager.compileReportToFile(sourceFile, destFile);
            System.out.println("✅ File laporan berhasil dikompilasi menjadi student_report.jasper!");
        } catch (Exception e) {
            System.out.println("⚠️ Gagal mengompilasi laporan: " + e.getMessage());
            e.printStackTrace();
        }


        // 🔹 Jalankan GUI utama aplikasi
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
