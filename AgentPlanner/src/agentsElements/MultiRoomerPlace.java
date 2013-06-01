/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

import java.util.ArrayList;

/**
 *
 * @author Rafal Tkaczyk
 * 
 * Gromadzi grupy z propozycji ubiegających się o to samo miejsce.
 * 
 */
public class MultiRoomerPlace {

    private final int DAY, TIME, ROOM_NO;
    private ArrayList<ProposedPlace> roomers;

    public MultiRoomerPlace(int day, int time, int room_no) {
        this.DAY = day;
        this.TIME = time;
        this.ROOM_NO = room_no;
        this.roomers = new ArrayList<>();
    }
    
    public MultiRoomerPlace(ProposedPlace roomer) {
        this.DAY = roomer.getDAY();
        this.TIME = roomer.getTIME();
        this.ROOM_NO = roomer.getROOM_NO();
        this.roomers = new ArrayList<>();
        this.roomers.add(roomer);
    }

    public boolean addRoomer(ProposedPlace place) {
        if(this.DAY == place.getDAY() && this.TIME == place.getTIME() && this.ROOM_NO == place.getROOM_NO()) {
            this.roomers.add(place);
            return true;
        } else {
            return false;
        }
    }

    public int getRommersNumber() {
        return this.roomers.size();
    }

    public ProposedPlace getRoomerAt(int i) {
        return this.roomers.get(i);
    }

    public ArrayList<ProposedPlace> getRoomers() {
        return this.roomers;
    }

    public boolean equal(int day, int time, int room_no) {
        if(this.DAY == day && this.TIME == time && this.ROOM_NO == room_no) {
            return true;
        } else {
            return false;
        }
    }

    public int getDAY() {
        return DAY;
    }

    public int getROOM_NO() {
        return ROOM_NO;
    }

    public int getTIME() {
        return TIME;
    }
    
    public void sortRoomersByEvaluation() {
        ProposedPlace.sortArrayListByEvaluation(roomers);
    }

    @Override
    public String toString() {
        String print = "Day: " + DAY
                + "\nTime: " + TIME
                + "\nRoom_no: " + ROOM_NO
                + "\nRoomers: " + roomers;
        return print;
    }
}
