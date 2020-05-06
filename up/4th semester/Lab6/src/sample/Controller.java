package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView goal;

    @FXML
    private Label label;

    @FXML
    private Pane pane;

    @FXML
    private AnchorPane anchorPane;

    private Image image;
    private Image fullImage;
    private PixelReader pixelReader;
    private WritableImage cutImage;
    private Pair<Integer, Integer> first;
    private Pair<Integer, Integer> second;
    private ImageView firstImage;
    private ImageView secondImage;

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public static ImageView copyImage(Image image) {
        if (image == null) {
            return null;
        }
        int height=(int)image.getHeight();
        int width=(int)image.getWidth();
        PixelReader pixelReader=image.getPixelReader();
        WritableImage writableImage = new WritableImage(width,height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }

        ImageView imageView = new ImageView(writableImage);
        return imageView;
    }

    private int direction = -1;
    private TextField textField;

    private void repaint() {
        pane.getChildren().clear();
        Random rand = new Random();
        for (int i = 0; i < 100; ++i) {
            Label l = new Label(textField.getText());
            l.setFont(Font.font("Cambria", 64));
            l.setTextFill(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            l.relocate(100 + direction * 0.1 * i, 100 + 0.1 * i);
            pane.getChildren().add(l);
        }
    }

    public void initialize() throws FileNotFoundException {

        textField = new TextField();
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.textProperty().addListener((a, b, c) -> {
            repaint();
        });

        Button button = new Button("change");
        button.setOnMouseClicked((e) -> {
            direction *= -1;
            repaint();
        });

        anchorPane.getChildren().add(textField);
        anchorPane.getChildren().add(button);

        fullImage = new Image(new FileInputStream("monogatari.jpg"), (int)gridPane.getPrefWidth(), (int)gridPane.getPrefHeight(), false, false);
        pixelReader = fullImage.getPixelReader();

        List<Pair<WritableImage, Integer>> list = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                cutImage = new WritableImage(pixelReader, (int)(gridPane.getPrefWidth() / 4 * i), (int)(gridPane.getPrefHeight() / 4 * j),
                                                                (int)(gridPane.getPrefWidth() / 4), (int)(gridPane.getPrefHeight() / 4));
                list.add(new Pair<>(cutImage, i * 4 + j));
            }
        }
        Collections.shuffle(list);

        first = new Pair<>(-1, -1);
        second = new Pair<>(-1, -1);
        firstImage = new ImageView();
        secondImage = new ImageView();

        Integer[][] order = new Integer[4][4];

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                ImageView imageView = new ImageView(list.get(i * 4 + j).getKey());
                order[i][j] = list.get(i * 4 + j).getValue();
                imageView.setOnMouseClicked((e) -> {
                    first = new Pair<>(second.getKey(), second.getValue());
                    second = new Pair<>(GridPane.getColumnIndex((Node)e.getSource()), GridPane.getRowIndex((Node)e.getSource()));
                    firstImage = copyImage(secondImage.getImage());
                    secondImage = copyImage(((ImageView)e.getSource()).getImage());
                    if (first.getKey() != -1 && second.getValue() != -1) {
                        Integer firstOrder = order[first.getKey()][first.getValue()];
                        Integer secondOrder = order[second.getKey()][second.getValue()];
                        order[first.getKey()][first.getValue()] = secondOrder;
                        order[second.getKey()][second.getValue()] = firstOrder;
                        ImageView copyFirstImage = copyImage(firstImage.getImage());
                        ImageView copySecondImage = copyImage(secondImage.getImage());
                        firstImage = copyImage(copySecondImage.getImage());
                        secondImage = copyImage(copyFirstImage.getImage());
                        ImageView f = (ImageView)getNodeFromGridPane(gridPane, first.getKey(), first.getValue());
                        ImageView s = (ImageView)getNodeFromGridPane(gridPane, second.getKey(), second.getValue());
                        f.setImage(firstImage.getImage());
                        s.setImage(secondImage.getImage());
                        first = new Pair<>(-1, -1);
                        second = new Pair<>(-1, -1);
                        boolean ok = true;
                        for (int i1 = 0; i1 < 4; ++i1) {
                            for (int j1 = 0; j1 < 4; ++j1) {
                                if (order[i1][j1] != i1 * 4 + j1) {
                                    ok = false;
                                }
                            }
                        }
                        if (ok) {
                            label.setText("Правильно");
                        } else {
                            label.setText("Неправильно");
                        }
                    }
                });
                gridPane.add(imageView, i, j);
            }
        }

        image = new Image(new FileInputStream("monogatari.jpg"), (int)goal.getFitWidth(), (int)goal.getFitHeight(), false, false);
        goal.setImage(image);

    }

}
