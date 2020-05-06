package subscribers;

import javafx.collections.ObservableList;
import observer.Subscriber;

public class ListSubscriber implements Subscriber {
    public ListSubscriber(ObservableList<String> model) {
        this.model = model;
    }

    @Override
    public void update(String data) {
        this.model.add(data);
    }

    private ObservableList<String> model;
}