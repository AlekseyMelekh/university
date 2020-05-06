package window;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import observer.Publisher;
import subscribers.LabelSubscriber;
import subscribers.ListSubscriber;

public class Controller {

    @FXML
    private Label label;

    @FXML
    private ListView listView;

    private Publisher publisher;

    @FXML
    public void initialize() {
        publisher = new Publisher();
        ObservableList listModel = FXCollections.observableArrayList();
        publisher.subscribe(new LabelSubscriber(label));
        publisher.subscribe(new ListSubscriber(listModel));
        listView.setItems(listModel);
    }

    @FXML
    public void keyButtonPressed (KeyEvent e) {
        publisher.notifySubs(e.getCode().getName());
    }

}
