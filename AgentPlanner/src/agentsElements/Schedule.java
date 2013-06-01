/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Rafal Tkaczyk
 */
public class Schedule {
    
    private final int DAYS;
    private final int TIMES;
    private final int allRoomNumbers[];
    
    /**
     * 3D Schedule which consists of:
     * First array containst DAYS, second TIMES.
     * Key od HashMap is room number.
     * Value of HashMap is symbol of agent who occupy this room or empty string ("") if it's empty.
     */
    ArrayList<ArrayList<HashMap<Integer, String>>> schedule;

    public Schedule(int DAYS, int TIMES, int[] allRoomNumbers) {
        this.DAYS = DAYS;
        this.TIMES = TIMES;
        this.allRoomNumbers = new int[allRoomNumbers.length];
        System.arraycopy(allRoomNumbers, 0, this.allRoomNumbers, 0, allRoomNumbers.length);
        initSchedule();
    }
    
    public Schedule(int DAYS, int TIMES, ArrayList<Integer> allRoomNumbers) {
        this.DAYS = DAYS;
        this.TIMES = TIMES;
        int size = allRoomNumbers.size();
        this.allRoomNumbers = new int[size];
        for(int i = 0; i < size; i++) {
            this.allRoomNumbers[i] = allRoomNumbers.get(i);
        }
        initSchedule();
    }
    
    /**
     * Make empty schedule.
     */
    private void initSchedule() {
        this.schedule = new ArrayList<>();
        for(int i = 0; i < this.DAYS; i++) {// add days
            this.schedule.add(new ArrayList<HashMap<Integer, String>>());
            for(int j = 0; j < this.TIMES; j++) {// add times
                this.schedule.get(i).add(new HashMap<Integer, String>());
                for(Integer roomNo : this.allRoomNumbers) {// put all rooms
                    this.schedule.get(i).get(j).put(roomNo, "");// put room and set as empty
                }
            }
        }
    }
    
    /**
     * Get roomer on day, of time, in room.
     * @param day Number of day.
     * @param time Number of time.
     * @param roomNo Room number.
     * @return Empty String ("") if empty or roomer's symbol or null if there is no 'roomNo'.
     */
    public String getRoomerAt(int day, int time, int roomNo) {
        return this.schedule.get(day).get(time).get(roomNo);
    }
    
    /**
     * Set roomer on day, of time, in room number.
     * @param day Number of day.
     * @param time Number of time.
     * @param roomNumber Room number.
     * @param newRoomer New roomer's symbol.
     * @return false if unsuccessful or true if successful.
     */
    public void setRoomerIn(int day, int time, int roomNumber, String newRoomer) {
        this.schedule.get(day).get(time).put(roomNumber, newRoomer);// set new roomer
    }

    public int getDAYS() {
        return DAYS;
    }

    public int getTIMES() {
        return TIMES;
    }
    
    /**
     * Zwraca listę ocenionych miejsc dla danej grupy, na podstawie nadających się sal 'properRooms' planu 'schedule' i preferencji wykładowcy.
     * @param subjectPriority Priorytet przedmiotu.
     * @param daysPriority Priorytety dni.
     * @param timesPriority Priorytety czasów (pierwszy wymier to dni).
     * @param properRooms Numery sal, które spełniają wymagania.
     * @return Lista ocenianych miejsc.
     */
    public ArrayList<Place> evaluatePlacesForGroup(int subjectPriority, int daysPriority[],
            int timesPriority[][], ArrayList<Integer> properRooms) {
        
        ArrayList<Place> result = new ArrayList<>();
        
        if(properRooms == null) {
            return result;
        }
        
        for(int day = 0; day < DAYS; day++) {
            for(int time = 0; time < TIMES; time++) {
                for(Integer roomNo : schedule.get(day).get(time).keySet()) {
                    int evaluation = subjectPriority;// pierwsza ocena to priorytet przedmiotu
                    if(!properRooms.contains(roomNo)) {// sprawdzanie, czy sala spełnia wymagania, jeśli nawet nie daje do wyniku
                        continue;
                    }
                    
                    evaluation *= daysPriority[day];// mnożenie oceny przez priorytet dnia wykładowcy
                    if (evaluation == 0) {// nie dodajemy do wyniku
                        continue;
                    }
                    
                    evaluation *= timesPriority[day][time];// mnożenie oceny przez priorytet czasu w danym dniu wykł.
                    if(evaluation == 0) {
                        continue;
                    }
                    
                    ///////////// KOLIZJA Z INNĄ GRUPĄ
                    if(isAnyGroupsInTime(day, time)) {// jeśli w tym czasie są już zajęcia to nie dodajemy do wyniku
                        continue;
                    }
                    ////////////
                    
                    if(time > 0 && isAnyGroupsInTime(day, time-1)) {// jeśli zaraz przed jest grupa prowadzona przez tego wykładowcę
                        evaluation += 5;
                    }
                    
                    if(time+1 < TIMES && isAnyGroupsInTime(day, time+1)) {// jeśli zaraz po jest grupa prowadzona przez tego wykładowcę 
                        evaluation += 5;
                    }
                    
                    evaluation += getGroupsInDay(day);// jeśli tego dnia są jeszcze jakieś zajęcia
                    
                    evaluation += new Random().nextInt(5);// czynnik losowy, daje nieznaczną różnicę w ocenie, ale dzięki temu przy każdym uruchomieniu wynik posortowanej listy miejsc może się nieco różnić, dzięki czemu nie ootrzymamy dokładnie takiego samego planu wynikowego
                    
                    result.add(new Place(day, time, roomNo, evaluation));// dodajemy ocenione miejsce
                }
            }
        }
        
        return result;
    }
    
    /**
     * Sprawdza czy w danym czasie są jakieś zajęcia.
     * @param day Dzień.
     * @param time Czas.
     * @return True jeśli są zajęcia w danym czasie, false jeśli nie.
     */
    private boolean isAnyGroupsInTime(int day, int time) {
        HashMap<Integer, String> tempTime = schedule.get(day).get(time);
        for(Integer roomId : tempTime.keySet()) {
            if(!tempTime.get(roomId).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sprawdza czy w danym dniu są już jakieś zajęcia i zwraca ich ilość.
     * @param day Dzień.
     * @return Ilość zajęć w ciągu dnia.
     */
    private int getGroupsInDay(int day) {
        int result = 0;
        for(int time = 0; time < TIMES; time++) {
            HashMap<Integer, String> tempTime = schedule.get(day).get(time);
            for (Integer roomId : tempTime.keySet()) {
                if (!tempTime.get(roomId).isEmpty()) {
                    result += 3;
                }
            }
        }
        return result;
    }
    
    public String isCollisionInTime(int day, int time) {
        HashMap<Integer, String> tempTime = schedule.get(day).get(time);
        for(Integer roomId : tempTime.keySet()) {
            if(!tempTime.get(roomId).isEmpty()) {
                return tempTime.get(roomId);
            }
        }
        return "";
    }

    @Override
    public String toString() {
        String print = "", symbol;
        
        for(int day = 0; day < DAYS; day++) {
            print += "DAY: " + (day+1) + "\n";
            for(int time = 0; time < TIMES; time++) {
                print += "TIME: " + (time+1) + "\n";
                for(Integer i : schedule.get(day).get(time).keySet()) {
                    symbol = schedule.get(day).get(time).get(i);
                    if(symbol.length() > 0) {
                        print += symbol + "\n";
                    }
                }
            }
            print += "======================================================\n\n";
        }
        
        return print;
    }
}
