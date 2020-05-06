package meleh.devices;

import java.util.Objects;

public abstract class Device {

    protected String manufacturer;
    protected int powerUsage;
    protected boolean turnedOn;

    public Device () {
        this.manufacturer = "No name";
        this.powerUsage = 0;
        this.turnedOn = false;
    }

    public Device (String manufacturer, int powerUsage, boolean turnedOn) {
        this.manufacturer = manufacturer;
        this.powerUsage = powerUsage;
        this.turnedOn = turnedOn;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getPowerUsage () {
        return powerUsage;
    }

    public boolean isTurnedOn () {
        return turnedOn;
    }

    public void switchDevice () {
        turnedOn ^= true;
    }

    @Override
    public String toString() {
        return "meleh.devices.Device{" +
                "manufacturer='" + manufacturer + '\'' +
                ", powerUsage=" + powerUsage +
                ", turnedOn=" + turnedOn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return powerUsage == device.powerUsage &&
                turnedOn == device.turnedOn &&
                Objects.equals(manufacturer, device.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturer, powerUsage, turnedOn);
    }

}
