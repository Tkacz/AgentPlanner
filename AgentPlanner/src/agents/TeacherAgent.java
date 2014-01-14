/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import agentsElements.Group;
import agentsElements.Place;
import agentsElements.Schedule;
import agentsElements.Teacher;
import behaviours.TeacherInitBehaviour;
import jade.core.Agent;
import jade.util.Logger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author RafalTkaczyk
 */
public class TeacherAgent extends Agent {

    class SortSubjectGroupByPriority implements Comparator<Group> {

        // sortowanie malejąco
	@Override
        public int compare(Group sg1, Group sg2) {
            if (sg1.getSubPriority() < sg2.getSubPriority()) {
                return 1;
            } else if (sg1.getSubPriority() > sg2.getSubPriority()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private Logger logger;
    private Teacher teacher;
    private ArrayList<Group> myGroups;//groups of Teacher
    private ArrayList<Group> failedGroups;
    private ArrayList<Group> okGroups;
    private Schedule mySchedule;
    
    @Override
    protected void setup() {
        super.setup();
        initLogger();
        this.myGroups = new ArrayList<>();
        this.failedGroups = new ArrayList<>();
        this.okGroups = new ArrayList<>();
        logger.log(Level.INFO, "TeacherAgent run");
        addBehaviour(new TeacherInitBehaviour(this, logger));
    }
    
    private void initLogger() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());
        FileHandler fileHandler = null;
        try {
             fileHandler = new FileHandler("log/" + dateStr + "_" + getLocalName() + ".log", true);
             fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException | SecurityException ex) {
            System.err.println(ex.toString());
        }
        logger = jade.util.Logger.getMyLogger(this.getClass().getName());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);// zapis tylko do pliku
    }
    
    public void initTeacher(Object data[]) {
        this.teacher = new Teacher(data);
    }
    
    public void initSchedule(int days, int times, ArrayList<Integer> roomNos) {
        this.mySchedule = new Schedule(days, times, roomNos);
    }
    
    public void takePlace(int day, int time, int roomNo, Group group) {
        mySchedule.setRoomerIn(day, time, roomNo, group.getGroupSymbol());
        okGroups.add(group);
    }
    
    public void setPlaceFree(int day, int time, int roomNo) {
        mySchedule.setRoomerIn(day, time, roomNo, "");
    }
    
    public String getTeacherSymbol() {
        return this.teacher.getSymbol();
    }

    public void createGroupObject(String group, int stud_no, String subject, int time, int priority) {
        myGroups.add(new Group(
                group,//g.symbol
                stud_no,// g.stud_no
                subject,// s.symbol
                time,// s.time
                priority));// st.priority
    }
    
    /**
     * Sortowanie zajęć względem priorytetu.
     */
    public void sortGroups() {
        Collections.sort(myGroups, new SortSubjectGroupByPriority());
    }
    
    public String getSymbol() {
        return this.teacher.getSymbol();
    }
    
    public Group getNextGroup() {
        if(myGroups.isEmpty()) {
            return null;
        } else {
            Group group = myGroups.get(0);
            myGroups.remove(0);
            return group;
        }
    }
    
    public void addToFailedGroups(Group group) {
        this.failedGroups.add(group);
    }

    public ArrayList<Group> getMyGroups() {
        return myGroups;
    }
    
    public void setRoomsOfGroup(String groupSymbol, ArrayList<Integer> rooms) {
        for(Group group : myGroups) {
            if(group.getGroupSymbol().equals(groupSymbol)) {
                group.setRooms(rooms);
                break;
            }
        }
    }
    
    public ArrayList<Place> evaluatePlacesForGroup(Group group) {
        
        return this.mySchedule.evaluatePlacesForGroup(
                group.getSubPriority(),
                this.teacher.getDaysPriority(),
                this.teacher.getTimesPriority(),
                group.getRooms());
    }
    
    public ArrayList<Place> evaluateAllPlacesForGroup(Group group) {
        
        return this.mySchedule.evaluateAllPlacesForGroup(
                group.getSubPriority(),
                this.teacher.getDaysPriority(),
                this.teacher.getTimesPriority(),
                group.getRooms());
    }
    
    public void printFailedGroups() {
//        System.out.print(getSymbol() + ":");
//        for(String str : okGroups) System.out.print(" [" + str + "]");
//        System.out.println();
        
//        System.out.print(getSymbol() + " reject groups:");
//        for(Group group : failedGroups) System.out.print(" [" + group.getGroupSymbol() + "]");
//        System.out.println();
    }
    
    public void addToOkGroups(Group group) {
        this.okGroups.add(group);
    }
    
    public Group getOkGroup(String symbol) {
        for(Group group : okGroups) {
            if(symbol.equals(group.getGroupSymbol())) {
                return group;
            }
        }
        return null;
    }
    
    public void printGroupEvaluation() {
        String info = getSymbol();
        for(Group group : myGroups) {
            info += " (" + group.getGroupSymbol() + " : " + group.getSubPriority() + ")";
        }
        System.out.println(info);
    }
}
