import javafx.scene.control.ComboBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TypeHandler {
    private Circle circle;
    private TextField field;
    private ComboBox<String> comboBox;

    public void handle() {
        Pattern pattern = null;
        switch (comboBox.getValue()) {
            case "Целое число":
                pattern = Pattern.compile("[+-]?[0-9]+");
                break;
            case "Число с плавающей запятой":
                pattern = Pattern.compile("([-+]?([.][0-9]+|[.][0-9]+[e][-+]?[0-9]+)|([0-9]+[.][0-9]+|[0-9]+[.]?[0-9]*[e][-+]?[0-9]+))");
                break;
            case "Дата":
                pattern = Pattern.compile("(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|" +
                        "(0[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|" +
                        "(0[1-9]|1[0-9]|2[0-8])[/.](2|02))" +
                        "[/.][0-9]+");
                break;
            case "Время":
                pattern = Pattern.compile("(([01][0-9])|(2[0-3])):[0-5][0-9]");
                break;
            case "E-mail":
                pattern = Pattern.compile("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+");
                break;
            default:
                break;
        }
        if (pattern == null) {
            circle.setFill(Color.RED);
            return;
        }
        Matcher matcher = pattern.matcher(field.getText());
        if (matcher.matches()) {
            circle.setFill(Color.GREEN);
        } else {
            circle.setFill(Color.RED);
        }
    }

    public TypeHandler(Circle circle, TextField field, ComboBox<String> comboBox) {
        this.circle = circle;
        this.field = field;
        this.comboBox = comboBox;
    }
}