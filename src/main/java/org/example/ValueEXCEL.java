package main.java.org.example;

public class ValueEXCEL {
    String sheet;
    String stationName;
    int platform;
    int line;

    String getStation(){
        return new String(stationName+" (Platz "+platform+")");
    }

    @Override
    public String toString() {
        return "Missing lines {" +
                " sheet=" + sheet +
                " line=" + line +
                " station=" + getStation() +
                " }";
    }
}
