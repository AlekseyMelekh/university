package meleh.devices;

import java.util.Objects;

public class ElectricStove extends Device {

    private int diameter;

    public ElectricStove () {
        super();
        diameter = 0;
    }

    public ElectricStove (String manufacturer, int powerUsage, boolean turnedOn, int diameter) {
        super(manufacturer, powerUsage, turnedOn);
        this.diameter = diameter;
    }

    public int getDiameter() {
        return diameter;
    }

    @Override
    public String toString() {
        return "meleh.devices.ElectricStove{" + super.toString() +
                "diameter=" + diameter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ElectricStove that = (ElectricStove) o;
        return diameter == that.diameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diameter);
    }

}
