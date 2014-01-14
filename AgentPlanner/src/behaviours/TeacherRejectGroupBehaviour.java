/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.TeacherAgent;
import agentsElements.Group;
import agentsElements.Place;
import agentsElements.ProposedPlace;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class TeacherRejectGroupBehaviour extends CyclicBehaviour {
    public static final String REJECT_GROUP_PROPOSE = "REJECT_GROUP_PROPOSE";
    public static final String REJECT_GROUP_END_OF_NEGOTIATION = "REJECT_GROUP_END_OF_NEGOTIATION";
    public static final String REJECT_GROUP_START_NEGOTIATION = "REJECT_GROUP_START_NEGOTIATION";
    
    private ArrayList<Place> allPlaces;// wszystkie miejsca w planie dla danej grupy, posortowane wg oceny
    private final Group actualGroup;// aktualnie brana pod uwagę grupa, dla której szukamy miejsca

    private Logger logger;
    private final String teacherSymbol;
    private MessageTemplate msgFilter;
    private final boolean EVALUATE_ALL_PLACES = false;
    
    public TeacherRejectGroupBehaviour(Agent a, Logger logger, Group group) {
        super(a);
        this.actualGroup = group;
        this.allPlaces = new ArrayList<>();
        
        if(EVALUATE_ALL_PLACES) {
            this.allPlaces.addAll(((TeacherAgent) myAgent).evaluateAllPlacesForGroup(actualGroup));
        } else {
            this.allPlaces.addAll(((TeacherAgent) myAgent).evaluatePlacesForGroup(actualGroup));
        }
//        this.allPlaces.addAll(((TeacherAgent) myAgent).evaluatePlacesForGroup(actualGroup));
        
        this.logger = logger;
        logger.log(Level.INFO, "TeacherRejectGroupBehaviour run");
        this.teacherSymbol = ((TeacherAgent) myAgent).getSymbol();
        initMessageTemplate();
        sendInformReadyForNegotiation();// powiadomienie o gotowości do negocjacji
    }
    
    private void initMessageTemplate() {
        MessageTemplate mt1 = MessageTemplate.MatchConversationId(ScheduleReorganizationBehaviour.REORGANIZATION_BEGINNING);
        MessageTemplate mt2 = MessageTemplate.MatchConversationId(ScheduleReorganizationBehaviour.REORGANIZATION_REJECT_PROPOSAL);
        MessageTemplate mt3 = MessageTemplate.MatchConversationId(ScheduleReorganizationBehaviour.REORGANIZATION_ACCEPT_PROPOSAL);
        
        this.msgFilter = MessageTemplate.or(mt1, MessageTemplate.or(mt2, mt3));
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
                case ScheduleReorganizationBehaviour.REORGANIZATION_BEGINNING:
                    sendProposal();
                    break;
                case ScheduleReorganizationBehaviour.REORGANIZATION_REJECT_PROPOSAL:
                    rejectProposal();
                    break;
                case ScheduleReorganizationBehaviour.REORGANIZATION_ACCEPT_PROPOSAL:
                    acceptProposal();
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
        logger.log(Level.INFO, (teacherSymbol + (teacherSymbol + ": REJECT GROUP READY FOR NEGOTIATION")));
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(REJECT_GROUP_START_NEGOTIATION);
        msg.setContent(teacherSymbol);
        myAgent.send(msg);
    }

    /**
     * Wysłanie do sędziego propozycji miejsca, które wykładowca chce zająć dla danej grupy.
     */
    private void sendProposal() {
        if (allPlaces.isEmpty()) {// nie udało się przeorganizować planu tak aby znaleźć miejsce, grupa odrzucona
            logger.log(Level.INFO, (teacherSymbol + ": allPlaces is empty"));
            ((TeacherAgent) myAgent).addToFailedGroups(actualGroup);
            sendSetGroupAsReject();// zapis w bazie danych o niepowodzeniu znalezienia miejsca dla grupy
            sendInformationEndNegotiationOfRejectGroup();
            return;
        }
        
        Place place = allPlaces.get(0);
        ProposedPlace proposition = new ProposedPlace(
                ((TeacherAgent) myAgent).getSymbol(), actualGroup.getGroupSymbol(),
                place.getDAY(), place.getTIME(), place.getROOM_NO(), place.getEvaluation(),
                actualGroup.getSubPriority(), actualGroup.getGroupStud_no());
        
        logger.log(Level.INFO, (teacherSymbol + ": REJECT GROUP SEND NEXT PROPOSAL: " + proposition.toString()));
        
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(REJECT_GROUP_PROPOSE);
        try {
            msg.setContentObject(proposition);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "TeacherRejectGroupBehaviour.sendProposal()", ex);
        }
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
    
    private void sendInformationEndNegotiationOfRejectGroup() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(REJECT_GROUP_END_OF_NEGOTIATION);
        msg.setContent(teacherSymbol);
        myAgent.send(msg);
        myAgent.removeBehaviour(this);
    }

    private void notUnderstandMessage(String sender, String title) {
        logger.log(Level.WARNING, (teacherSymbol + ": TeacherNegotiationBehaviour.notUnderstand(): Sender: "
                + sender + " Title of msg: " + title));
    }
    
    /**
     * Propozycja została przyjęta, miejsce dla aktualnej grupy zostało zajęte.
     */
    private void acceptProposal() {
        Place place = allPlaces.get(0);
        logger.log(Level.INFO, (teacherSymbol + ": REJECT GROUP - ACCEPT PROPOSAL: (" + place.getDAY()
                + " : " + place.getTIME() + " : " + place.getROOM_NO() + " : " + actualGroup.getGroupSymbol() +")"));
        ((TeacherAgent) myAgent).takePlace(place.getDAY(), place.getTIME(), place.getROOM_NO(), actualGroup);
        sendInformationEndNegotiationOfRejectGroup();
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
}
