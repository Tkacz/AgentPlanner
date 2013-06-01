/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.TeacherAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import agentsElements.Place;
import agentsElements.Group;
import agentsElements.ProposedPlace;
import jade.core.AID;
import jade.util.Logger;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Rafal Tkaczyk
 */
public class TeacherNegotiationBehaviour extends CyclicBehaviour {
    
    public static final String TEACHER_READY_FOR_NEGOTIATION = "TEACHER_READY_FOR_NEGOTIATION";
    public static final String TEACHER_PROPOSE = "TEACHER_PROPOSE";
    public static final String TEACHER_END_OF_NEGOTIATION = "TEACHER_END_OF_NEGOTIATION";
    
    private ArrayList<Place> allPlaces;// wszystkie miejsca w planie dla danej grupy, posortowane wg oceny
    private Group actualGroup;// aktualnie brana pod uwagę grupa, dla której szukamy miejsca

    private Logger logger;
    
    public TeacherNegotiationBehaviour(Agent a, Logger logger) {
        super(a);
        this.allPlaces = new ArrayList<>();
        this.logger = logger;
        logger.log(Level.INFO, "TeacherNegotiationBehaviour run");
        ((TeacherAgent) myAgent).foo();
        sendInformReadyForNegotiation();// powiadomienie o gotowości do negocjacji
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
                case ScheduleNegotiationBehaviour.NEGOTIATION_START_NEXT_PART:
                    negotiationNextPart();
                    break;
                case ScheduleNegotiationBehaviour.ACCEPT_PROPOSAL:
                    acceptProposal();
                    break;
                case ScheduleNegotiationBehaviour.REJECT_PROPOSAL:
                    rejectProposal();
                    break;
                default:
                    notUnderstandMessage(msg.getSender().getLocalName(), msg.getConversationId());
                    break;
            }
        } else {
            block();
        }
    }
    
    private void sendInformReadyForNegotiation() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(TEACHER_READY_FOR_NEGOTIATION);
        msg.setContent(((TeacherAgent) myAgent).getSymbol());
        myAgent.send(msg);
    }
    
    private void negotiationNextPart() {
        this.actualGroup = ((TeacherAgent) myAgent).getNextGroup();
        
        if(this.actualGroup == null) {// wszystkie grupy mają już miejsce
            allGroupsHavePlace();// koniec poszukiwań
        } else {
            placesEvaluation();
        }
    }

    private void placesEvaluation() {
        this.allPlaces.clear();
        
        // dodawanie, ocena i sortowanie wszystkich miejsc dla tej grupy
        this.allPlaces.addAll(((TeacherAgent) myAgent).evaluatePlacesForGroup(actualGroup));

        // sortowanie po ocenie (od najlepszego, czyli malejąco)
        Place.sortArrayListByEvaluation(this.allPlaces);

        sendProposal();
    }
    
    /**
     * Wysłanie do sędziego propozycji miejsca, które wykładowca chce zająć dla danej grupy.
     */
    private void sendProposal() {
        if (allPlaces.isEmpty()) {            
            ((TeacherAgent) myAgent).addToFailedGroups(actualGroup);
            sendSetGroupAsReject();// zapis w bazie danych o niepowodzeniu znalezienia miejsca dla grupy
            negotiationNextPart();// nie udało się znaleźć miejsca dla tej grupy, branie pod uwagę kolejnej grupy
            return;
        }
        
        Place place = allPlaces.get(0);
        ProposedPlace proposition = new ProposedPlace(
                ((TeacherAgent) myAgent).getSymbol(), actualGroup.getGroupSymbol(),
                place.getDAY(), place.getTIME(), place.getROOM_NO(), place.getEvaluation(),
                actualGroup.getSubPriority(), actualGroup.getGroupStud_no());
        
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(TEACHER_PROPOSE);
        try {
            msg.setContentObject(proposition);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "TeacherNegotiationBehaviour.sendProposal()", ex);
        }
    }
    
    private void sendSetGroupAsReject() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.SET_GROUP_AS_REJECT);
        try {
            msg.setContentObject(new Object[]{actualGroup.getGroupSymbol(), ((TeacherAgent) myAgent).getSymbol()});
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "SET GROUP AS REJECT", ex);
        }
    }

    private void notUnderstandMessage(String sender, String title) {
        String log = "TeacherNegotiationBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    /**
     * Wysłanie info o zakończeniu negocjacji.
     */
    private void allGroupsHavePlace() {
        ((TeacherAgent) myAgent).printFailedGroups();
        
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(TEACHER_END_OF_NEGOTIATION);
        msg.setContent(((TeacherAgent) myAgent).getSymbol());
        myAgent.send(msg);
        
        myAgent.removeBehaviour(this);
    }
    
    /**
     * Propozycja została przyjęta, miejsce dla aktualnej grupy zostało zajęte.
     */
    private void acceptProposal() {
//        System.out.println(myAgent.getLocalName() + " : ACCEPT : " + actualGroup.getGroupSymbol());
        Place place = allPlaces.get(0);
        ((TeacherAgent) myAgent).takePlace(place.getDAY(), place.getTIME(), place.getROOM_NO(),
                actualGroup.getGroupSymbol());
    }
    
    /**
     * Odrzucona propozycja, wysyłam kolejną,
     * a jak nie ma innych opcji to wrzucam do nieudanych i wysyłam info do sędziego (ScheduleAgent)
     */
    private void rejectProposal() {
        this.allPlaces.remove(0);
        sendProposal();// i wysłanie do sędziego
    }
}
