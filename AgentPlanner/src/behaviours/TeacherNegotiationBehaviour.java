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
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
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
    
    private final ArrayList<Place> allPlaces;// wszystkie miejsca w planie dla danej grupy, posortowane wg oceny
    private Group actualGroup;// aktualnie brana pod uwagę grupa, dla której szukamy miejsca

    private final Logger logger;
    private final String teacherSymbol;
    private MessageTemplate msgFilter;
    private final boolean REJECT_GROUP_NEGOTIATION = true;
    private final boolean EVALUATE_ALL_PLACES = false;
    
    public TeacherNegotiationBehaviour(Agent a, Logger logger) {
        super(a);
        this.allPlaces = new ArrayList<>();
        this.logger = logger;
        logger.log(Level.INFO, "TeacherNegotiationBehaviour run");
        this.teacherSymbol = ((TeacherAgent) myAgent).getSymbol();
        initMessageTemplate();
        sendInformReadyForNegotiation();// powiadomienie o gotowości do negocjacji
    }
    
    private void initMessageTemplate() {
        MessageTemplate mt1 = MessageTemplate.MatchConversationId(ScheduleNegotiationBehaviour.NEGOTIATION_START_NEXT_PART);
        MessageTemplate mt2 = MessageTemplate.MatchConversationId(ScheduleNegotiationBehaviour.ACCEPT_PROPOSAL);
        MessageTemplate mt3 = MessageTemplate.MatchConversationId(ScheduleNegotiationBehaviour.REJECT_PROPOSAL);
        MessageTemplate mt4 = MessageTemplate.MatchConversationId(ScheduleReorganizationBehaviour.REORGANIZATION_SEARCH_NEW_PLACE);
        
        this.msgFilter = MessageTemplate.or(MessageTemplate.or(mt1, mt2), MessageTemplate.or(mt3, mt4));
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgFilter);
        if (msg != null) {
            if (msg.getConversationId() == null) {
                logger.log(Level.WARNING, (teacherSymbol + ": ConversationId == null : sender: "
                        + msg.getSender().getLocalName() + "; content: " + msg.getContent()));
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
                case ScheduleReorganizationBehaviour.REORGANIZATION_SEARCH_NEW_PLACE:
                    try {
                        searchNewPlaceNegotiationBeginning((Object[]) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "SEARCH NEW PLACE NEGOTIATION BEGINNING", ex);
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
    
    private void sendInformReadyForNegotiation() {
        logger.log(Level.INFO, (teacherSymbol + (teacherSymbol + ": READY FOR NEGOTIATION")));
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(TEACHER_READY_FOR_NEGOTIATION);
        msg.setContent(((TeacherAgent) myAgent).getSymbol());
        myAgent.send(msg);
    }
    
    private void negotiationNextPart() {
        logger.log(Level.INFO, (teacherSymbol + ": NEXT PART OF NEGOTIATION"));
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
        if(EVALUATE_ALL_PLACES) {
            this.allPlaces.addAll(((TeacherAgent) myAgent).evaluateAllPlacesForGroup(actualGroup));
        } else {
            this.allPlaces.addAll(((TeacherAgent) myAgent).evaluatePlacesForGroup(actualGroup));
        }

        // sortowanie po ocenie (od najlepszego, czyli malejąco)
        Place.sortArrayListByEvaluation(this.allPlaces);
        
        sendProposal();
    }
    
    /**
     * Wysłanie do sędziego propozycji miejsca, które wykładowca chce zająć dla danej grupy.
     */
    private void sendProposal() {
        if (allPlaces.isEmpty()) {            
            logger.log(Level.INFO, (teacherSymbol + ": list allPlaces is empty"));
            ((TeacherAgent) myAgent).addToFailedGroups(actualGroup);
            if (REJECT_GROUP_NEGOTIATION) {// czy reorganizacja planu
                addRejectGroupBehaviour();
            } else {
                sendSetGroupAsReject();// zapis w bazie danych o niepowodzeniu znalezienia miejsca dla grupy
                negotiationNextPart();// nie udało się znaleźć miejsca dla tej grupy, branie pod uwagę kolejnej grupy
            }
            return;
        }
        
        Place place = allPlaces.get(0);
        ProposedPlace proposition = new ProposedPlace(
                ((TeacherAgent) myAgent).getSymbol(), actualGroup.getGroupSymbol(),
                place.getDAY(), place.getTIME(), place.getROOM_NO(), place.getEvaluation(),
                actualGroup.getSubPriority(), actualGroup.getGroupStud_no());
        
        logger.log(Level.INFO, (teacherSymbol + ": SEND NEXT PROPOSAL: " + proposition.toString()));
        
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
    
    private void addRejectGroupBehaviour() {
        logger.log(Level.INFO, (teacherSymbol + ": REJECT GROUP BEHAVIOUR"));
        myAgent.addBehaviour(new TeacherRejectGroupBehaviour(myAgent, logger, actualGroup));
    }

    private void notUnderstandMessage(String sender, String title) {
        logger.log(Level.WARNING, (teacherSymbol + ": TeacherNegotiationBehaviour.notUnderstand(): Sender: "
                + sender + " Title of msg: " + title));
    }
    
    /**
     * Wysłanie info o zakończeniu negocjacji.
     */
    private void allGroupsHavePlace() {
        logger.log(Level.INFO, (teacherSymbol + ": END OF NEGOTIATION"));
        ((TeacherAgent) myAgent).printFailedGroups();
        
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(TEACHER_END_OF_NEGOTIATION);
        msg.setContent(((TeacherAgent) myAgent).getSymbol());
        myAgent.send(msg);
    }
    
    /**
     * Propozycja została przyjęta, miejsce dla aktualnej grupy zostało zajęte.
     */
    private void acceptProposal() {
        Place place = allPlaces.get(0);
        if(actualGroup == null) {
            System.err.println(teacherSymbol);
        }
        logger.log(Level.INFO, (teacherSymbol + ": ACCEPT PROPOSAL: (" + place.getDAY()
                + " : " + place.getTIME() + " : " + place.getROOM_NO() + " : " + actualGroup.getGroupSymbol() +")"));
        ((TeacherAgent) myAgent).takePlace(place.getDAY(), place.getTIME(), place.getROOM_NO(), actualGroup);
    }
    
    /**
     * Odrzucona propozycja, wysyłam kolejną,
     * a jak nie ma innych opcji to wrzucam do nieudanych i wysyłam info do sędziego (ScheduleAgent)
     */
    private void rejectProposal() {
        Place place = allPlaces.get(0);
        logger.log(Level.INFO, (teacherSymbol + ": REJECT PROPOSAL: (" + place.getDAY()
                + " : " + place.getTIME() + " : " + place.getROOM_NO() + " : " + actualGroup.getGroupSymbol() +")"));
        this.allPlaces.remove(0);
        sendProposal();// i wysłanie do sędziego następnego
    }
    
    private void searchNewPlaceNegotiationBeginning(Object data[]) {
        logger.log(Level.INFO, (teacherSymbol + ": search new place negotiation beginning"));
        myAgent.addBehaviour(new TeacherSearchNewPlaceBehaviour(myAgent, logger, (String) data[0],
                (Integer) data[1], (Integer) data[2], (Integer) data[3]));
    }
    
    private void sendSetGroupAsReject() {
        logger.log(Level.INFO, (teacherSymbol + ": SET GROUP " + actualGroup.getGroupSymbol() + " AS REJECT"));
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
}
