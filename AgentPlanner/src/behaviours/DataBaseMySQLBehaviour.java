/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.logging.Level;
import agentsElements.MySQLHandler;
import agentsElements.Place;
import agentsElements.ProposedPlace;
import jade.core.AID;
import jade.util.Logger;
import java.util.ArrayList;

/**
 *
 * @author Rafal Tkaczyk
 */
public class DataBaseMySQLBehaviour extends CyclicBehaviour {

    public static final String DATA_BASE_AGENT_READY = "DATA_BASE_AGENT_IS_READY";
    public static final String GET_ALL_ROOMS = "GET_ALL_ROOMS";
    public static final String GET_EQUIPMENT_OF_ROOMS = "GET_EQUIPMENT_OF_ROOMS";
    public static final String GET_EQUIPMENT_OF_SUBJECTS = "GET_EQUIPMENT_OF_SUBJECTS";
    public static final String GET_ALL_SYMBOLS_OF_SUBJECTS = "GET_ALL_SYMBOLS_OF_SUBJECTS";
    public static final String GET_TEACHERS_SYMBOLS = "GET_TEACHERS_SYMBOLS";
    public static final String GET_SCHEDULE_DATA = "GET_SCHEDULE_DATA";
    public static final String GET_DATA_FOR_TEACHER_AGENT =  "GET_DATA_FOR_TEACHER_AGENT";
    public static final String GET_SUBJECT_DATA_FOR_TEACHER = "GET_SUBJECT_DATA_FOR_TEACHER";
    public static final String GET_TOTAL_TEACHERS_NUMBER = "GET_TOTAL_TEACHERS_NUMBER";
    public static final String INSERT_GROUPS_INTO_PLAN = "INSERT_GROUPS_INTO_PLAN";
    public static final String INSERT_GROUP_INTO_PLAN = "INSERT_GROUP_INTO_PLAN";
    public static final String SET_GROUP_AS_REJECT = "SET_GROUP_AS_REJECT";
    public static final String CLOSE_CONNECTION = "CLOSE_CONNECTION";
    public static final String CHECK_COLLISIONS_OF_STUDENTS = "CHECK_COLLISIONS_OF_STUDENTS";
    
    private Logger logger;
    private MySQLHandler mysql;
    
    public DataBaseMySQLBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "DataBaseMySQLBehaviour run");
        mysql = new MySQLHandler(logger);
        sendToBootAgentDataBaseAgentIsReady();
    }
    
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            String sender = msg.getSender().getLocalName();
            
            if(sender.equals("BootAgent")) {
                switch(msg.getConversationId()) {
                    case GET_TEACHERS_SYMBOLS:
                        getTeachersSymbols(msg);
                        break;
                    default:
                        notUnderstandMessage(sender, msg.getConversationId());
                        break;
                }
            } else if(sender.startsWith("Teacher")) {
                switch (msg.getConversationId()) {
                    case GET_SCHEDULE_DATA:
                        getScheduleData(msg);
                        break;
                    case GET_DATA_FOR_TEACHER_AGENT:
                        getDataForTeacherAgent(msg, msg.getContent());
                        break;
                    case GET_SUBJECT_DATA_FOR_TEACHER:
                        getSubjectDataForTeacher(msg, msg.getContent());
                        break;
                    case SET_GROUP_AS_REJECT:
                        setGroupAsReject(msg);
                        break;
                    default:
                        notUnderstandMessage(sender, msg.getConversationId());
                        break;
                }
            } else if(sender.equals("ScheduleAgent")) {
                switch (msg.getConversationId()) {
                    case CHECK_COLLISIONS_OF_STUDENTS:
                        try {
                            checkCollisionsOfStudents(msg, (ArrayList<ProposedPlace>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.action()", ex);
                        }
                        break;
                    case GET_TOTAL_TEACHERS_NUMBER:
                        getTotalTeachersNumber(msg);
                        break;
                    case INSERT_GROUPS_INTO_PLAN:
                        try {
                            insertGroupsIntoPlan((ArrayList<Place>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.action()", ex);
                        }
                        break;
                    case INSERT_GROUP_INTO_PLAN:
                        try {
                            insertGroupIntoPlan((Place) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.action()", ex);
                        }
                        break;
                    case CLOSE_CONNECTION:
                        mysql.close();
                        break;
                    default:
                        notUnderstandMessage(sender, msg.getConversationId());
                        break;
                }
            } else if(sender.equals("RoomsAgent")) {
                switch(msg.getConversationId()) {
                    case GET_ALL_ROOMS:
                        getAllRooms(msg);
                        break;
                    case GET_EQUIPMENT_OF_ROOMS:
                        getEquipmentOfRooms(msg);
                        break;
                    case GET_EQUIPMENT_OF_SUBJECTS:
                        getEquipmentOfSubjects(msg);
                        break;
                    case GET_ALL_SYMBOLS_OF_SUBJECTS:
                        getALLSymbolsOfSubjects(msg);
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
    
    private void sendToBootAgentDataBaseAgentIsReady() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setConversationId(DATA_BASE_AGENT_READY);
        msg.addReceiver(new AID("BootAgent", AID.ISLOCALNAME));
        myAgent.send(msg);
    }
    
    private void getTeachersSymbols(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getTeachersSymbols());
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getTeachersSymbols()", ex);
        }
    }
    
    private void getSubjectDataForTeacher(ACLMessage msg, String symbol) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getSubjectDataForTeacher(symbol));
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getSubjectDataForTeacher()", ex);
        }
    }

    private void getDataForTeacherAgent(ACLMessage msg, String teacherSymbol) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getDataForTeacherAgent(teacherSymbol));
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getDataForTeacherAgent()", ex);
        }
    }
    
    private void getTotalTeachersNumber(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getTotalTeachersNumber());// Integer
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getDataForTeacherAgent()", ex);
        }
    }
    
    private void getScheduleData(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getScheduleData());
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getScheduleData()", ex);
        }
    }
    
    private void notUnderstandMessage(String sender, String title) {
        String log = "DataBaseMySQLBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    private void insertGroupsIntoPlan(ArrayList<Place> places) {
        for(Place place : places) {
            mysql.insertGroupIntoPlan(place.getRoomer(), place.getDAY(), place.getTIME(),
                    place.getROOM_NO());
        }
    }
    
    private void insertGroupIntoPlan(Place place) {
        mysql.insertGroupIntoPlan(place.getRoomer(), place.getDAY(), place.getTIME(), place.getROOM_NO());
    }
    
    private void getAllRooms(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getAllRooms());
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getRoomsForGroup()", ex);
        }
    }
    
    private void getEquipmentOfRooms(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getEquipmentOfRooms());
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getRoomsForGroup()", ex);
        }
    }
    
    private void getEquipmentOfSubjects(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getEquipmentOfSubjects());
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getRoomsForGroup()", ex);
        }
    }
    
    private void getALLSymbolsOfSubjects(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(mysql.getAllSymbolsOfSubjects());
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.getAllSymbolsOfSubjects()", ex);
        }
    }
    
    private void setGroupAsReject(ACLMessage msg) {
        Object[] data;
        try {
            data = (Object[]) msg.getContentObject();
        } catch (UnreadableException ex) {
            logger.log(Level.WARNING, "INSERT TO REJECT", ex);
            return;
        }
        
        mysql.setGroupAsReject((String) data[0], (String) data[1]);
    }
    
    private void checkCollisionsOfStudents(ACLMessage msg, ArrayList<ProposedPlace> proposedPlaces) {
        int studentsNo;
        int collisionStudentsNumber;
        int additonalEvaluation;
        int negotiationLevel = mysql.getNegotiationLevel();
        
        ArrayList<Object> answear = new ArrayList<>();        
        for(ProposedPlace place : proposedPlaces) {// check and add to answear
            studentsNo = mysql.getStudentsNumberOfGroup(place.getGROUP_SYMBOL());
            collisionStudentsNumber = mysql.getCollisionStudentsNumber(
                    place.getGROUP_SYMBOL(), place.getDAY(), place.getTIME());

            // próg kolizyjny został przekroczony
            if(studentsNo > 0 && negotiationLevel < (collisionStudentsNumber * 100 / studentsNo)) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                place.setEvaluation(0);// absolutnie nie można ustawić danej grupy w danym miejscu
            } else {// ok, próg nie został przekroczony i zostaje dodany do oceny ogólnej
                additonalEvaluation = (studentsNo - collisionStudentsNumber) + place.getEvaluation();
                place.setEvaluation(additonalEvaluation);
            }
            
            // spr czy w ogóle to miejsce jest wolne
            if(!mysql.isPlaceFree(place.getDAY(), place.getTIME(), place.getROOM_NO())) {
                place.setEvaluation(0);// absolutnie nie można ustawić danej grupy w danym miejscu
            }
            
            answear.add(place);// dodany, ponownie oceniony obiekt proponowanego miejsca
        }

        ACLMessage reply = msg.createReply();// create reply
        reply.setPerformative(ACLMessage.INFORM_REF);
        try {
            reply.setContentObject(answear);
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "DataBaseMySQLBehaviour.checkCollisions()", ex);
        }
    }
}
