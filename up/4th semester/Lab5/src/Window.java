import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Window extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private ComboBox<String> comboBox;
    private ObservableList<String> list;
    private TextField textField;
    private Circle checkCircle;
    private BorderPane root1;
    private FlowPane root2;
    private FlowPane flowPane;
    private TypeHandler typeHandler;
    private TabPane tabPane;
    private Tab tab1;
    private Tab tab2;
    private TextArea textArea;
    private ObservableList<String> model;
    private ListView<String> listOfData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Lab5");

        comboBox = new ComboBox<>();
        list = FXCollections.observableArrayList("Целое число",
                                                                            "Число с плавающей запятой",
                                                                            "Дата",
                                                                            "Время",
                                                                            "E-mail");
        comboBox.setItems(list);
        textField = new TextField("Введите текст");

        checkCircle = new Circle();
        checkCircle.setRadius(50);
        checkCircle.setFill(Color.RED);

        root1 = new BorderPane();
        flowPane = new FlowPane();
        flowPane.setHgap(45);
        flowPane.getChildren().add(comboBox);
        flowPane.getChildren().add(textField);
        root1.setTop(flowPane);
        root1.setCenter(checkCircle);

        typeHandler = new TypeHandler(checkCircle, textField, comboBox);
        comboBox.setValue("Целое число");
        comboBox.valueProperty().addListener(e -> {
            typeHandler.handle();
        });
        textField.textProperty().addListener(e -> {
            typeHandler.handle();
        });

        root2 = new FlowPane();
        textArea = new TextArea("Вводите текст");
        textArea.setPrefSize(200, 700);
        model = FXCollections.observableArrayList();
        listOfData = new ListView<>(model);
        listOfData.setPrefSize(190, 700);

        textArea.textProperty().addListener(e -> {
            model.clear();
            model.add("Список дат в тексте:");
            String test = textArea.getText();
            Pattern pattern = Pattern.compile("(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|" +
                    "(0[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|" +
                    "(0[1-9]|1[0-9]|2[0-8])[/.](2|02))" +
                    "[/.][0-9]+");

            Matcher matcher = pattern.matcher(test);
            while (matcher.find()) {
                model.add(matcher.start()+ "    "+matcher.group());
            }
        });

        root2.getChildren().add(textArea);
        root2.getChildren().add(listOfData);

        tabPane = new TabPane();
        tab1 = new Tab("Task 1");
        tab1.setContent(root1);
        tab2 = new Tab("Task 2");
        tab2.setContent(root2);
        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);

        Scene scene = new Scene(tabPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
