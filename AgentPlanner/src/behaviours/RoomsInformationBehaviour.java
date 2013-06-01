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
import jade.wrapper.ControllerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class RoomsInformationBehaviour extends CyclicBehaviour {
    
    public static final String ROOMS_AGENT_READY = "ROOMS_AGENT_READY";
    public static final String GET_ROOMS_FOR_GROUPS = "GET_ROOMS_FOR_GROUPS";
    public static final String KILL_ROOMS_AGENT = "KILL_ROOMS_AGENT";
    
    private Logger logger;
    
    public RoomsInformationBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "RoomsInformationBehaviour run");
        sendInfoRoomsAgentReady();
    }
    
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            if (msg.getConversationId() == null) {
                String log = "ConversationId == null : sender: " + msg.getSender().getLocalName()
                        + "; content: " + msg.getContent();
                logger.log(Level.WARNING, log);
                return;
            }

            switch (msg.getConversationId()) {
                case GET_ROOMS_FOR_GROUPS:
                    getRoomsForGroup(msg);
                    break;
                case KILL_ROOMS_AGENT:
                    try {
                        myAgent.getContainerController().getAgent(myAgent.getLocalName()).kill();
                    } catch (ControllerException ex) {
                        logger.log(Level.WARNING, "ROOMS AGENT DON'T WANT TO DIE", ex);
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
        String log = "RoomsInitBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    private void sendInfoRoomsAgentReady() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("BootAgent", AID.ISLOCALNAME));
        msg.setConversationId(ROOMS_AGENT_READY);
        myAgent.send(msg);
    }
    
    private void getRoomsForGroup(ACLMessage msg) {
        ArrayList<Object> data = new ArrayList<>();
        try {
            data = (ArrayList<Object>) msg.getContentObject();
        } catch (UnreadableException ex) {
            logger.log(Level.WARNING, "getRoomsForGroup", ex);
            return;
        }
        
        /* od wykładowcy przychodzi: symbol grupy (potrzebny do odpowiedzi),
         * symbol przedmiotu (potrzebny do znalezienia sal z odpowiednim wyposażeniem),
         * ilość studentów (potrzebna do wybrania sal z odpowiednią pojemnością*/
        
        ArrayList<Object> answear = new ArrayList<>();
        String group, subject;
        Integer capacity;
        for(int i = 0, size = data.size(); i < size; i += 3) {
            group = (String) data.get(i);
            subject = (String) data.get(i+1);
            capacity = (Integer) data.get(i+2);
            answear.add(group);
            answear.add(((RoomsAgent) myAgent).getRoomsOfSubject(subject, capacity));
        }
        
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(answear);
            myAgent.send(reply);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "getRoomsForGroup", ex);
        }
    }
}
