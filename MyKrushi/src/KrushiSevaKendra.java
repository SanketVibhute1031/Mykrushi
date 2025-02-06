import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KrushiSevaKendra {
    private JFrame frame;
    private JPanel panelLogin, panelEntry, panelManagement, panelSales;
    private JTextField txtName, txtQuantity, txtPrice, txtSaleQuantity, txtCustomerName, txtUseOfProduct, txtLoginId, txtLoginPass;
    private JComboBox<String> cmbType, cmbProduct, cmbUnit, cmbUnitForSale;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
    private List<SaleItem> saleItems = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                KrushiSevaKendra window = new KrushiSevaKendra();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Krushi", "root", "@sanket2006");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public KrushiSevaKendra() {
        initialize();
    }

    private void initialize() {
        try {
            // Apply Nimbus Look and Feel for modern UI
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Samruddhi Krushi Awjare,Peth-Vadgaon");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout());

        panelLogin = new JPanel();
        panelEntry = new JPanel();
        panelManagement = new JPanel();
        panelSales = new JPanel();

        frame.getContentPane().add(panelLogin, "Login");
        frame.getContentPane().add(panelEntry, "Product Entry");
        frame.getContentPane().add(panelManagement, "Product Management");
        frame.getContentPane().add(panelSales, "Product Sales");

        setupLoginPanel();
        setupProductEntryPanel();
        setupProductManagementPanel();
        setupProductSalesPanel();

        showPanel("Login");
    }

    private void setupLoginPanel() {
        panelLogin.setLayout(new GridLayout(3, 2, 10, 10));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblLoginId = new JLabel("Login ID:");
        lblLoginId.setFont(new Font("Arial", Font.BOLD, 14));
        panelLogin.add(lblLoginId);
        txtLoginId = new JTextField();
        txtLoginId.setFont(new Font("Arial", Font.PLAIN, 14));
        panelLogin.add(txtLoginId);

        JLabel lblLoginPass = new JLabel("Password:");
        lblLoginPass.setFont(new Font("Arial", Font.BOLD, 14));
        panelLogin.add(lblLoginPass);
        txtLoginPass = new JPasswordField();
        txtLoginPass.setFont(new Font("Arial", Font.PLAIN, 14));
        panelLogin.add(txtLoginPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.addActionListener(new LoginButtonListener());
        panelLogin.add(btnLogin);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.addActionListener(e -> System.exit(0));
        panelLogin.add(btnLogout);
    }

    private void setupProductEntryPanel() {
        panelEntry.setLayout(new GridLayout(9, 2, 10, 10));
        panelEntry.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelEntry.setBackground(Color.LIGHT_GRAY);

        JLabel lblName = new JLabel("Product Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        panelEntry.add(lblName);
        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEntry.add(txtName);

        JLabel lblType = new JLabel("Product Type:");
        lblType.setFont(new Font("Arial", Font.BOLD, 14));
        panelEntry.add(lblType);
        String[] productTypes = {"Seeds", "Fertilizers", "Pesticides", "Equipment"};
        cmbType = new JComboBox<>(productTypes);
        cmbType.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEntry.add(cmbType);

        JLabel lblUnit = new JLabel("Unit:");
        lblUnit.setFont(new Font("Arial", Font.BOLD, 14));
        panelEntry.add(lblUnit);
        String[] units = {"Kg", "Liter", "Millimeter", "No Unit"};
        cmbUnit = new JComboBox<>(units);
        cmbUnit.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEntry.add(cmbUnit);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 14));
        panelEntry.add(lblQuantity);
        txtQuantity = new JTextField();
        txtQuantity.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEntry.add(txtQuantity);

        JLabel lblPrice = new JLabel("Price (IN 1):");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
        panelEntry.add(lblPrice);
        txtPrice = new JTextField();
        txtPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEntry.add(txtPrice);

        JLabel lblUseOfProduct = new JLabel("Use of this Product:");
        lblUseOfProduct.setFont(new Font("Arial", Font.BOLD, 14));
        panelEntry.add(lblUseOfProduct);
        txtUseOfProduct = new JTextField();
        txtUseOfProduct.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEntry.add(txtUseOfProduct);

        JButton btnSave = new JButton("Save");
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.addActionListener(new SaveButtonListener());
        panelEntry.add(btnSave);

        JButton btnViewProducts = new JButton("View Products");
        btnViewProducts.setFont(new Font("Arial", Font.BOLD, 14));
        btnViewProducts.addActionListener(e -> showPanel("Product Management"));
        panelEntry.add(btnViewProducts);

        JButton btnSales = new JButton("Product Sales");
        btnSales.setFont(new Font("Arial", Font.BOLD, 14));
        btnSales.addActionListener(e -> showPanel("Product Sales"));
        panelEntry.add(btnSales);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.addActionListener(e -> showPanel("Login"));
        panelEntry.add(btnBack);

        JLabel lblEmpty = new JLabel("");
        panelEntry.add(lblEmpty);
    }

    private void setupProductManagementPanel() {
        panelManagement.setLayout(new BorderLayout());
        panelManagement.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelManagement.setBackground(Color.LIGHT_GRAY);

        tableModel = new DefaultTableModel(new String[]{"ID", "Product Code", "Name", "Type", "Quantity", "Unit", "Price", "Use of Product"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        panelManagement.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));
        btnUpdate.setBackground(Color.CYAN);
        btnUpdate.setForeground(Color.BLACK);
        btnUpdate.addActionListener(new UpdateButtonListener());
        panelButtons.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(new DeleteButtonListener());
        panelButtons.add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> showPanel("Product Entry"));
        panelButtons.add(btnBack);

        panelManagement.add(panelButtons, BorderLayout.SOUTH);
    }

    private void setupProductSalesPanel() {
        panelSales.setLayout(new GridLayout(9, 2, 10, 10));
        panelSales.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelSales.setBackground(Color.LIGHT_GRAY);

        JLabel lblCustomerName = new JLabel("Customer Name:");
        lblCustomerName.setFont(new Font("Arial", Font.BOLD, 14));
        panelSales.add(lblCustomerName);
        txtCustomerName = new JTextField();
        txtCustomerName.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSales.add(txtCustomerName);

        JLabel lblSelectProduct = new JLabel("Select Product:");
        lblSelectProduct.setFont(new Font("Arial", Font.BOLD, 14));
        panelSales.add(lblSelectProduct);
        cmbProduct = new JComboBox<>();
        cmbProduct.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSales.add(cmbProduct);

        JLabel lblQuantityToSell = new JLabel("Quantity to Sell:");
        lblQuantityToSell.setFont(new Font("Arial", Font.BOLD, 14));
        panelSales.add(lblQuantityToSell);
        txtSaleQuantity = new JTextField();
        txtSaleQuantity.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSales.add(txtSaleQuantity);

        JLabel lblUnit = new JLabel("Unit:");
        lblUnit.setFont(new Font("Arial", Font.BOLD, 14));
        panelSales.add(lblUnit);
        cmbUnitForSale = new JComboBox<>(new String[]{"Kg", "Liter", "Millimeter", "No Unit"});
        cmbUnitForSale.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSales.add(cmbUnitForSale);

        JButton btnAddProduct = new JButton("Add Product to Sale");
        btnAddProduct.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddProduct.addActionListener(new AddProductButtonListener());
        panelSales.add(btnAddProduct);

        JButton btnGenerateBill = new JButton("Generate Bill");
        btnGenerateBill.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerateBill.addActionListener(new GenerateBillButtonListener());
        panelSales.add(btnGenerateBill);

        JButton btnShowBills = new JButton("Show Bills");
        btnShowBills.setFont(new Font("Arial", Font.BOLD, 14));
        btnShowBills.addActionListener(new ShowBillsButtonListener());
        panelSales.add(btnShowBills);

        JButton btnTotalSales = new JButton("Today's Total Sale");
        btnTotalSales.setFont(new Font("Arial", Font.BOLD, 14));
        btnTotalSales.addActionListener(new TotalSalesButtonListener());
        panelSales.add(btnTotalSales);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.addActionListener(e -> showPanel("Product Entry"));
        panelSales.add(btnBack);
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
        cl.show(frame.getContentPane(), panelName);
    }

    private String generateProductCode(String name, String type) {
        return name.substring(0, 3).toUpperCase() + type.substring(0, 3).toUpperCase() + (tableModel.getRowCount() + 1);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String loginId = txtLoginId.getText();
            String loginPass = txtLoginPass.getText();

            if (loginId.equals("Samruddhi123") && loginPass.equals("9699138886")) {
                initializeDatabaseConnection();
                loadProductsFromDatabase();
                loadProductsForSale();
                showPanel("Product Entry");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid login credentials.");
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = txtName.getText();
            String type = cmbType.getSelectedItem().toString();
            int quantity = Integer.parseInt(txtQuantity.getText());
            String unit = cmbUnit.getSelectedItem().toString();
            double price = Double.parseDouble(txtPrice.getText());
            String useOfProduct = txtUseOfProduct.getText();
            String productCode = generateProductCode(name, type);

            try {
                String sql = "SELECT * FROM products WHERE name = ? AND unit = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, unit);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Product already exists, update the quantity
                    int existingQuantity = rs.getInt("quantity");
                    int newQuantity = existingQuantity + quantity;

                    sql = "UPDATE products SET quantity = ? WHERE name = ? AND unit = ?";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setInt(1, newQuantity);
                    pstmt.setString(2, name);
                    pstmt.setString(3, unit);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(frame, "Product quantity updated successfully!");
                } else {
                    // Product does not exist, insert a new record
                    sql = "INSERT INTO products (product_code, name, type, quantity, unit, price, use_of_product) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, productCode);
                    pstmt.setString(2, name);
                    pstmt.setString(3, type);
                    pstmt.setInt(4, quantity);
                    pstmt.setString(5, unit);
                    pstmt.setDouble(6, price);
                    pstmt.setString(7, useOfProduct);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(frame, "Product saved successfully!");
                }
                pstmt.close();
                rs.close();

                tableModel.setRowCount(0); // Clear the table model
                loadProductsFromDatabase(); // Reload the products from the database

                txtName.setText("");
                txtQuantity.setText("");
                txtPrice.setText("");
                txtUseOfProduct.setText("");
                loadProductsForSale();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving product.");
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = (Integer) tableModel.getValueAt(selectedRow, 0);
                    String name = tableModel.getValueAt(selectedRow, 2).toString();
                    String type = tableModel.getValueAt(selectedRow, 3).toString();
                    int quantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
                    String unit = tableModel.getValueAt(selectedRow, 5).toString();
                    double price = Double.parseDouble(tableModel.getValueAt(selectedRow, 6).toString());
                    String useOfProduct = tableModel.getValueAt(selectedRow, 7).toString();

                    String sql = "UPDATE products SET name = ?, type = ?, quantity = ?, unit = ?, price = ?, use_of_product = ? WHERE id = ?";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, type);
                    pstmt.setInt(3, quantity);
                    pstmt.setString(4, unit);
                    pstmt.setDouble(5, price);
                    pstmt.setString(6, useOfProduct);
                    pstmt.setInt(7, id);
                    pstmt.executeUpdate();
                    pstmt.close();

                    JOptionPane.showMessageDialog(frame, "Product updated successfully!");
                    loadProductsForSale();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error updating product.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to update.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = (Integer) tableModel.getValueAt(selectedRow, 0);

                    String sql = "DELETE FROM products WHERE id = ?";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    pstmt.close();

                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Product deleted successfully!");
                    loadProductsForSale();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error deleting product.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to delete.");
            }
        }
    }

    private class AddProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedProduct = cmbProduct.getSelectedItem().toString();
            int quantityToSell = Integer.parseInt(txtSaleQuantity.getText());
            String unit = cmbUnitForSale.getSelectedItem().toString();

            try {
                String sql = "SELECT * FROM products WHERE name = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, selectedProduct.split(" \\(")[0]);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int availableQuantity = rs.getInt("quantity");
                    String availableUnit = rs.getString("unit");

                    if (unit.equals(availableUnit)) {
                        if (quantityToSell > availableQuantity) {
                            JOptionPane.showMessageDialog(frame, "Insufficient quantity available.");
                        } else {
                            SaleItem saleItem = new SaleItem(rs.getString("name"), rs.getString("type"), quantityToSell, rs.getDouble("price"), unit);
                            saleItems.add(saleItem);
                            JOptionPane.showMessageDialog(frame, "Product added to sale!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selected unit does not match the product unit.");
                    }
                }
                pstmt.close();
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error adding product to sale.");
            }
        }
    }

    private class GenerateBillButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String customerName = txtCustomerName.getText();
            double totalAmount = 0.0;
            StringBuilder bill = new StringBuilder();

            ImageIcon logo = new ImageIcon("samruddhi.png");
            JLabel lblLogo = new JLabel(logo);

            bill.append("                          Samruddhi Krushi Awjare,Peth-Vadgaon\n");
            bill.append("Date: ").append(new SimpleDateFormat("dd/MM/yyyy").format(new Date())).append("\n");
            bill.append("Customer Name: ").append(customerName).append("\n\n");
            bill.append("Product Name\tType\tQuantity\tUnit\tPrice\tTotal\n");

            for (SaleItem item : saleItems) {
                double itemTotal = item.getQuantity() * item.getPrice();
                totalAmount += itemTotal;
                bill.append(item.getName()).append("\t").append(item.getType()).append("\t")
                        .append(item.getQuantity()).append("\t").append(item.getUnit()).append("\t")
                        .append(item.getPrice()).append("\t").append(itemTotal).append("\n");
            }

            bill.append("\nTotal Amount: ").append(totalAmount).append("\n");
            bill.append("                                 Thank you for your purchase!");

            JTextArea textArea = new JTextArea(bill.toString());

            JPanel panelBill = new JPanel(new BorderLayout());
            panelBill.add(lblLogo, BorderLayout.NORTH);
            panelBill.add(new JScrollPane(textArea), BorderLayout.CENTER);

            JOptionPane.showMessageDialog(frame, panelBill, "Generated Bill", JOptionPane.INFORMATION_MESSAGE);

            storeBill(customerName, totalAmount);

            saleItems.clear();
            loadProductsForSale();
        }
    }

    private class ShowBillsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String sql = "SELECT * FROM bills";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("Bill ID");
                tableModel.addColumn("Customer Name");
                tableModel.addColumn("Total Amount");
                tableModel.addColumn("Bill Date");

                while (rs.next()) {
                    int billId = rs.getInt("id");
                    String customerName = rs.getString("customer_name");
                    double totalAmount = rs.getDouble("total_amount");
                    Date billDate = rs.getDate("bill_date");

                    tableModel.addRow(new Object[]{billId, customerName, totalAmount, billDate});
                }

                stmt.close();
                rs.close();

                JTable table = new JTable(tableModel);
                table.setFont(new Font("Arial", Font.PLAIN, 14));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new JScrollPane(table), BorderLayout.CENTER);

                JOptionPane.showMessageDialog(frame, panel, "Bills", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error showing bills.");
            }
        }
    }

    private class TotalSalesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String sql = "SELECT b.id, b.customer_name, bi.product_name, bi.quantity, bi.total " +
                    "FROM bills b " +
                    "JOIN bill_items bi ON b.id = bi.bill_id " +
                    "WHERE DATE(b.bill_date) = CURDATE()";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder salesDetails = new StringBuilder();
                double totalSales = 0.0;

                salesDetails.append("Today's Sales Details:\n\n");
                salesDetails.append(String.format("%-10s %-20s %-20s %-10s %-10s%n", "Bill ID", "Customer Name", "Product Name", "Quantity", "Total"));

                while (rs.next()) {
                    int billId = rs.getInt("id");
                    String customerName = rs.getString("customer_name");
                    String productName = rs.getString("product_name");
                    int quantity = rs.getInt("quantity");
                    double total = rs.getDouble("total");

                    totalSales += total;

                    salesDetails.append(String.format("%-10d %-20s %-20s %-10d %-10.2f%n",
                            billId, customerName, productName, quantity, total));
                }

                salesDetails.append("\nTotal Sales Amount: ").append(String.format("%.2f", totalSales));

                JOptionPane.showMessageDialog(frame, salesDetails.toString(), "Today's Total Sales", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error retrieving today's total sales.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void storeBill(String customerName, double totalAmount) {
        try {
            String sql = "INSERT INTO bills (customer_name, total_amount, bill_date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, customerName);
            pstmt.setDouble(2, totalAmount);
            pstmt.setDate(3, new java.sql.Date(new Date().getTime()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int billId = 0;
            if (rs.next()) {
                billId = rs.getInt(1);
            }

            sql = "INSERT INTO bill_items (bill_id, product_name, product_type, quantity, unit, price, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);
            for (SaleItem item : saleItems) {
                double total = item.getQuantity() * item.getPrice();
                pstmt.setInt(1, billId);
                pstmt.setString(2, item.getName());
                pstmt.setString(3, item.getType());
                pstmt.setInt(4, item.getQuantity());
                pstmt.setString(5, item.getUnit());
                pstmt.setDouble(6, item.getPrice());
                pstmt.setDouble(7, total);
                pstmt.addBatch();
            }
            pstmt.executeBatch();

            sql = "UPDATE products SET quantity = quantity - ? WHERE name = ? AND unit = ?";
            pstmt = connection.prepareStatement(sql);
            for (SaleItem item : saleItems) {
                pstmt.setInt(1, item.getQuantity());
                pstmt.setString(2, item.getName());
                pstmt.setString(3, item.getUnit());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

            pstmt.close();
            JOptionPane.showMessageDialog(frame, "Bill stored successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error storing bill.");
        }
    }

    private void loadProductsFromDatabase() {
        try {
            String sql = "SELECT * FROM products";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("product_code"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("quantity"),
                        rs.getString("unit"),
                        rs.getDouble("price"),
                        rs.getString("use_of_product")
                });
            }

            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProductsForSale() {
        cmbProduct.removeAllItems();
        try {
            String sql = "SELECT name, quantity, type, unit FROM products";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String productName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                String type = rs.getString("type");
                String unit = rs.getString("unit");

                if (type.equals("Seeds")) {
                    cmbProduct.addItem(productName + " (" + quantity + " Bags" + " " + "(" + unit + ")" + " in available)");
                } else if (type.equals("Fertilizers")) {
                    cmbProduct.addItem(productName + " (" + quantity + " Bags" + " " + "(" + unit + ")" + " in available)");
                } else if (type.equals("Pesticides")) {
                    cmbProduct.addItem(productName + " (" + quantity + " Bottle" + " " + "(" + unit + ")" + " in available)");
                } else if (type.equals("Equipment")) {
                    cmbProduct.addItem(productName + " (" + quantity + " Equipment" + " " + "(" + unit + ")" + " in available)");
                }
            }

            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class SaleItem {
        private String name;
        private String type;
        private int quantity;
        private double price;
        private String unit;

        public SaleItem(String name, String type, int quantity, double price, String unit) {
            this.name = name;
            this.type = type;
            this.quantity = quantity;
            this.price = price;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public String getUnit() {
            return unit;
        }
    }
}