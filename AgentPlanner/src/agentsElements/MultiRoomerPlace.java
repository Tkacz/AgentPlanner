/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Rafal Tkaczyk
 * 
 * Gromadzi grupy z propozycji ubiegających się o to samo miejsce.
 * 
 */
public class MultiRoomerPlace {

    private static class SortMultiRoomerPlaceByEvaluation implements Comparator<MultiRoomerPlace> {

	@Override
        public int compare(MultiRoomerPlace place1, MultiRoomerPlace place2) {
            if (place1.getRoomerAt(0).getEvaluation() < place2.getRoomerAt(0).getEvaluation()) {
                return 1;
            } else if (place1.getRoomerAt(0).getEvaluation() > place2.getRoomerAt(0).getEvaluation()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    private final int DAY, TIME, ROOM_NO;// współrzędne
    private final ArrayList<ProposedPlace> roomers;// propozycje przysłane przez agentów wykładowców
    
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
	return this.DAY == day && this.TIME == time && this.ROOM_NO == room_no;
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
    
    /**
     * Sortowanie listy po ocenach pierwszych lokatorów (listy lokatorów też są sortowane).
     * @param list Lista lokatorów.
     */
    public static void sortArrayListByEvaluation(ArrayList<MultiRoomerPlace> list) {
	
	for(MultiRoomerPlace place : list) {// sortowanie listy lokatorów
	    place.sortRoomers();
	}
	
        Collections.sort(list, new SortMultiRoomerPlaceByEvaluation());// sortowanie wszystkiego
    }
    
    public void sortRoomers() {
	ProposedPlace.sortArrayListByEvaluation(roomers);// sortowanie wyników

	ArrayList<ProposedPlace> contestants = new ArrayList<>();
	contestants.add(roomers.get(0));// dodanie pierwszego zawodnika w konkursie
	int evaluation = contestants.get(0).getEvaluation();// zapamiętanie najlepszego wyniku w konkursie
	for (int i = 1, allContestants = contestants.size(); i < allContestants; i++) {// jeżeli jest kilka wyników z tą samą oceną, to zebranie ich do osobnej listy
	    if (roomers.get(i).getEvaluation() == evaluation) {
		contestants.add(roomers.get(i));
	    } else {// gorszy wynik
		break;
	    }
	}
	
	ArrayList<Integer> ids = new ArrayList<>();
	for(int i = 0, allContestants = contestants.size(); i < allContestants; i++) {
	    ids.add(i);
	}
	
	int idRoomers, idIds;
	Random rand = new Random();
	while(!ids.isEmpty()) {
	    idIds = rand.nextInt(ids.size());
	    idRoomers = ids.get(idIds);
	    roomers.set(idRoomers, contestants.get(idRoomers));
	    ids.remove(idIds);
	}// (??) do sprawdzenia czy to dobrze działa
	
	contestants.clear();
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
