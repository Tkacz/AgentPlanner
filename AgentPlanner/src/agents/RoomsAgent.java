/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import behaviours.RoomsInitBehaviour;
import jade.core.Agent;
import jade.util.Logger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Rafal Tkaczyk
 */
public class RoomsAgent extends Agent {
    
    private Logger logger;
    private HashMap<Integer, Integer> capacityOfRooms;// pojemność sal
    private HashMap<String, ArrayList<Integer>> roomsOfSubjects;// pokoje spełniające wymogi sprzętowe przedmiotu
    
    @Override
    protected void setup() {
        this.capacityOfRooms = new HashMap<>();
        this.roomsOfSubjects = new HashMap<>();
        initLogger();
        logger.log(Level.INFO, "DataBaseAgent run");
        addBehaviour(new RoomsInitBehaviour(this, logger));
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
        logger.setUseParentHandlers(false);// zapis tylko do pliku, coby ekranu nie śmiecić
    }
    
    @Override
    protected void takeDown() {
        super.takeDown();
    }
    
    public void setCapacity(HashMap<Integer, Integer> capacity) {
        this.capacityOfRooms.putAll(capacity);
    }

    public void setRoomsOfSubjects(HashMap<String, ArrayList<Integer>> roomsOfSubjects) {
        this.roomsOfSubjects.putAll(roomsOfSubjects);
    }
    
    public ArrayList<Integer> getRoomsOfSubject(String subject, int capacity) {
        ArrayList<Integer> rooms = new ArrayList<>();
        rooms.addAll(roomsOfSubjects.get(subject));// sale, które spełniają wymagania sprzętowe
        
        for(int i = 0; i < rooms.size(); i++) {// wyrzucanie sal, które są za małe
            if(capacityOfRooms.get(rooms.get(i)) < capacity) {
                rooms.remove(i--);
            }
        }
        
        return rooms;
    }
}
