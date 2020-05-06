package meleh.devices;

import java.util.Objects;

public class Washer extends Device {

    private int spinSpeed;

    public Washer () {
        super();
        spinSpeed = 0;
    }

    public Washer (String manufacturer, int powerUsage, boolean turnedOn, int spinSpeed) {
        super(manufacturer, powerUsage, turnedOn);
        this.spinSpeed = spinSpeed;
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    @Override
    public String toString() {
        return "meleh.devices.Washer{" + super.toString() +
                "spinSpeed=" + spinSpeed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Washer washer = (Washer) o;
        return spinSpeed == washer.spinSpeed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), spinSpeed);
    }

}
