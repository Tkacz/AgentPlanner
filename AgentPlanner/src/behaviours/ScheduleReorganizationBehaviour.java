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
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class ScheduleReorganizationBehaviour extends CyclicBehaviour {
    public static final String REORGANIZATION_BEGINNING = "REORGANIZATION_BEGINNING";
    public static final String REORGANIZATION_REJECT_PROPOSAL = "REORGANIZATION_REJECT_PROPOSAL";
    public static final String REORGANIZATION_ACCEPT_PROPOSAL = "REORGANIZATION_ACCEPT_PROPOSAL";
    public static final String REORGANIZATION_SEARCH_NEW_PLACE = "REORGANIZATION_SEARCH_NEW_PLACE";
    
    private final Logger logger;
    private final String teacherSymbol;
    private ProposedPlace actualProposition;
    private MessageTemplate msgFilter;
    
    public ScheduleReorganizationBehaviour(Agent a, Logger logger, String teacherSymbol) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, ("ScheduleReorganizationBehaviour for " + teacherSymbol + " run"));
        this.teacherSymbol = teacherSymbol;
        initMessageTemplate();
        sendInformReorganizationBeginning();
    }
    
    private void initMessageTemplate() {
        MessageTemplate mt1 = MessageTemplate.MatchConversationId(TeacherRejectGroupBehaviour.REJECT_GROUP_PROPOSE);
        MessageTemplate mt2 = MessageTemplate.MatchConversationId(DataBaseMySQLBehaviour.REJECT_GROUP_CHECK_COLLISION);
        MessageTemplate mt3 = MessageTemplate.MatchConversationId(TeacherSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_TEACHER_ACCEPT_PROPOSAL);
        MessageTemplate mt4 = MessageTemplate.MatchConversationId(TeacherSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_TEACHER_REJECT_PROPOSAL);
        
        this.msgFilter = MessageTemplate.or(MessageTemplate.or(mt1, mt2), MessageTemplate.or(mt3, mt4));
    }
    
    private void sendInformReorganizationBeginning() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
        msg.addReceiver(new AID("Teacher" + teacherSymbol, AID.ISLOCALNAME));
        msg.setConversationId(REORGANIZATION_BEGINNING);
        myAgent.send(msg);
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
                case TeacherRejectGroupBehaviour.REJECT_GROUP_PROPOSE:
                    try {
                        rejectGroupPropose((ProposedPlace) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleReorganizationBehaviour.action()", ex);
                    }
                    break;
                case DataBaseMySQLBehaviour.REJECT_GROUP_CHECK_COLLISION:
                    try {
                        checkCollisionRet((Object[]) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleReorganizationBehaviour.action()", ex);
                    }
                    break;
                case TeacherSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_TEACHER_ACCEPT_PROPOSAL:
                    acceptProposal();
                    break;
                case TeacherSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_TEACHER_REJECT_PROPOSAL:
                    sendToTeacherRejectProposal();
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
    
    private void rejectGroupPropose(ProposedPlace proposition) {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - new reject group proposal: " +
                proposition.getTEACHER_SYMBOL() + " : " + proposition.getGROUP_SYMBOL()
                + " : " + proposition.getDAY() + " : " + proposition.getTIME() + " : " + proposition.getROOM_NO()));
        this.actualProposition = proposition;
        checkCollision();
    }

    private void checkCollision() {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - check collision for: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.REJECT_GROUP_CHECK_COLLISION);
        
        try {
            msg.setContentObject(actualProposition);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "ScheduleRejectGroupBehaviour.checkCollision()", ex);
        }
    }
    
    private void checkCollisionRet(Object[] data) {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - check collision return for: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        this.actualProposition.setEvaluation((Integer) data[0]);
        if(actualProposition.getEvaluation() == 0) {
            sendToTeacherRejectProposal();
        } else {
            sendProposeToRoomerInPlan((String) data[1], (String) data[2]);
        }
    }
    
    private void sendProposeToRoomerInPlan(String roomerSymbol, String groupOfRoomerSymbol) {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - send propose to roomer in plan: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.addReceiver(new AID("Teacher" + roomerSymbol, AID.ISLOCALNAME));
        msg.setConversationId(REORGANIZATION_SEARCH_NEW_PLACE);
        try {
            msg.setContentObject(new Object[]{groupOfRoomerSymbol, actualProposition.getDAY(),
                actualProposition.getTIME(), actualProposition.getROOM_NO()});
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "ScheduleReorganizationBehaviour.sendProposeToRoomerInPlan()", ex);
        }
    }
    
    private void acceptProposal() {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - accept proposal: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        sendInsertGroupIntoPlan();
        sendToTeacherAcceptProposal();
    }
    
    private void sendInsertGroupIntoPlan() {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - send insert group into Plan: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.INSERT_GROUP_INTO_PLAN);
        try {
            msg.setContentObject(new Place(actualProposition.getDAY(), actualProposition.getTIME(),
                    actualProposition.getROOM_NO(), actualProposition.getGROUP_SYMBOL()));
            myAgent.send(msg);
        } catch(IOException ex) {
            logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.putPlacesWithNoCollisions()", ex);
        }
    }
    
    private void sendToTeacherAcceptProposal() {        
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - send to teacher accept proposal: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);        
        msg.setConversationId(REORGANIZATION_ACCEPT_PROPOSAL);
        msg.addReceiver(new AID("Teacher" + teacherSymbol, AID.ISLOCALNAME));
        myAgent.send(msg);
    }
    
    private void sendToTeacherRejectProposal() {
        logger.log(Level.INFO, ("Schedule Reorganization Behaviour - send to teacher reject proposal: " +
                actualProposition.getTEACHER_SYMBOL() + " : " + actualProposition.getGROUP_SYMBOL()
                + " : " + actualProposition.getDAY() + " : " + actualProposition.getTIME() + " : " + actualProposition.getROOM_NO()));
        ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);        
        msg.setConversationId(REORGANIZATION_REJECT_PROPOSAL);
        msg.addReceiver(new AID("Teacher" + teacherSymbol, AID.ISLOCALNAME));
        myAgent.send(msg);
    }
}
