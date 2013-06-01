/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.BootAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class BootRoomsAgentBehaviour extends CyclicBehaviour {
    
    private Logger logger;

    public BootRoomsAgentBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "BootRoomsAgentBehaviour run");
        runRoomsAgent();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {            
            String sender = msg.getSender().getLocalName();
            if (sender.startsWith("RoomsAgent")) {
                if(msg.getConversationId().equals(RoomsInformationBehaviour.ROOMS_AGENT_READY)) {
                    System.out.println("ROOMS AGENT READY");
                    ((BootAgent) myAgent).makeSchedule();
                    myAgent.removeBehaviour(this);
                }
            }
        } else {
            block();
        }
    }
    
    private void runRoomsAgent() {
        AgentController dbAgent = null;
        try {
            dbAgent = myAgent.getContainerController().createNewAgent("RoomsAgent", "agents.RoomsAgent", null);
            dbAgent.start();
        } catch (StaleProxyException ex) {
            logger.log(Level.WARNING, "BootRoomsAgentBehaviour.runRoomsAgent()", ex);
        }
    }
}
