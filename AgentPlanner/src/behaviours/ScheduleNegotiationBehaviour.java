/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.ScheduleAgent;
import agentsElements.Place;
import agentsElements.ProposedPlace;
import agentsElements.MultiRoomerPlace;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class ScheduleNegotiationBehaviour extends CyclicBehaviour {
    
    public static final String NEGOTIATION_START_NEXT_PART = "NEGOTIATION_BEGINNING";
    public static final String REJECT_PROPOSAL = "REJECT_PROPOSAL";
    public static final String ACCEPT_PROPOSAL = "ACCEPT_PROPOSAL";
    
    private Logger logger;
    private int totalTeachersNo;
    private ArrayList<String> allTeachers;

    private HashMap<String, ProposedPlace> actualPropositions;// key: symbol wykładowcy, val: miejsce proponowane przez danego wykładowcę (do zajęcia prze niego)
    private ArrayList<String> rejectPropositions;// lista symboli wykładowców odrzuconych propozycji
    
    private ArrayList<MultiRoomerPlace> proposalsInPlan;
    
    public ScheduleNegotiationBehaviour(Agent a, Logger logger, int totalTeachersNumber) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "ScheduleNegotiationBehaviour run");
        this.allTeachers = new ArrayList<>();
        this.actualPropositions = new HashMap<>();
        this.rejectPropositions = new ArrayList<>();
        this.proposalsInPlan = new ArrayList<>();
        this.totalTeachersNo = totalTeachersNumber;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            if(msg.getConversationId() == null) {
                String log = "ConversationId == null : sender: " + msg.getSender().getLocalName()
                        + "; content: " + msg.getContent();
                logger.log(Level.WARNING, log);
                return;
            }
            switch (msg.getConversationId()) {
                case TeacherNegotiationBehaviour.TEACHER_READY_FOR_NEGOTIATION:
                    teacherReadyForNegotiation(msg.getContent());
                    break;
                case TeacherNegotiationBehaviour.TEACHER_PROPOSE:
                    try {
                        teacherPropose((ProposedPlace) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.action()", ex);
                    }
                    break;
                case TeacherNegotiationBehaviour.TEACHER_END_OF_NEGOTIATION:
                    teacherEndOfNegotiation(msg.getContent());
                    break;
                case DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS:
                    try {
                        checkCollisionsOfStudentsRet((ArrayList<Object>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.action()", ex);
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
        String log = "ScheduleNegotiationBehaviour.notUnderstand(): Sender: " + sender
                + " Title of msg: " + title;
        logger.log(Level.WARNING, log);
    }
    
    private void teacherReadyForNegotiation(String teachersSymbol) {
        this.allTeachers.add(teachersSymbol);
        if(allTeachers.size() == totalTeachersNo) {// już wszyscy agenci wykładowców są gotowi
            killRoomsAgent();
            informTeachersAboutNegotiatonBeginning();
        }
    }
    
    private void killRoomsAgent() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("RoomsAgent", AID.ISLOCALNAME));
        msg.setConversationId(RoomsInformationBehaviour.KILL_ROOMS_AGENT);
        myAgent.send(msg);
    }
    
    private void informTeachersAboutNegotiatonBeginning() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setConversationId(NEGOTIATION_START_NEXT_PART);
        for(String teacherSymbol : allTeachers) {
            msg.addReceiver(new AID("Teacher" + teacherSymbol, AID.ISLOCALNAME));
        }
        myAgent.send(msg);
    }
    
    private void teacherPropose(ProposedPlace proposition) {
        // wykładowca może jednorazowo wysłać tylko jedną propozycję
        this.actualPropositions.put(proposition.getTEACHER_SYMBOL(), proposition);        
        starPartOfNegotiation();
    }
    
    private void starPartOfNegotiation() {
        if (rejectPropositions.isEmpty() && actualPropositions.size() == totalTeachersNo) {// jeśli wszyscy zgłosili swoje propozycje
            for (String key : actualPropositions.keySet()) {
                System.err.print(actualPropositions.get(key).getGROUP_SYMBOL() + " : ");
            }
            System.out.println();
            checkCollisionsOfStudents();// spr czy są kolizje czasowe ze studentami
        } else if (!rejectPropositions.isEmpty() && rejectPropositions.size() == actualPropositions.size()) {
            checkCollisionsOfStudents();
        }
    }

    private void checkCollisionsOfStudents() {        
        ArrayList<Object> propositions = new ArrayList<>();
        for(String teacher : actualPropositions.keySet()) {// wrzucanie propozycji do ocenienia (sprawdzenia kolizji)
            propositions.add(actualPropositions.get(teacher));
        }
        
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS);
        
        try {
            msg.setContentObject(propositions);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.checkCollisionsOfStudents()", ex);
        }
    }
    
    private void checkCollisionsOfStudentsRet(ArrayList<Object> newActualPropositions) {
        this.rejectPropositions.clear();
        this.actualPropositions.clear();
        
        ProposedPlace place;
        for(Object data : newActualPropositions) {
            place = (ProposedPlace) data;
            if(place.getEvaluation() == 0) {// odrzucono propozycję wykładowcy
                rejectPropositions.add(place.getTEACHER_SYMBOL());
            } else {
                actualPropositions.put(place.getTEACHER_SYMBOL(), place);
            }
        }
        
        negotiation();
    }
    
    private void negotiation() {
        disposeProposalsInPlan();// rozmieszczenie pozostałych propozycji w planie
        putPlacesWithNoCollisions();// wrzucanie jasnych sytuacji do planu (bez kolizji)
        resolveCollisions();// konkursy o miejsca z kolizjami
        
        if(rejectPropositions.isEmpty()) {// koniec tego etapu negocjacji, wszystkie grupy mają już miejsce w planie
            // powiadomienie wykładowców o rozpoczęciu kolejnego etapu negocjacji
            informTeachersAboutNegotiatonBeginning();
        } else {// wysłanie  wykładowcom z listy odrzuconych propozycji
            sendToTeachersRejectProposal();
        }
    }
    
    private void disposeProposalsInPlan() {
        this.proposalsInPlan.clear();
        boolean placeFound;
        for(String teacher : actualPropositions.keySet()) {
            placeFound = false;
            for(MultiRoomerPlace place : proposalsInPlan) {
                if(place.addRoomer(actualPropositions.get(teacher))) {// jeśli to samo miejsce to doda do tj grupy
                    placeFound = true;
                    break;
                }
            }
            if(placeFound == false) {// jeśli nie ma jeszcze takiego zajętego miejsca to jest pierwszym lokatorem i je zajmuje
                this.proposalsInPlan.add(new MultiRoomerPlace(actualPropositions.get(teacher)));
            }
        }
        this.actualPropositions.clear();
    }
    
    /**
     * Najpierw umieszczamy w planie sytuacje klarowne, czyli grupy bez kolizji.
     */
    private void putPlacesWithNoCollisions() {
        // spr już wszystkie kolizje i czy sala o danej porze, w danym dniu jest wolna, można spokojnie wstawić grupę
        ArrayList<Place> takePlace = new ArrayList<>();// miejsca do zajęcia, wysyłane do agenta bazy danych0
        
        for(MultiRoomerPlace place : proposalsInPlan) {
            if(place.getRommersNumber() == 1) {
                takePlace.add(new Place(place.getDAY(), place.getTIME(), place.getROOM_NO(), place.getRoomerAt(0).getGROUP_SYMBOL()));
                sendToTeachersAcceptProposal(place.getRoomerAt(0).getTEACHER_SYMBOL());// powiadomienie agenta wykładowcy o zaakceptowaniu propozycji
            }
        }
        
        sendInsertGroupsIntoPlan(takePlace);// zapis w BD
    }
    
    private void resolveCollisions() {
        ArrayList<Place> takePlace = new ArrayList<>();
        
        for(MultiRoomerPlace place : proposalsInPlan) {
            if(place.getRommersNumber() > 1) {// kolizja, robimy konkurs
                place.sortRoomersByEvaluation();// sortowanie lokatorów po ocenach
                takePlace.add(new Place(place.getDAY(), place.getTIME(), place.getROOM_NO(),
                        place.getRoomerAt(0).getGROUP_SYMBOL()));// dodajemy zwycięzcę do planu
                sendToTeachersAcceptProposal(place.getRoomerAt(0).getTEACHER_SYMBOL());// powiadomienie agenta wykładowcy o zaakceptowaniu propozycji
                for(int i = 1, size = place.getRommersNumber(); i < size; i++) {// wrzucanie przegranych do listy odrzuconych
                    this.rejectPropositions.add(place.getRoomerAt(i).getTEACHER_SYMBOL());
                }
            }
        }
        
        sendInsertGroupsIntoPlan(takePlace);// wstawienie miejsc do planu (do BD)
    }
    
    private void sendInsertGroupsIntoPlan(ArrayList<Place> takePlace) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.INSERT_GROUPS_INTO_PLAN);
        try {
            msg.setContentObject(takePlace);
            myAgent.send(msg);
        } catch(IOException ex) {
            logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.putPlacesWithNoCollisions()", ex);
        }
    }
    
    private void sendToTeachersAcceptProposal(String symbol) {
        ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);        
        msg.setConversationId(ACCEPT_PROPOSAL);
        msg.addReceiver(new AID("Teacher" + symbol, AID.ISLOCALNAME));
        myAgent.send(msg);    
    }
    
    private void sendToTeachersRejectProposal() {
        ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);        
        msg.setConversationId(REJECT_PROPOSAL);
        for(String symbol : rejectPropositions) {
            msg.addReceiver(new AID("Teacher" + symbol, AID.ISLOCALNAME));
        }
        myAgent.send(msg);
    }
    
    private void teacherEndOfNegotiation(String symbol) {
        this.allTeachers.remove(symbol);
        this.totalTeachersNo--;
        if(this.totalTeachersNo == 0) {
            endOfNegotiation();
        } else {
            starPartOfNegotiation();
        }
    }

    private void endOfNegotiation() {
        System.out.println("END OF NEGOTIATION");
        
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Error reading from user");
        }
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.CLOSE_CONNECTION);
        myAgent.send(msg);// wysłanie wiadomości do agenta bazy danych i studentów w celu zamknięcia połączeń z BD
        ((ScheduleAgent) myAgent).stopSystem();// zatrzymanie systemu
    }
}
