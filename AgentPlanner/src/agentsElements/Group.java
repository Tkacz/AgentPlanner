/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

import java.util.ArrayList;

/**
 *
 * @author Rafal Tkaczyk
 */
public class Group {
    private final String groupSymbol;// symbol grupy
    private final int groupStud_no;// ilość studentów w grupie
    private final String subjectSymbol;// symbol przedmiotu
    private final int subTime;// czas trwania przedmiotu (w jednostkach planowych)
    private final int subPriority;// priorytet przedmiotu
    private ArrayList<Integer> rooms;// nry sal, które spełniają wymagania tej grupy i przedmiotu

    public Group(String groupSymbol, int groupStud_no, String subjectSymbol, int subTime, int subPriority) {
        this.groupSymbol = groupSymbol;
        this.groupStud_no = groupStud_no;
        this.subjectSymbol = subjectSymbol;
        this.subTime = subTime;
        this.subPriority = subPriority;
        this.rooms = new ArrayList<>();
    }

    public int getGroupStud_no() {
        return groupStud_no;
    }

    public String getGroupSymbol() {
        return groupSymbol;
    }

    public String getSubjectSymbol() {
        return subjectSymbol;
    }

    public int getSubPriority() {
        return subPriority;
    }

    public int getSubTime() {
        return subTime;
    }

    public ArrayList<Integer> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Integer> rooms) {
        this.rooms.addAll(rooms);
    }

    @Override
    public String toString() {
        String print = "Group Symbol: " + this.groupSymbol
                + "; Group stud_no: " + this.groupStud_no
                + "; Subject Symbol: " + this.subjectSymbol
                + "; Subject Time: " + this.subTime
                + "; Subject Priority: " + this.subPriority;
        int roomsSize = rooms.size();
        if(roomsSize == 1) {
            print += "; Rooms: [" + rooms.get(0) + "]";
        } else if(roomsSize > 1) {
            print += "; Rooms: [" + rooms.get(0);
            for(int i = 1; i < roomsSize-1; i++) {
                print += ", " + rooms.get(i);
            }
            print += ", " + rooms.get(roomsSize-1) + "]";
        } else {
            print += "; Rooms: []";
        }
        
        return print;
    }
}
