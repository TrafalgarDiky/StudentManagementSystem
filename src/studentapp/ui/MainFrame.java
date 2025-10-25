package studentapp.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import studentapp.dao.StudentDAO;
import studentapp.model.Student;
import java.sql.SQLException;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import studentapp.util.DatabaseConnector;

/**
 * Student Management System
 * Author: Diky Firmansyah
 * Deskripsi:
 * Aplikasi CRUD Mahasiswa dengan fitur:
 * - Simpan, Update, Hapus, Cari, Refresh
 * - Cetak Laporan JasperReport (PDF)
 * - Desain user-friendly
 */
public class MainFrame extends JFrame {

    // Deklarasi komponen utama GUI
    private JTextField txtNim, txtNama, txtEmail, txtSearch;
    private JDateChooser dateChooser;
    private JComboBox<String> cbJurusan;
    private JRadioButton rbLaki, rbPerempuan, rbOther;
    private JTable table;
    private JButton btnSave, btnUpdate, btnDelete, btnRefresh, btnReport, btnExit, btnSearch;
    private StudentDAO studentDAO;
    private DefaultTableModel tableModel;


    // Konstruktor (membangun tampilan utama)
    public MainFrame() {
        setTitle("Student Management System - Diky Firmansyah");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        studentDAO = new StudentDAO(); // Inisialisasi koneksi DAO

        // Tema warna
        Color primary = new Color(62, 99, 142);       // biru gelap
        Color accent = new Color(52, 152, 219);      // biru muda untuk tombol
        Color background = new Color(245, 249, 255); // latar belakang

        // font
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));

        // HEADER: Judul + Logo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primary);
        headerPanel.setBorder(new EmptyBorder(8, 20, 8, 20));

        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon("src/studentapp/reports/logoCRUD.png");
        Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaled));

        JLabel titleLabel = new JLabel("STUDENT MANAGEMENT SYSTEM HOGWARTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // PANEL UTAMA (form input + tabel + tombol)
        JPanel container = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Latar belakang gradasi lembut
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(240, 245, 255),
                        0, getHeight(), new Color(220, 230, 250)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        container.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Tambahkan panel ke layout utama
        container.add(initTopPanel(background), BorderLayout.NORTH);
        container.add(initTablePanel(background), BorderLayout.CENTER);
        container.add(initBottomPanel(accent), BorderLayout.SOUTH);

        add(container, BorderLayout.CENTER);
    }

    // FORM INPUT (Panel Atas)
    private JPanel initTopPanel(Color bg) {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(bg);
        topPanel.setBorder(BorderFactory.createTitledBorder("Input Data Mahasiswa"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Label dan Field
        JLabel lblNim = new JLabel("NIM:");
        JLabel lblNama = new JLabel("Nama:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblTanggal = new JLabel("Tanggal Lahir:");
        JLabel lblJurusan = new JLabel("Jurusan:");
        JLabel lblGender = new JLabel("Gender:");

        txtNim = new JTextField(10);
        txtNama = new JTextField(15);
        txtEmail = new JTextField(15);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        cbJurusan = new JComboBox<>(new String[]{
                "Informatika", "Sistem Informasi", "Teknik Komputer", "Lainnya"
        });

        // Radio Button Gender
        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");
        rbOther = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rbLaki);
        genderGroup.add(rbPerempuan);
        genderGroup.add(rbOther);

        // Panel Gender
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        genderPanel.setBackground(bg);
        genderPanel.add(rbLaki);
        genderPanel.add(rbPerempuan);
        genderPanel.add(rbOther);

        // Tombol Search
        txtSearch = new JTextField(12);
        btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchStudentByNim());

        // Penempatan komponen dalam grid
        c.gridx = 0; c.gridy = 0; topPanel.add(lblNim, c);
        c.gridx = 1; topPanel.add(txtNim, c);
        c.gridx = 2; topPanel.add(lblEmail, c);
        c.gridx = 3; topPanel.add(txtEmail, c);
        c.gridx = 4; topPanel.add(lblJurusan, c);
        c.gridx = 5; topPanel.add(cbJurusan, c);

        c.gridx = 0; c.gridy = 1; topPanel.add(lblNama, c);
        c.gridx = 1; topPanel.add(txtNama, c);
        c.gridx = 2; topPanel.add(lblTanggal, c);
        c.gridx = 3; topPanel.add(dateChooser, c);
        c.gridx = 4; topPanel.add(lblGender, c);
        c.gridx = 5; topPanel.add(genderPanel, c);

        // Bar pencarian
        c.gridx = 4; c.gridy = 2; topPanel.add(new JLabel("Cari NIM:"), c);
        c.gridx = 5; topPanel.add(txtSearch, c);
        c.gridx = 6; topPanel.add(btnSearch, c);

        return topPanel;
    }

    // TABEL DATA MAHASISWA (Panel Tengah)
    private JScrollPane initTablePanel(Color bg) {
        String[] columns = {"NIM", "Nama", "Email", "Tanggal Lahir", "Jurusan", "Gender"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setRowHeight(25);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(200, 220, 255));

        // Klik baris → isi form
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtNim.setText(tableModel.getValueAt(row, 0).toString());
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    cbJurusan.setSelectedItem(tableModel.getValueAt(row, 4).toString());

                    String gender = tableModel.getValueAt(row, 5).toString();
                    rbLaki.setSelected(gender.equalsIgnoreCase("Laki-laki"));
                    rbPerempuan.setSelected(gender.equalsIgnoreCase("Perempuan"));
                    rbOther.setSelected(gender.equalsIgnoreCase("Other"));
                }
            }
        });

        loadDataToTable(); // Tampilkan data dari DB
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Daftar Mahasiswa"));
        return scroll;
    }

    // TOMBOL CRUD + REPORT (Panel Bawah)
    private JPanel initBottomPanel(Color accent) {
        JPanel panel = new JPanel(new BorderLayout());

        // Kiri: Exit, Refresh, Report
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setBackground(Color.WHITE);
        btnExit = new JButton("Keluar");
        btnRefresh = new JButton("Refresh");
        btnReport = new JButton("Cetak Laporan");
        btnReport.addActionListener(e -> generateReport());
        left.add(btnExit);
        left.add(btnRefresh);
        left.add(btnReport);

        // Kanan: Save, Update, Delete
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        right.setBackground(Color.WHITE);
        btnSave = new JButton("Simpan");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        right.add(btnSave);
        right.add(btnUpdate);
        right.add(btnDelete);

        // Warna tombol
        JButton[] buttons = {btnSave, btnUpdate, btnDelete, btnRefresh, btnReport, btnExit};
        for (JButton b : buttons) {
            b.setBackground(accent);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // Action Listener Setiap Tombol

        // SIMPAN
        btnSave.addActionListener(e -> {
            try {
                saveStudent();
                clearForm();
                loadDataToTable();
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // UPDATE
        btnUpdate.addActionListener(e -> {
            try {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Apakah Anda yakin ingin memperbarui data mahasiswa ini?",
                        "Konfirmasi Update",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    updateStudent();
                    clearForm();
                    loadDataToTable();
                    JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
                }

            } catch (Exception ex) {
                showError(ex);
            }
        });

        // 🗑HAPUS
        btnDelete.addActionListener(e -> {
            try {
                deleteStudent();
                clearForm();
                loadDataToTable();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // REFRESH
        btnRefresh.addActionListener(e -> {
            clearForm();
            loadDataToTable();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
        });

        // KELUAR
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin keluar dari aplikasi?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Terima kasih telah menggunakan aplikasi!");
                System.exit(0);
            }
        });

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    // FUNGSI BANTUAN (Utility)
    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }

    private void loadDataToTable() {
        try {
            tableModel.setRowCount(0);
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                tableModel.addRow(new Object[]{
                        s.getStudentId(), s.getName(), s.getEmail(),
                        s.getBirthDate(), s.getMajor(), s.getGender()
                });
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    // CRUD OPERASI
    private void saveStudent() throws Exception {
        if (txtNim.getText().isEmpty() || txtNama.getText().isEmpty() || txtEmail.getText().isEmpty())
            throw new Exception("Semua field wajib diisi!");

        java.util.Date d = dateChooser.getDate();
        if (d == null) throw new Exception("Tanggal lahir wajib diisi!");

        String gender = rbLaki.isSelected() ? "Laki-laki"
                : rbPerempuan.isSelected() ? "Perempuan" : "Other";

        studentDAO.addStudent(new Student(
                Integer.parseInt(txtNim.getText()),
                txtNama.getText(),
                txtEmail.getText(),
                new java.sql.Date(d.getTime()),
                cbJurusan.getSelectedItem().toString(),
                gender
        ));
    }

    private void updateStudent() throws Exception {
        if (txtNim.getText().isEmpty()) throw new Exception("Pilih data yang ingin diupdate!");
        java.util.Date d = dateChooser.getDate();
        if (d == null) throw new Exception("Tanggal lahir wajib diisi!");

        studentDAO.updateStudent(new Student(
                Integer.parseInt(txtNim.getText()),
                txtNama.getText(),
                txtEmail.getText(),
                new java.sql.Date(d.getTime()),
                cbJurusan.getSelectedItem().toString(),
                rbLaki.isSelected() ? "Laki-laki" : rbPerempuan.isSelected() ? "Perempuan" : "Other"
        ));
    }

    private void deleteStudent() throws Exception {
        String nim = txtNim.getText().trim();
        if (nim.isEmpty()) throw new Exception("Masukkan NIM yang ingin dihapus!");

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data NIM " + nim + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION)
            studentDAO.deleteStudent(Integer.parseInt(nim));
    }


    // CLEAR FORM & SEARCH
    private void clearForm() {
        txtNim.setText("");
        txtNama.setText("");
        txtEmail.setText("");
        cbJurusan.setSelectedIndex(0);
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        rbOther.setSelected(false);
        txtSearch.setText("");
        dateChooser.setDate(null);
    }

    private void searchStudentByNim() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan NIM untuk mencari!");
            return;
        }
        try {
            List<Student> students = studentDAO.searchStudentByNim(keyword);
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{
                        s.getStudentId(), s.getName(), s.getEmail(),
                        s.getBirthDate(), s.getMajor(), s.getGender()
                });
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    // CETAK LAPORAN JASPERREPORT (PDF)
    private void generateReport() {
        try {
            String from = JOptionPane.showInputDialog(this, "Masukkan NIM Awal:");
            String to = JOptionPane.showInputDialog(this, "Masukkan NIM Akhir:");
            if (from == null || to == null) return;

            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("FROM_NIM", Integer.parseInt(from));
            params.put("TO_NIM", Integer.parseInt(to));

            String path = "src/studentapp/reports/student_report.jasper";
            net.sf.jasperreports.engine.JasperPrint jp =
                    net.sf.jasperreports.engine.JasperFillManager.fillReport(
                            path, params, DatabaseConnector.getConnection());

            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jp, "student_report.pdf");
            JOptionPane.showMessageDialog(this, "Laporan berhasil dibuat!");
        } catch (Exception e) {
            showError(e);
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
