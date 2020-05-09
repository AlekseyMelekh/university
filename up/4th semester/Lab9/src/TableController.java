import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableController {

    private final Pattern DATE = Pattern.compile("(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+");

    public TableController() {
    }

    public GregorianCalendar getGregorianCalendar (String data) {
        String year, month, day;
        StringTokenizer stringTokenizer = new StringTokenizer(data, ".");
        day = stringTokenizer.nextToken();
        month = stringTokenizer.nextToken();
        year = stringTokenizer.nextToken();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
        return gregorianCalendar;
    }

    public GregorianCalendar addToGregorianCalendar (GregorianCalendar gregorianCalendar, String add) {
        if (add.length() != 0) {
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(add));
        }
        return gregorianCalendar;
    }

    public Pair<String, String> parseDate (String formula) {
        StringBuilder date = new StringBuilder("");
        StringBuilder add = new StringBuilder("");
        boolean flag = false;
        if (formula.length() != 0 && formula.charAt(0) == '=') {
            formula = formula.substring(1);
        }
        for (int ind = 0; ind < formula.length(); ++ind) {
            char symbol = formula.charAt(ind);
            if (symbol == '+' || symbol == '-') {
                flag = true;
            }
            if (flag) {
                add.append(symbol);
            } else {
                date.append(symbol);
            }
        }
        return new Pair<>(date.toString(), add.toString());
    }

    public Pair<Pair<Integer, Integer>, String> parseField (String formula) {
        StringBuilder field = new StringBuilder("");
        StringBuilder add = new StringBuilder("");
        boolean flag = false;
        if (formula.length() != 0 && formula.charAt(0) == '=') {
            formula = formula.substring(1);
        }
        for (int ind = 0; ind < formula.length(); ++ind) {
            char symbol = formula.charAt(ind);
            if (symbol == '+' || symbol == '-') {
                flag = true;
            }
            if (flag) {
                add.append(symbol);
            } else {
                field.append(symbol);
            }
        }
        int x = (field.charAt(0) - 'A') + 1;
        int y = field.charAt(1) - '0';
        return new Pair<>(new Pair<>(x, y), add.toString());
    }

    public Pair<ArrayList<Pair<Pair<Integer, Integer>, String>>, GregorianCalendar> parseMinMax (String formula) {
        String func = formula.substring(1, 4);
        String tmp = formula.substring(5, formula.length() - 1);
        StringTokenizer stringTokenizer = new StringTokenizer(tmp, ",");

        ArrayList<Pair<Pair<Integer, Integer>, String>> edges = new ArrayList<>();
        GregorianCalendar date = null;

        while (stringTokenizer.hasMoreTokens()) {
            String cur = stringTokenizer.nextToken();
            Matcher dateMatcher = DATE.matcher(cur);
            if (dateMatcher.matches()) {
                GregorianCalendar gregorianCalendar = getGregorianCalendar(cur);
                if (date == null) {
                    date = gregorianCalendar;
                } else {
                    if (func.equals("MIN")) {
                        if (date.compareTo(gregorianCalendar) > 0) {
                            date = gregorianCalendar;
                        }
                    } else {
                        if (date.compareTo(gregorianCalendar) < 0) {
                            date = gregorianCalendar;
                        }
                    }
                }
            } else {
                Pair<Pair<Integer, Integer>, String> result = parseField(cur);
                edges.add(new Pair<>(result.getKey(), result.getValue()));
            }
        }
        return new Pair<>(edges, date);
    }
}
