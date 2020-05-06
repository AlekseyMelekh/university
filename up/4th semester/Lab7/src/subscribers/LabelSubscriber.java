package subscribers;

import javafx.scene.control.Label;
import observer.Subscriber;

public class LabelSubscriber implements Subscriber {
    public LabelSubscriber(Label label) {
        this.label = label;
    }

    @Override
    public void update(String data) {
        this.label.setText(data);
    }

    private Label label;
}