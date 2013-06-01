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
public class BootDataBaseAgentBehaviour extends CyclicBehaviour {
    
    private Logger logger;

    public BootDataBaseAgentBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "BootDataBaseAgentBehaviour run");
        runDataBaseAgent();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {            
            String sender = msg.getSender().getLocalName();
            if (sender.startsWith("DataBaseAgent")) {
                if(msg.getConversationId().equals(DataBaseMySQLBehaviour.DATA_BASE_AGENT_READY)) {
                    ((BootAgent) myAgent).runRoomsAgent();
                    myAgent.removeBehaviour(this);
                }
            }
        } else {
            block();
        }
    }
    
    private void runDataBaseAgent() {
        AgentController dbAgent = null;
        try {
            dbAgent = myAgent.getContainerController().createNewAgent("DataBaseAgent", "agents.DataBaseAgent", null);
            dbAgent.start();
        } catch (StaleProxyException ex) {
            logger.log(Level.WARNING, "BootDataBaseAgentBehaviour.runDataAgent()", ex);
        }
    }
}
