/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

import jade.util.leap.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Rafal Tkaczyk
 */
public class Place implements Serializable {

    private static class SortPlacesByEvaluation implements Comparator<Place> {

        public int compare(Place place1, Place place2) {
            if (place1.evaluation < place2.evaluation) {
                return 1;
            } else if (place1.evaluation > place2.evaluation) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    private final int DAY;// id dnia
    private final int TIME;// id czasu
    private final int ROOM_NO;// nr pokoju
    private String roomer;// lokator
    private int evaluation;// ocena

    public Place(int DAY, int TIME, int ROOM_NO) {
        this.DAY = DAY;
        this.TIME = TIME;
        this.ROOM_NO = ROOM_NO;
        this.roomer = "";
        this.evaluation = 0;
    }

    public Place(int DAY, int TIME, int ROOM_NO, int evaluation) {
        this.DAY = DAY;
        this.TIME = TIME;
        this.ROOM_NO = ROOM_NO;
        this.roomer = "";
        this.evaluation = evaluation;
    }

    public Place(int DAY, int TIME, int ROOM_NO, String roomer) {
        this.DAY = DAY;
        this.TIME = TIME;
        this.ROOM_NO = ROOM_NO;
        this.roomer = roomer;
        this.evaluation = 0;
    }

    public Place(int DAY, int TIME, int ROOM_NO, String roomer, int evaluation) {
        this.DAY = DAY;
        this.TIME = TIME;
        this.ROOM_NO = ROOM_NO;
        this.roomer = roomer;
        this.evaluation = evaluation;
    }
    
    public int getDAY() {
        return DAY;
    }

    public int getTIME() {
        return TIME;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getROOM_NO() {
        return ROOM_NO;
    }

    public String getRoomer() {
        return roomer;
    }

    public void setRoomer(String roomer) {
        this.roomer = roomer;
    }
    
    public static void sortArrayListByEvaluation(ArrayList<Place> list) {
        Collections.sort(list, new SortPlacesByEvaluation());
    }

    @Override
    public String toString() {
        return "DAY(" + DAY + ") TIME(" + TIME + ") ROOM_NO(" + ROOM_NO
                + ") roomer(" + roomer + ") evaluation(" + evaluation + ")\n";
    }
}
