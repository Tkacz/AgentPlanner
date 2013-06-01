/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class BootScheduleAgentBehaviour extends OneShotBehaviour {
    
    private Logger logger;

    public BootScheduleAgentBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "BootAgentBootScheduleAgentBehaviour run");
    }
    
    @Override
    public void action() {
        runScheduleAgent();
        myAgent.addBehaviour(new BootTeachersAgentsBehaviour(myAgent, logger));
    }
    
    private void runScheduleAgent() {
        AgentController teacherAgent;
        try {
            teacherAgent = myAgent.getContainerController().createNewAgent("ScheduleAgent",
                    "agents.ScheduleAgent", null);
            teacherAgent.start();
        } catch (StaleProxyException ex) {
            logger.log(Level.WARNING, "BootScheduleAgentBehaviour.runScheduleAgent()", ex);
        }
    }
}
