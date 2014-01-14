/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentsElements.Place;
import agentsElements.ProposedPlace;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class ScheduleSearchNewPlaceBehaviour extends CyclicBehaviour {
    public static final String SEARCH_NEW_PLACE_REJECT_PROPOSAL = "SEARCH_NEW_PLACE_REJECT_PROPOSAL";
    public static final String SEARCH_NEW_PLACE_ACCEPT_PROPOSAL = "SEARCH_NEW_PLACE_ACCEPT_PROPOSAL";
    
    private final Logger logger;
    private ProposedPlace actualProposition;
    private MessageTemplate msgFilter;
    
    public ScheduleSearchNewPlaceBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "ScheduleSearchNewPlaceBehaviour run");
        initMessageTemplate();
    }
    
    private void initMessageTemplate() {
        MessageTemplate m1 = MessageTemplate.MatchConversationId(DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS_OF_REJECT_GROUP);
        MessageTemplate m2 = MessageTemplate.MatchConversationId(TeacherSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_TEACHER_PROPOSE);
        
        this.msgFilter = MessageTemplate.or(m1, m2);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgFilter);
        if (msg != null) {
            if(msg.getConversationId() == null) {
                String log = "ConversationId == null : sender: " + msg.getSender().getLocalName()
                        + "; content: " + msg.getContent();
                logger.log(Level.WARNING, log);
                return;
            }
            switch (msg.getConversationId()) {
                case TeacherSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_TEACHER_PROPOSE:
                    try {
                        teacherProposal((ProposedPlace) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleSearchNewPlaceBehaviour.action()", ex);
                    }
                    break;
                case DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS_OF_REJECT_GROUP:
                    try {
                        checkCollisionsOfStudentsRet((ArrayList<Object>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleSearchNewPlaceBehaviour.action()", ex);
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
        String log = "ScheduleNegotiationBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    private void teacherProposal(ProposedPlace proposition) {
        logger.log(Level.INFO, "SCHEDULE SEARCH NEW PLACE BEH new teacher proposal");
        this.actualProposition = proposition;
        checkCollisionsOfStudents();
    }

    private void checkCollisionsOfStudents() {
        logger.log(Level.INFO, "Schedule Search New Place - check collisions of students");
        
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS_OF_REJECT_GROUP);
        
        try {
            msg.setContentObject(this.actualProposition);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.checkCollisionsOfStudents()", ex);
        }
    }
    
    private void checkCollisionsOfStudentsRet(ArrayList<Object> newActualPropositions) {
        logger.log(Level.INFO, "Schedule Search New Place - check collisions of students return");
        this.actualProposition = (ProposedPlace) newActualPropositions.get(0);
        if(this.actualProposition.getEvaluation() == 0) {// odrzucono propozycję wykładowcy
            sendToTeacherRejectProposal();
        } else {
            sendChangePlaceInPlan(new Place(actualProposition.getDAY(), actualProposition.getTIME(),
                    actualProposition.getROOM_NO(), actualProposition.getGROUP_SYMBOL()));
            
            sendToTeacherAcceptProposal();
        }
    }
    
    private void sendChangePlaceInPlan(Place place) {
        logger.log(Level.INFO, "Schedule Search New Place - send change place in Plan");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.CHANGE_PLACE_IN_PLAN);
        try {
            msg.setContentObject(place);
            myAgent.send(msg);
        } catch(IOException ex) {
            logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.putPlacesWithNoCollisions()", ex);
        }
    }
    
    private void sendToTeacherAcceptProposal() {
        logger.log(Level.INFO, "Schedule Search New Place - send to teacher accept proposal");
        ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);        
        msg.setConversationId(SEARCH_NEW_PLACE_ACCEPT_PROPOSAL);
        msg.addReceiver(new AID("Teacher" + actualProposition.getTEACHER_SYMBOL(), AID.ISLOCALNAME));
        myAgent.send(msg);
    }
    
    private void sendToTeacherRejectProposal() {
        logger.log(Level.INFO, "Schedule Search New Place - send to teacher reject proposal");
        ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);        
        msg.setConversationId(SEARCH_NEW_PLACE_REJECT_PROPOSAL);
        msg.addReceiver(new AID("Teacher" + actualProposition.getTEACHER_SYMBOL(), AID.ISLOCALNAME));
        myAgent.send(msg);
    }
}
