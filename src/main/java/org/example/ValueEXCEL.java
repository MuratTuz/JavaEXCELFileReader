package main.java.org.example;

public class ValueEXCEL {
    String sheet;
    String stationName;
    String nextStationName;
    int platform;
    int nextPlatform;
    int line;

    String getStation(){
        return new String(stationName+" (Platz "+platform+")");
    }
    String getNextStation(){
        return new String(nextStationName+" (Platz "+nextPlatform+")");
    }

    @Override
    public String toString() {
        return "Missing lines {" +
                " sheet=" + sheet +
                " line=" + line +
                " station=" + getStation() +
                " next station=" + getNextStation() +
                " }";
    }
}
