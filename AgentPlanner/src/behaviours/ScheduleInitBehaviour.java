/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class ScheduleInitBehaviour extends CyclicBehaviour {

    private Logger logger;
    
    public ScheduleInitBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "ScheduleInitBehaviour run");
        sendQueryGetTotalTeachersNumber();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {            
            String sender = msg.getSender().getLocalName();
            
            if(sender.equals("DataBaseAgent")) {
                switch(msg.getConversationId()) {
                    case DataBaseMySQLBehaviour.GET_TOTAL_TEACHERS_NUMBER:
                        try {
                            getTotalTeachersNumberRet((Integer) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "ScheduleInitBehaviour.action()", ex);
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
        String log = "ScheduleInitBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    private void sendQueryGetTotalTeachersNumber() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_TOTAL_TEACHERS_NUMBER);
        myAgent.send(msg);
    }
    
    private void getTotalTeachersNumberRet(int totalTeachersNo) {
        myAgent.addBehaviour(new ScheduleNegotiationBehaviour(myAgent, logger, totalTeachersNo));
        myAgent.removeBehaviour(this);
    }
}
