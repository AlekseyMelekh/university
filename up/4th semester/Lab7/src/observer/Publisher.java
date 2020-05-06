package observer;

import java.util.ArrayList;

public class Publisher {
    public Publisher() {
        subscribers = new ArrayList<>();
    }

    public void subscribe(Subscriber s) {
        subscribers.add(s);
    }

    public void unsubscribe(Subscriber s) {
        subscribers.remove(s);
    }

    public void notifySubs(String data) {
        for (Subscriber s : subscribers) {
            s.update(data);
        }
    }

    private ArrayList<Subscriber> subscribers;
}