/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.RoomsAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class RoomsInitBehaviour extends CyclicBehaviour {
    
    private Logger logger;
    private HashMap<Integer, Integer> capacityOfRooms;// pojemność sal
    private HashMap<Integer, ArrayList<String>> equipmentOfRooms;// wyposażenie sal
    private HashMap<String, ArrayList<String>> equipmentOfSubjects;// wyposażenie potrzebne do prowadzenia przedmiotów
    private HashMap<String, ArrayList<Integer>> roomsOfSubjects;// pokoje spełniające wymogi sprzętowe przedmiotu
    
    public RoomsInitBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        this.capacityOfRooms = new HashMap<>();
        this.equipmentOfRooms = new HashMap<>();
        this.equipmentOfSubjects = new HashMap<>();
        this.roomsOfSubjects = new HashMap<>();
        logger.log(Level.INFO, "RoomsInitBehaviour run");
        sendQueryGetAllRooms();
    }
    
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            String sender = msg.getSender().getLocalName();
            
            if(sender.equals("DataBaseAgent")) {
                switch (msg.getConversationId()) {
                    case DataBaseMySQLBehaviour.GET_ALL_ROOMS:
                        try {
                            sendQueryGetAllRoomsRet((ArrayList<Integer>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "GET_ALL_ROOMS", ex);
                        }
                        break;
                    case DataBaseMySQLBehaviour.GET_EQUIPMENT_OF_ROOMS:
                        try {
                            sendQueryGetEquipmentOfRoomsRet((ArrayList<Object>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "GET_EQUIPMENT_OF_ROOMS", ex);
                        }
                        break;
                    case DataBaseMySQLBehaviour.GET_EQUIPMENT_OF_SUBJECTS:
                        try {
                            sendQueryGetEquipmentOfSubjectsRet((ArrayList<String>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "GET_EQUIPMENT_OF_ROOMS", ex);
                        }
                        break;
                    case DataBaseMySQLBehaviour.GET_ALL_SYMBOLS_OF_SUBJECTS:
                        try {
                            sendQueryGetAllSymbolsOfSubjectsRet((ArrayList<String>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "GET_EQUIPMENT_OF_ROOMS", ex);
                        }
                        break;
                    default:
                        notUnderstandMessage(sender, msg.getConversationId());
                        break;
                }
            } else {
                notUnderstandMessage(sender, msg.getConversationId());
            }
        } else {
            block();
        }
    }
    
    private void notUnderstandMessage(String sender, String title) {
        String log = "RoomsInitBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    private void sendQueryGetAllRooms() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_ALL_ROOMS);
        myAgent.send(msg);
    }
    
    private void sendQueryGetAllRoomsRet(ArrayList<Integer> data) {
        for(int i = 0, size = data.size(); i < size; i += 2) {
            this.capacityOfRooms.put(data.get(i), data.get(i+1));
        }
        ((RoomsAgent) myAgent).setCapacity(capacityOfRooms);
        sendQueryGetEquipmentOfRooms();
    }
    
    private void sendQueryGetEquipmentOfRooms() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_EQUIPMENT_OF_ROOMS);
        myAgent.send(msg);
    }
    
    private void sendQueryGetEquipmentOfRoomsRet(ArrayList<Object> data) {
        Integer number;
        String equip;
        for(int i = 0, size = data.size(); i < size; i += 2) {
            number = (Integer) data.get(i);
            equip = (String) data.get(i+1);
            if(equipmentOfRooms.containsKey(number)) {
                equipmentOfRooms.get(number).add(equip);
            } else {
                ArrayList<String> eq = new ArrayList<>();
                eq.add(equip);
                equipmentOfRooms.put(number, eq);
            }
        }
        sendQueryGetEquipmentOfSubjects();
    }
    
    private void sendQueryGetEquipmentOfSubjects() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_EQUIPMENT_OF_SUBJECTS);
        myAgent.send(msg);
    }
    
    private void sendQueryGetEquipmentOfSubjectsRet(ArrayList<String> data) {
        String sub, equip;
        for(int i = 0, size = data.size(); i < size; i += 2) {
            sub = (String) data.get(i);
            equip = (String) data.get(i+1);
            if(equipmentOfSubjects.containsKey(sub)) {
                equipmentOfSubjects.get(sub).add(equip);
            } else {
                ArrayList<String> eq = new ArrayList<>();
                eq.add(equip);
                equipmentOfSubjects.put(sub, eq);
            }
        }
        sendQueryGetAllSymbolsOfSubjects();
    }
    
    private void sendQueryGetAllSymbolsOfSubjects() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_ALL_SYMBOLS_OF_SUBJECTS);
        myAgent.send(msg);
    }
    
    private void sendQueryGetAllSymbolsOfSubjectsRet(ArrayList<String> data) {
        for(String subject : data) {
            roomsOfSubjects.put(subject, new ArrayList<Integer>());
        }
        searchRoomsForSubjects();
    }
    
    private void searchRoomsForSubjects() {
        for(String subject : roomsOfSubjects.keySet()) {
            if(equipmentOfSubjects.containsKey(subject) == false) {// przedmiot nic nie wymaga, każda sala jest dobra
                roomsOfSubjects.get(subject).addAll(new ArrayList<>(capacityOfRooms.keySet()));
            } else {
                checkEquipment(subject);
            }
        }
        
        ((RoomsAgent) myAgent).setRoomsOfSubjects(this.roomsOfSubjects);
        myAgent.addBehaviour(new RoomsInformationBehaviour(myAgent, logger));
        myAgent.removeBehaviour(this);
    }
    
    private void checkEquipment(String subject) {
        ArrayList<String> eqSub, eqRoom;
        eqSub = equipmentOfSubjects.get(subject);
        for (Integer room : equipmentOfRooms.keySet()) {// przegląd wypowsażenia każdej sali
            eqRoom = equipmentOfRooms.get(room);
            if (isCompatibility(eqSub, eqRoom)) {// sala się nadaje, ma wszystko co potrzeba
                roomsOfSubjects.get(subject).add(room);
            }
        }
    }
    
    private boolean isCompatibility(ArrayList<String> eqSub, ArrayList<String> eqRoom) {
        for(String eq : eqSub) {
            if(eqRoom.indexOf(eq) == -1) {// nie ma, nie spełnia wymagań
                return false;
            }
        }
        
        return true;
    }
}
