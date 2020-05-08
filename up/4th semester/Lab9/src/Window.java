import javafx.util.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Window extends JFrame {
    private JPanel panel;
    private JTable table;
    private DefaultTableModel model;
    private final int ROWS = 10;
    private final int COLUMNS = 8;
    private String folmula[][];
    private Object data[][];
    private GregorianCalendar foo[][];
    private int x = -1, y = -1;
    private int cycle_x, cycle_y;
    private ArrayList<Pair<Integer, Integer>> graph[][];
    private boolean used[][];
    private int addToCell[][];

    public Window() {
        setTitle("Lab9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 1000, 600);

        addToCell = new int[ROWS][COLUMNS];
        used = new boolean[ROWS][COLUMNS];
        foo = new GregorianCalendar[ROWS][COLUMNS];
        folmula = new String[ROWS][COLUMNS];
        data = new Object[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                folmula[i][j] = "";
                data[i][j] = "";
            }
        }

        for (int i = 0; i < ROWS - 1; ++i) {
            data[i + 1][0] = String.valueOf(i + 1);
        }
        char cur = 'A';
        for (int i = 1; i < COLUMNS; ++i) {
            data[0][i] = String.valueOf(cur++);
        }
        String column[] = new String[COLUMNS];
        for (int i = 0; i < COLUMNS; ++i) {
            column[i] = "ID";
        }
        model = new DefaultTableModel(data, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (row != 0 && column != 0);
            }
        };

        table = new JTable();
        table.setModel(model);
        table.setCellSelectionEnabled(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (x != -1 && y != -1) {
                    folmula[x][y] = (String) model.getValueAt(x, y);
                }
                x = table.getSelectedRow();
                y = table.getSelectedColumn();
                if (table.getModel().isCellEditable(x, y)) {
                    model.setValueAt(folmula[x][y], x, y);
                }
                repaintTable();
            }
        });

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                repaintTable();
            }
        });

        panel = new JPanel(new BorderLayout());
        panel.add(table);

        Container container = this.getContentPane();
        container.add(panel);
    }

    private void repaintTable() {
        recalc();
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                int x = table.getSelectedRow(), y = table.getSelectedColumn();
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
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private void recalc() {
        graph = new ArrayList[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                graph[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                if (table.isCellEditable(i, j)) {
                    String cur = folmula[i][j];
                    Matcher dateMatcher = DATE.matcher(cur);
                    if (dateMatcher.matches()) {
                        parseDate(i, j);
                        System.out.println("date  " + cur);
                        continue;
                    }
                    Matcher sumFieldMatcher = SUM_FIELD.matcher(cur);
                    if (sumFieldMatcher.matches()) {
                        parseField(i, j);
                        System.out.println("sum field  " + cur);
                        continue;
                    }
                    Matcher sumConstMatcher = SUM_CONST.matcher(cur);
                    if (sumConstMatcher.matches()) {
                        parseDate(i, j);
                        System.out.println("sum const   " + cur);
                        continue;
                    }
                    Matcher minMaxMatcher = MIN_MAX.matcher(cur);
                    if (minMaxMatcher.matches()) {
                        parseMinMax(i, j);
                        System.out.println("min max   " + cur);
                        continue;
                    }
                    if (folmula[i][j].length() != 0) {
                        data[i][j] = "Wrong folmula";
                    }
                }
            }
        }
        cycle = false;
        ArrayList<Pair<Integer, Integer>> order = topological_sort();
        if (cycle) {
            data[cycle_x][cycle_y] = "cycle here";
        } else {
            for (int i = 0; i < order.size(); ++i) {
                int x = order.get(i).getKey();
                int y = order.get(i).getValue();
                if (graph[x][y].size() == 1) {
                    int xx = graph[x][y].get(0).getKey();
                    int yy = graph[x][y].get(0).getValue();
                    data[x][y] = data[xx][yy];
                }
            }
        }
    }

    private boolean cycle;

    private void dfs (int i, int j, ArrayList<Pair<Integer, Integer>> ans) {
        used[i][j] = true;
        for (int ind = 0; ind < graph[i][j].size(); ++ind) {
            int to_i = graph[i][j].get(ind).getKey();
            int to_j = graph[i][j].get(ind).getValue();
            if (!used[to_i][to_j]) {
                dfs(to_i, to_j, ans);
            } else {
                cycle = true;
                cycle_x = i;
                cycle_y = j;
            }
        }
        ans.add(new Pair<>(i, j));
    }

    private ArrayList<Pair<Integer, Integer>> topological_sort() {
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                used[i][j] = false;
            }
        }
        ArrayList<Pair<Integer, Integer>> ans = new ArrayList<>();
        for (int i = 1; i < ROWS; ++i) {
            for (int j = 1; j < COLUMNS; ++j) {
                if (!used[i][j]) {
                    dfs(i, j, ans);
                }
            }
        }
        return ans;
    }

    private GregorianCalendar getGregorianCalendar (StringBuilder data) {
        String year, month, day;
        StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), ".");
        day = stringTokenizer.nextToken();
        month = stringTokenizer.nextToken();
        year = stringTokenizer.nextToken();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
        return gregorianCalendar;
    }

    private void parseDate (int i, int j) {
        StringBuilder date = new StringBuilder("");
        StringBuilder add = new StringBuilder("");
        boolean flag = false;
        String buf = folmula[i][j];
        if (folmula[i][j].length() != 0 && folmula[i][j].charAt(0) == '=') {
            buf = folmula[i][j].substring(1);
        }
        for (int ind = 0; ind < buf.length(); ++ind) {
            char symbol = buf.charAt(ind);
            if (symbol == '+' || symbol == '-') {
                flag = true;
            }
            if (flag) {
                add.append(symbol);
            } else {
                date.append(symbol);
            }
        }
        GregorianCalendar gregorianCalendar = getGregorianCalendar(date);
        if (add.length() != 0) {
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(add.toString()));
        }
        data[i][j] = dateFormat.format(gregorianCalendar.getTime());
    }

    private void parseField (int i, int j) {
        StringBuilder field = new StringBuilder("");
        StringBuilder add = new StringBuilder("");
        boolean flag = false;
        for (int ind = 0; ind < folmula[i][j].length(); ++ind) {
            char symbol = folmula[i][j].charAt(ind);
            if (symbol == '+' || symbol == '-') {
                flag = true;
            }
            if (flag) {
                add.append(symbol);
            } else {
                field.append(symbol);
            }
        }
        int x = (field.charAt(1) - 'A') + 1;
        int y = field.charAt(2) - '0';
        graph[i][j].add(new Pair<>(x, y));
    }

    private void parseMinMax (int i, int j) {
        String func = folmula[i][j].substring(1, 4);
        String tmp = folmula[i][j].substring(5, folmula[i][j].length() - 1);
        StringTokenizer stringTokenizer = new StringTokenizer(tmp, ",");

        foo[i][j] = null;

        while (stringTokenizer.hasMoreTokens()) {
            String cur = stringTokenizer.nextToken();
            Matcher dateMatcher = DATE.matcher(cur);
            if (dateMatcher.matches()) {
                GregorianCalendar gregorianCalendar = getGregorianCalendar(new StringBuilder(cur));
                if (foo[i][j] == null) {
                    foo[i][j] = gregorianCalendar;
                } else {
                    if (func.equals("MIN")) {
                        if (foo[i][j].compareTo(gregorianCalendar) > 0) {
                            foo[i][j] = gregorianCalendar;
                        }
                    } else {
                        if (foo[i][j].compareTo(gregorianCalendar) < 0) {
                            foo[i][j] = gregorianCalendar;
                        }
                    }
                }
            } else {
                int x = (cur.charAt(0) - 'A') + 1;
                int y = cur.charAt(1) - '0';
                graph[i][j].add(new Pair<>(x, y));
            }
        }
    }

}
