package meleh.devices;

import java.util.Objects;

public class Fridge extends Device {

    private int volume;

    public Fridge () {
        super();
        volume = 0;
    }

    public Fridge (String manufacturer, int powerUsage, boolean turnedOn, int volume) {
        super(manufacturer, powerUsage, turnedOn);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "meleh.devices.Fridge{" + super.toString() +
                "volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Fridge fridge = (Fridge) o;
        return volume == fridge.volume;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), volume);
    }

}
