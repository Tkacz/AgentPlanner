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
public class ProposedPlace implements Serializable {
    
    private static class SortProposedPlaceByPriority implements Comparator<ProposedPlace> {

	@Override
        public int compare(ProposedPlace place1, ProposedPlace place2) {
            if (place1.PRIORITY < place2.PRIORITY) {
                return 1;
            } else if (place1.PRIORITY > place2.PRIORITY) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    private static class SortProposedPlaceByEvaluation implements Comparator<ProposedPlace> {

	@Override
        public int compare(ProposedPlace place1, ProposedPlace place2) {
            if (place1.evaluation < place2.evaluation) {
                return 1;
            } else if (place1.evaluation > place2.evaluation) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    private final String TEACHER_SYMBOL;
    private final String GROUP_SYMBOL;
    private final int DAY;
    private final int TIME;
    private final int ROOM_NO;
    private int evaluation;
    private final int PRIORITY;
    private final int REQUIRED_CAPACITY;

    public ProposedPlace(String TEACHER_SYMBOL, String GROUP_SYMBOL, int DAY, int TIME, int ROOM_NO, int evaluation, int PRIORITY, int REQUIRED_CAPACITY) {
        this.TEACHER_SYMBOL = TEACHER_SYMBOL;
        this.GROUP_SYMBOL = GROUP_SYMBOL;
        this.DAY = DAY;
        this.TIME = TIME;
        this.ROOM_NO = ROOM_NO;
        this.PRIORITY = PRIORITY;
        this.evaluation = evaluation;
        this.REQUIRED_CAPACITY = REQUIRED_CAPACITY;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
    
    public int getDAY() {
        return DAY;
    }

    public String getGROUP_SYMBOL() {
        return GROUP_SYMBOL;
    }

    public int getREQUIRED_CAPACITY() {
        return REQUIRED_CAPACITY;
    }

    public int getROOM_NO() {
        return ROOM_NO;
    }

    public String getTEACHER_SYMBOL() {
        return TEACHER_SYMBOL;
    }

    public int getTIME() {
        return TIME;
    }

    public int getPRIORITY() {
        return PRIORITY;
    }
    
    public static void sortArrayListByPriority(ArrayList<ProposedPlace> list) {
        Collections.sort(list, new SortProposedPlaceByPriority());
    }
    
    public static void sortArrayListByEvaluation(ArrayList<ProposedPlace> list) {
        Collections.sort(list, new SortProposedPlaceByEvaluation());
    }

    @Override
    public String toString() {
        String print = "Teacher: " + this.TEACHER_SYMBOL
                + " Group: " + this.GROUP_SYMBOL
                + " Day: " + this.DAY
                + " Time: " + this.TIME
                + " Room No: " + this.ROOM_NO
                + " Evaluation: " + this.evaluation
                + " Capacity: " + this.REQUIRED_CAPACITY;
        return print;
    }
}
