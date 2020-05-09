import com.sun.org.apache.bcel.internal.generic.SWAP;
import javafx.util.Pair;
import jdk.internal.org.objectweb.asm.tree.InnerClassNode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTable extends JTable {

    private int rows;
    private int columns;
    private DefaultTableModel model;
    private String[][] formula;
    private Object[][] data;
    private TableController tableController;
    private int lastX;
    private int lastY;
    private int tmpX, tmpY;

    public MyTable(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        lastX = lastY = -1;

        initialization();

        setModel(model);
        setCellSelectionEnabled(true);
        setRowHeight(50);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for (int x = 0; x < this.columns; x++) {
            getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = getSelectedRow(), y = getSelectedColumn();
                if (getModel().isCellEditable(x, y)) {
                    model.setValueAt(formula[x][y], x, y);
                }
                tmpX = lastX;
                tmpY = lastY;
                if (lastX != -1 && lastY != -1) {
                    formula[lastX][lastY] = (String) model.getValueAt(lastX, lastY);
                }
                lastX = x;
                lastY = y;
                repaintTable();
            }
        });
    }

    private void initialization() {
        tableController = new TableController();

        formula = new String[this.rows][this.columns];
        data = new Object[this.rows][this.columns];
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                formula[i][j] = "";
                data[i][j] = "";
            }
        }
        for (int i = 0; i < this.rows - 1; ++i) {
            data[i + 1][0] = String.valueOf(i + 1);
        }
        char cur = 'A';
        for (int i = 1; i < this.columns; ++i) {
            data[0][i] = String.valueOf(cur++);
        }
        String column[] = new String[this.columns];
        for (int i = 0; i < this.columns; ++i) {
            column[i] = "ID";
        }

        model = new DefaultTableModel(data, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (row != 0 && column != 0);
            }
        };
    }

    private void repaintTable() {
        recalc();
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                int x = getSelectedRow(), y = getSelectedColumn();
                if (x != i || y != j) {
                    model.setValueAt(data[i][j], i, j);
                }
            }
        }
    }

    private final Pattern DATE = Pattern.compile("(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+");
    private final Pattern SUM_CONST = Pattern.compile("=(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+");
    private final Pattern SUM_FIELD = Pattern.compile("=[A-Z]+[0-9]+[+-]?[0-9]+");
    private final Pattern MIN_MAX = Pattern.compile("=(MIN|MAX)\\(((((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+|[A-Z]+[0-9]+)(,[ ]?((((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+|[A-Z]+[0-9]+))+\\)");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private void recalc() {
        ArrayList<Pair<Pair<Integer, Integer>, String>>[][] graph = new ArrayList[this.rows][this.columns];
        GregorianCalendar[][] tmp = new GregorianCalendar[this.rows][this.columns];
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                graph[i][j] = new ArrayList<>();
                tmp[i][j] = null;
            }
        }
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                if (isCellEditable(i, j)) {
                    String currentFormula = formula[i][j];

                    Matcher dateMatcher = DATE.matcher(currentFormula);
                    if (dateMatcher.matches()) {
                        Pair<String, String> result = tableController.parseDate(currentFormula);
                        GregorianCalendar gregorianCalendar = tableController.getGregorianCalendar(result.getKey());
                        gregorianCalendar = tableController.addToGregorianCalendar(gregorianCalendar, result.getValue());
                        data[i][j] = dateFormat.format(gregorianCalendar.getTime());
                        continue;
                    }

                    Matcher sumFieldMatcher = SUM_FIELD.matcher(currentFormula);
                    if (sumFieldMatcher.matches()) {
                        Pair<Pair<Integer, Integer>, String> result = tableController.parseField(currentFormula);
                        graph[i][j].add(new Pair<>(result.getKey(), result.getValue()));
                        continue;
                    }

                    Matcher sumConstMatcher = SUM_CONST.matcher(currentFormula);
                    if (sumConstMatcher.matches()) {
                        Pair<String, String> result = tableController.parseDate(currentFormula);
                        GregorianCalendar gregorianCalendar = tableController.getGregorianCalendar(result.getKey());
                        gregorianCalendar = tableController.addToGregorianCalendar(gregorianCalendar, result.getValue());
                        data[i][j] = dateFormat.format(gregorianCalendar.getTime());
                        continue;
                    }

                    Matcher minMaxMatcher = MIN_MAX.matcher(currentFormula);
                    if (minMaxMatcher.matches()) {
                        Pair<ArrayList<Pair<Pair<Integer, Integer>, String>>, GregorianCalendar> result
                                = tableController.parseMinMax(currentFormula);
                        graph[i][j] = result.getKey();
                        tmp[i][j] = result.getValue();
                        continue;
                    }

                    if (formula[i][j].length() != 0) {
                        data[i][j] = "Wrong formula";
                    }
                }
            }
        }
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                for (int k = 0; k < graph[i][j].size(); ++k) {
                    int first = graph[i][j].get(k).getKey().getKey();
                    int second = graph[i][j].get(k).getKey().getValue();
                    graph[i][j].set(k, new Pair<>(new Pair<>(second, first), graph[i][j].get(k).getValue()));
                }
            }
        }
        TopSort topsort = new TopSort(graph, this.rows, this.columns);
        ArrayList<Pair<Integer, Integer>> order = topsort.work();
        if (!topsort.isCycle()) {
            for (int i = 0; i < order.size(); ++i) {
                int x = order.get(i).getKey();
                int y = order.get(i).getValue();
                if (graph[x][y].isEmpty()) {
                    continue;
                }
                if (graph[x][y].size() == 1) {
                    int xx = graph[x][y].get(0).getKey().getKey();
                    int yy = graph[x][y].get(0).getKey().getValue();
                    if (data[xx][yy].equals("Wrong formula") || data[xx][yy].equals("Undefined") ||
                            ((String) data[xx][yy]).length() == 0) {
                        data[x][y] = "Undefined";
                    } else {
                        GregorianCalendar gregorianCalendar = tableController.getGregorianCalendar((String) data[xx][yy]);
                        gregorianCalendar = tableController.addToGregorianCalendar(gregorianCalendar, graph[x][y].get(0).getValue());
                        data[x][y] = dateFormat.format(gregorianCalendar.getTime());
                    }
                    continue;
                }
                for (int j = 0; j < graph[x][y].size(); ++j) {
                    int xx = graph[x][y].get(j).getKey().getKey();
                    int yy = graph[x][y].get(j).getKey().getValue();
                    if (data[xx][yy].equals("Wrong formula") || data[xx][yy].equals("Undefined") ||
                            ((String) data[xx][yy]).length() == 0) {
                        data[x][y] = "Undefined";
                        break;
                    } else {
                        GregorianCalendar gregorianCalendar = tableController.getGregorianCalendar((String) data[xx][yy]);
                        gregorianCalendar = tableController.addToGregorianCalendar(gregorianCalendar, graph[x][y].get(0).getValue().toString());
                        String func = formula[x][y].substring(1, 4);
                        if (tmp[x][y] == null) {
                            tmp[x][y] = gregorianCalendar;
                        } else {
                            if (func.equals("MIN")) {
                                if (tmp[x][y].compareTo(gregorianCalendar) > 0) {
                                    tmp[x][y] = gregorianCalendar;
                                }
                            } else {
                                if (tmp[x][y].compareTo(gregorianCalendar) < 0) {
                                    tmp[x][y] = gregorianCalendar;
                                }
                            }
                        }
                        data[x][y] = dateFormat.format(tmp[x][y].getTime());
                    }
                }
            }
        } else {
            formula[tmpX][tmpY] = "";
            JOptionPane.showMessageDialog(this, "Ваши формулы зациклились, последняя формула удалена",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

}
