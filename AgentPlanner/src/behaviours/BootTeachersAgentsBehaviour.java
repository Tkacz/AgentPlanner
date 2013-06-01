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
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class BootTeachersAgentsBehaviour extends CyclicBehaviour {
    
    private Logger logger;

    public BootTeachersAgentsBehaviour(Agent a, Logger logger) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "BootTeachersAgentsBehaviour run");
        sendAskForTeachersSymbols();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            String sender = msg.getSender().getLocalName();
            
            if(sender.equals("DataBaseAgent")) {
                switch (msg.getConversationId()) {
                    case DataBaseMySQLBehaviour.GET_TEACHERS_SYMBOLS: // odebrano dane potrzebne do utworzenia agenta
                        try {
                            getTeachersSymbolsRet((ArrayList<Object>) msg.getContentObject());
                        } catch (UnreadableException ex) {
                            logger.log(Level.WARNING, "BootTeachersAgentsBehaviour.action()", ex);
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
    
    private void sendAskForTeachersSymbols() {
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.GET_TEACHERS_SYMBOLS);
        myAgent.send(msg);
    }
    
    /**
     * Odbiera zapytanie o dane wykładowców, dzieli je i tworzy nowych agentów
     * @param data Wynik zapytania o dane wykładowców
     */
    private void getTeachersSymbolsRet(ArrayList<Object> data) {
        int size = data.size();
        for(int i = 0; i < size; i++) {
            runTeacherAgent((String) data.get(i));
        }
        myAgent.removeBehaviour(this);
    }
    
    private void runTeacherAgent(String symbol) {
        AgentController teacherAgent;
        try {
            String agentName = "Teacher" + symbol;
            teacherAgent = myAgent.getContainerController().createNewAgent(agentName, "agents.TeacherAgent", null);
            teacherAgent.start();
        } catch (StaleProxyException ex) {
            logger.log(Level.WARNING, "BootTeachersAgentsBehaviour.runTeacherAgent()", ex);
        }
    }
    
    private void notUnderstandMessage(String sender, String title) {
        String log = "BootTeachersAgentsBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
}
