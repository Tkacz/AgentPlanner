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
public class TeacherSearchNewPlaceBehaviour extends CyclicBehaviour {
    public static final String SEARCH_NEW_PLACE_TEACHER_PROPOSE = "SEARCH_NEW_PLACE_TEACHER_PROPOSE";
    public static final String SEARCH_NEW_PLACE_TEACHER_ACCEPT_PROPOSAL = "SEARCH_NEW_PLACE_TEACHER_ACCEPT_PROPOSAL";
    public static final String SEARCH_NEW_PLACE_TEACHER_REJECT_PROPOSAL = "SEARCH_NEW_PLACE_TEACHER_REJECT_PROPOSAL";
    
    private ArrayList<Place> allPlaces;// wszystkie miejsca w planie dla danej grupy, posortowane wg oceny
    private final Group actualGroup;// aktualnie brana pod uwagę grupa, dla której szukamy miejsca
    private final int oldDay;
    private final int oldTime;
    private final int oldRoom_no;
    
    private Logger logger;
    private final String teacherSymbol;
    private MessageTemplate msgFilter;
    private final boolean EVALUATE_ALL_PLACES = false;
    
    public TeacherSearchNewPlaceBehaviour(Agent a, Logger logger, String groupSymbol,
            int oldDay, int oldTime, int oldRoom_no) {
        super(a);
        this.actualGroup = ((TeacherAgent) myAgent).getOkGroup(groupSymbol);
        this.oldDay = oldDay;
        this.oldTime = oldTime;
        this.oldRoom_no = oldRoom_no;
        this.allPlaces = new ArrayList<>();
        
        if(EVALUATE_ALL_PLACES) {
            this.allPlaces.addAll(((TeacherAgent) myAgent).evaluateAllPlacesForGroup(actualGroup));
        } else {
            this.allPlaces.addAll(((TeacherAgent) myAgent).evaluatePlacesForGroup(actualGroup));
        }
        
        this.logger = logger;
        this.teacherSymbol = ((TeacherAgent) myAgent).getSymbol();
        logger.log(Level.INFO, ("TeacherSearchPlaceBehaviour for " + teacherSymbol + " run"));
        initMessageTemplate();
        sendProposal();
    }
    
    private void initMessageTemplate() {
        MessageTemplate mt1 = MessageTemplate.MatchConversationId(ScheduleSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_ACCEPT_PROPOSAL);
        MessageTemplate mt2 = MessageTemplate.MatchConversationId(ScheduleSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_REJECT_PROPOSAL);
        
        this.msgFilter = MessageTemplate.or(mt1, mt2);
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
                case ScheduleSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_REJECT_PROPOSAL:
                    rejectProposal();
                    break;
                case ScheduleSearchNewPlaceBehaviour.SEARCH_NEW_PLACE_ACCEPT_PROPOSAL:
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
    
    /**
     * Wysłanie do sędziego propozycji miejsca, które wykładowca chce zająć dla danej grupy.
     */
    private void sendProposal() {
        if (allPlaces.isEmpty()) {            
            logger.log(Level.INFO, (teacherSymbol + ": search new place, allPlaces is empty"));
            sendRejectProposal();
            myAgent.removeBehaviour(this);
            return;
        }
        
        Place place = allPlaces.get(0);
        if(place.getDAY() == this.oldDay && place.getTIME() == this.oldTime && place.getROOM_NO() == this.oldRoom_no) {
            sendProposal();
            return;
        }
        
        ProposedPlace proposition = new ProposedPlace(teacherSymbol, actualGroup.getGroupSymbol(),
                place.getDAY(), place.getTIME(), place.getROOM_NO(), place.getEvaluation(),
                actualGroup.getSubPriority(), actualGroup.getGroupStud_no());
        
        logger.log(Level.INFO, (teacherSymbol + ": SEARCH NEW PLACE SEND NEXT PROPOSAL: " + proposition.toString()));
        
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(SEARCH_NEW_PLACE_TEACHER_PROPOSE);
        try {
            msg.setContentObject(proposition);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "TeacherNegotiationBehaviour.sendProposal()", ex);
        }
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
        logger.log(Level.INFO, (teacherSymbol + ": SEARCH NEW PLACE ACCEPT PROPOSAL: (" + place.getDAY()
                + " : " + place.getTIME() + " : " + place.getROOM_NO() + " : " + actualGroup.getGroupSymbol() +")"));
        ((TeacherAgent) myAgent).setPlaceFree(oldDay, oldTime, oldRoom_no);// zwalnianie miejsca
        ((TeacherAgent) myAgent).takePlace(place.getDAY(), place.getTIME(),
                place.getROOM_NO(), actualGroup);// zajęcie nowego miejsca
        
        sendAcceptProposal();// wyślij info o przyjęciu propozycji
        myAgent.removeBehaviour(this);
    }
    
    /**
     * Odrzucona propozycja, wysyłam kolejną,
     * a jak nie ma innych opcji to wrzucam do nieudanych i wysyłam info do sędziego (ScheduleAgent)
     */
    private void rejectProposal() {
        Place place = allPlaces.get(0);
        logger.log(Level.INFO, (teacherSymbol + ": SEARCH NEW PLACE REJECT PROPOSAL: (" + place.getDAY()
                + " : " + place.getTIME() + " : " + place.getROOM_NO() + " : " + actualGroup.getGroupSymbol() +")"));
        this.allPlaces.remove(0);
        sendProposal();// i wysłanie do sędziego następnego
    }
    
    private void sendAcceptProposal() {
        ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(SEARCH_NEW_PLACE_TEACHER_ACCEPT_PROPOSAL);
        myAgent.send(msg);
    }
    
    private void sendRejectProposal() {
        ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
        msg.addReceiver(new AID("ScheduleAgent", AID.ISLOCALNAME));
        msg.setConversationId(SEARCH_NEW_PLACE_TEACHER_REJECT_PROPOSAL);
        myAgent.send(msg);
    }
}
