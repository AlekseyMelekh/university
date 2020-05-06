package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Window extends JFrame {

    private JTabbedPane tabbedPane;
    private JList<Country> listOfCountries;
    private DefaultListModel<Country> defaultListModel;
    private JPanel panelTask1;
    private JPanel panelTask2;
    private JLabel chosen;
    private HashMap<String, String> capitalOfCountry;
    private HashMap<String, ImageIcon> flagOfCountry;
    private DefaultTableModel defaultTableModel;
    private JTable table;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem add;
    private JMenuItem calc;
    private int ans = 0;
    private JLabel label;

    private class ListCountryRenderer extends JLabel implements ListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Country entry = (Country) value;
            setText(value.toString());
            setIcon(entry.getFlag());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    public Window () {

        this.setBounds(700, 200, 400, 600);
        this.setTitle("Lab3");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        listOfCountries = new JList<>();

        defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement(new Country("Cyprus", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_cyprus.png"))));
        defaultListModel.addElement(new Country("Egypt", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_egypt.png"))));
        defaultListModel.addElement(new Country("USA", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_usa.png"))));
        defaultListModel.addElement(new Country("Denmark", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_denmark.png"))));
        defaultListModel.addElement(new Country("Japan", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_japan.png"))));
        defaultListModel.addElement(new Country("Cuba", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_cuba.png"))));
        defaultListModel.addElement(new Country("Israel", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_israel.png"))));
        defaultListModel.addElement(new Country("Belarus", new ImageIcon(Toolkit.getDefaultToolkit().getImage("flags-all-world/shadow/flag_belarus.png"))));

        capitalOfCountry = new HashMap<>();
        capitalOfCountry.put("Cyprus", "Nicosia");
        capitalOfCountry.put("Egypt", "Cairo");
        capitalOfCountry.put("USA", "Washington, D.C.");
        capitalOfCountry.put("Denmark", "Copenhagen");
        capitalOfCountry.put("Japan", "Tokyo");
        capitalOfCountry.put("Cuba", "Havana");
        capitalOfCountry.put("Israel", "Jerusalem");
        capitalOfCountry.put("Belarus", "Minsk");

        listOfCountries.setModel(defaultListModel);
        listOfCountries.setCellRenderer(new ListCountryRenderer());

        chosen = new JLabel("Choose any country", SwingConstants.CENTER);
        listOfCountries.addListSelectionListener(e -> {
            chosen.setIcon(listOfCountries.getSelectedValue().getFlag());
            chosen.setText(listOfCountries.getSelectedValue().getName() + ",  " + capitalOfCountry.get(listOfCountries.getSelectedValue().getName()));
        });

        panelTask1 = new JPanel();
        panelTask1.setLayout(new BorderLayout());
        panelTask1.add(listOfCountries, BorderLayout.SOUTH);
        panelTask1.add(chosen, BorderLayout.CENTER);

        defaultTableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return ImageIcon.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    case 3:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (row == this.getRowCount() - 1) {
                    return false;
                }
                switch (column) {
                    case 2:
                        return true;
                    case 3:
                        return true;
                    default:
                        return false;
                }
            }
        };
        label = new JLabel("Total price = 0");
        defaultTableModel.setColumnIdentifiers(new Object[]{"Country", "Description", "Price", "Select", "Total price"});
        defaultTableModel.addRow(new Object[]{null, null, null, null, 0});
        table = new JTable(defaultTableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(100);
        defaultTableModel.addTableModelListener(e -> {
            if ((e.getColumn() == 3) || (e.getColumn() == 2)) {
                recalc();
                defaultTableModel.removeRow(defaultTableModel.getRowCount() - 1);
                defaultTableModel.addRow(new Object[]{null, null, null, null, ans});
            }
            label.setText("Total price = " + ans);
        });

        menuBar = new JMenuBar();
        menu = new JMenu("Edit");
        add = new JMenuItem("add new tour");
        calc = new JMenuItem("get price of tour");

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedComponent(panelTask2);

                InputPanel inputPanel = new InputPanel(Window.this);
                inputPanel.setBounds(700, 200, 400, 600);
                inputPanel.setVisible(true);

                recalc();
                defaultTableModel.removeRow(defaultTableModel.getRowCount() - 1);
                defaultTableModel.addRow(new Object[]{inputPanel.getCountyName(), inputPanel.getDescription(), inputPanel.getPrice(), false, null});
                defaultTableModel.addRow(new Object[]{null, null, null, null, ans});
            }
        });

        calc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedComponent(panelTask2);

                JOptionPane.showMessageDialog(Window.this, "Price of your tour = " + ans + "$", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        menu.add(add);
        menu.add(calc);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        panelTask2 = new JPanel(new BorderLayout());
        panelTask2.add(scrollPane, BorderLayout.CENTER);
        panelTask2.add(label, BorderLayout.SOUTH);

        Container container = this.getContentPane();
        tabbedPane.add(panelTask1, "Task 1");
        tabbedPane.add(panelTask2, "Task 2");
        container.add(tabbedPane);
    }

    public void recalc() {
        ans = 0;
        for (int i = 0; i < defaultTableModel.getRowCount() - 1; i++) {
            if ((boolean) defaultTableModel.getValueAt(i, 3)) {
                ans += (int) defaultTableModel.getValueAt(i, 2);
            }
        }
    }

    public class InputPanel extends JDialog {

        private ImageIcon flag;
        private JTextField description;
        private JTextField price;
        private JPanel mainPanel;
        private boolean pressedOK;

        public InputPanel(JFrame frame) {
            super(frame, "kek", true);
            pressedOK = false;
            mainPanel = new JPanel(new GridLayout(9, 0));
            add(mainPanel);
            description = new JTextField(5);
            price = new JTextField(5);

            mainPanel.add(new JLabel("Select country flag icon:"));
            JButton button = new JButton("select");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser(new File("D:\\Projects\\University\\UP_4sem\\Lab3\\flags-all-world\\shadow"));
                    if (fileChooser.showDialog(Window.this, "Load") == JFileChooser.APPROVE_OPTION) {
                        flag = new ImageIcon(Toolkit.getDefaultToolkit().getImage(fileChooser.getSelectedFile().getPath()));
                    }
                }
            });
            mainPanel.add(button);
            //mainPanel.add(Box.createHorizontalStrut(15));
            mainPanel.add(new JLabel("Description:"));
            mainPanel.add(description);
            //mainPanel.add(Box.createHorizontalStrut(15));
            mainPanel.add(new JLabel("Price:"));
            mainPanel.add(price);
            //mainPanel.add(Box.createHorizontalStrut(15));
            JButton ok = new JButton("OK");
            ok.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pressedOK = true;
                    dispose();
                }
            });
            mainPanel.add(ok);
        }

        public ImageIcon getCountyName() throws NumberFormatException {
            return flag;
        }

        public String getDescription() throws NumberFormatException {
            return description.getText();
        }

        public int getPrice() throws NumberFormatException {
            return Integer.parseInt(price.getText());
        }

        public boolean isPressedOK() {
            return pressedOK;
        }

    }

}
