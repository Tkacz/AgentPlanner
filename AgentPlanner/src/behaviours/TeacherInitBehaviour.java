/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.TeacherAgent;
import agentsElements.Group;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import jade.core.AID;
import jade.util.Logger;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rafal Tkaczyk
 */
public class TeacherInitBehaviour extends CyclicBehaviour {

    private Logger logger;
    
    public TeacherInitBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "TeacherInitBehaviour run");
        sendQueryForScheduleInit();
    }
    
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            if (msg.getConversationId() == null) {
                String log = "ConversationId == null : sender: " + msg.getSender().getLocalName()
                        + "; content: " + msg.getContent();
                logger.log(Level.WARNING, log);
                return;
            }
                
            switch (msg.getConversationId()) {
                case DataBaseMySQLBehaviour.GET_DATA_FOR_TEACHER_AGENT:
                    try {
                        getDataForTeacherAgentRet((ArrayList<Object>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "TeacherInitBehaviour.action()", ex);
                    }
                    break;
                case DataBaseMySQLBehaviour.GET_SUBJECT_DATA_FOR_TEACHER: // odebrano dane potrzebne do utworzenia agenta
                    try {
                        getSubjectDataForTeacherRet((ArrayList<Object>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "TeacherInitBehaviour.action()", ex);
                    }
                    break;
                case DataBaseMySQLBehaviour.GET_SCHEDULE_DATA:
                    try {
                        initSchedule((ArrayList<Object>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "TeacherInitBehaviour.action()", ex);
                    }
                    break;
                case RoomsInformationBehaviour.GET_ROOMS_FOR_GROUPS:
                    try {
                        getRoomsForGroupsRet((ArrayList<Object>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "TeacherInitBehaviour.action()", ex);
                    }
                    break;
                default:
                    notUnderstandMessage(msg.getSender().getLocalName(), msg.getConversationId());
                    break;
            }
        } else {
            block();
        }
    }
    
    private void notUnderstandMessage(String sender, String title) {
        String log = "TeacherInitBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    /**
     * Send query for basic info: days, times, room numbers.
     */
    private void sendQueryForScheduleInit() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_SCHEDULE_DATA);
        myAgent.send(msg);
    }
    
    /**
     * Init schdule object in ScheduleAgent, run ScheduleMsgHandlerBehaviour and kill himself.
     * @param msgo 
     */
    private void initSchedule(ArrayList<Object> data) {
        ((TeacherAgent) myAgent).initSchedule((Integer) data.get(0), (Integer) data.get(1),
                (ArrayList<Integer>) data.get(2));
        
        sendQueryGetDataForTeacherAgent();
    }
    
    private void sendQueryGetDataForTeacherAgent() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_DATA_FOR_TEACHER_AGENT);
        msg.setContent(myAgent.getLocalName().substring(("Teacher").length()));// symbol
        myAgent.send(msg);
    }
    
    private void getDataForTeacherAgentRet(ArrayList<Object> data) {
        ((TeacherAgent) myAgent).initTeacher(data.toArray());
        sendQueryGetSubjectDataForTeacher();
    }
    
    private void sendQueryGetSubjectDataForTeacher() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_SUBJECT_DATA_FOR_TEACHER);
        msg.setContent(((TeacherAgent) myAgent).getTeacherSymbol());
        myAgent.send(msg);
    }
    
    private void getSubjectDataForTeacherRet(ArrayList<Object> data) {
        Object[] row = new Object[5];
        String group, subject;
        int stud_no, time, priority;
        
        for(Object obj : data) {
            row = (Object[]) obj;
            group = (String) row[0];
            stud_no = (Integer) row[1];
            subject = (String) row[2];
            time = (Integer) row[3];
            priority = (Integer) row[4];
            
            ((TeacherAgent) myAgent).createGroupObject(group, stud_no, subject, time, priority);
        }
        
        getRoomsForGroups();
    }
    
    private void getRoomsForGroups() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("RoomsAgent", AID.ISLOCALNAME));
        msg.setConversationId(RoomsInformationBehaviour.GET_ROOMS_FOR_GROUPS);
        
        ArrayList<Object> query = new ArrayList<>();
        for(Group group : ((TeacherAgent) myAgent).getMyGroups()) {
            query.add(group.getGroupSymbol());// symbol grupy
            query.add(group.getSubjectSymbol());// symbol przedmiotu
            query.add(group.getGroupStud_no());// ilość studentów (wymagana pojemność sali)
        }
        
        try {
            msg.setContentObject(query);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "SEND MESSAGE TO ROOMS AGENT", ex);
        }
    }
    
    private void getRoomsForGroupsRet(ArrayList<Object> data) {
        
        for(int i = 0, size = data.size(); i < size; i += 2) {// ustawianie każde z grup sal
            ((TeacherAgent) myAgent).setRoomsOfGroup((String) data.get(i), (ArrayList<Integer>) data.get(i+1));
        }
        
        
        myAgent.addBehaviour(new TeacherNegotiationBehaviour(myAgent, logger));
        myAgent.removeBehaviour(this);
    }
}
