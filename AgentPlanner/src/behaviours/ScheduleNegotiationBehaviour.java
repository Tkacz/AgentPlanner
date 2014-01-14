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
import jade.lang.acl.MessageTemplate;
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
    
    private final String STATUS_WAIT_FOR_TEACHERS = "STATUS WAIT FOR TEACHERS";
    private final String STATUS_WAIT_FOR_ALL_PROPOSALS = "STATUS_WAIT_FOR_ALL_PROPOSALS";
    private final String STATUS_WAIT_FOR_ALL_REJECT_PROPOSALS = "STATUS_WAIT_FOR_ALL_REJECT_PROPOSALS";
    private final String STATUS_CONSIDER_PROPOSALS = "STATUS_CONSIDER_PROPOSALS";
    private final String STATUS_REJECT_GROUP_NEGOTIATION = "STATUS_REJECT_GROUP_NEGOTIATION";
    private String status = "NO STATUS";
    
    private final Logger logger;
    private int totalTeachersNo;
    private final ArrayList<String> allTeachers;

    private final HashMap<String, ProposedPlace> actualPropositions;// key: symbol wykładowcy, val: miejsce proponowane przez danego wykładowcę (do zajęcia prze niego)
    private final ArrayList<String> rejectPropositions;// lista symboli wykładowców odrzuconych propozycji
    
    private final ArrayList<MultiRoomerPlace> proposalsInPlan;// propozycje rozmieszczone na konkretnych pozycjach (miejscach)
    
    private final ArrayList<String> negotiationOfRejectGroup;// symbol wykładowcy
    private MessageTemplate msgFilter;
    private ScheduleReorganizationBehaviour reorganizationBehaviour;
    
    public ScheduleNegotiationBehaviour(Agent a, Logger logger, int totalTeachersNumber) {
        super(a);
        this.logger = logger;
        logger.log(Level.INFO, "ScheduleNegotiationBehaviour run");
        initMessageTemplate();
        this.allTeachers = new ArrayList<>();
        this.actualPropositions = new HashMap<>();
        this.rejectPropositions = new ArrayList<>();
        this.proposalsInPlan = new ArrayList<>();
        this.negotiationOfRejectGroup = new ArrayList<>();
        this.totalTeachersNo = totalTeachersNumber;
        status = STATUS_WAIT_FOR_TEACHERS;
    }
    
    private void initMessageTemplate() {
        MessageTemplate mt1 = MessageTemplate.MatchConversationId(TeacherNegotiationBehaviour.TEACHER_READY_FOR_NEGOTIATION);
        MessageTemplate mt2 = MessageTemplate.MatchConversationId(TeacherNegotiationBehaviour.TEACHER_PROPOSE);
        MessageTemplate mt3 = MessageTemplate.MatchConversationId(TeacherNegotiationBehaviour.TEACHER_END_OF_NEGOTIATION);
        MessageTemplate mt4 = MessageTemplate.MatchConversationId(DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS);
        MessageTemplate mt5 = MessageTemplate.MatchConversationId(TeacherRejectGroupBehaviour.REJECT_GROUP_START_NEGOTIATION);
        MessageTemplate mt6 = MessageTemplate.MatchConversationId(TeacherRejectGroupBehaviour.REJECT_GROUP_END_OF_NEGOTIATION);
        MessageTemplate mt7 = MessageTemplate.MatchConversationId(DataBaseMySQLBehaviour.CHECK_IS_PLACE_FREE);
        
        this.msgFilter = MessageTemplate.or(MessageTemplate.or(MessageTemplate.or(mt1, mt2),
                MessageTemplate.or(mt3, mt4)), MessageTemplate.or(mt5, MessageTemplate.or(mt6, mt7)));
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgFilter);
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
		case DataBaseMySQLBehaviour.CHECK_IS_PLACE_FREE:
		    try {
                        checkIsPlaceFreeRet((ArrayList<String>) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.action()", ex);
                    }
		    break;
                case DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS:
                    try {
                        checkCollisionsOfStudentsRet((ProposedPlace) msg.getContentObject());
                    } catch (UnreadableException ex) {
                        logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.action()", ex);
                    }
                    break;
                case TeacherRejectGroupBehaviour.REJECT_GROUP_START_NEGOTIATION:
                    teacherNegotiationOfRejectGroup(msg.getContent());
                    break;
                case TeacherRejectGroupBehaviour.REJECT_GROUP_END_OF_NEGOTIATION:
                    rejectGroupEndOfNegotiation(msg.getContent());
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
            status = STATUS_WAIT_FOR_ALL_PROPOSALS;
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
        status = STATUS_WAIT_FOR_ALL_PROPOSALS;
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
        startPartOfNegotiation();
    }
    
    private void startPartOfNegotiation() {
//        if (status.equals(STATUS_WAIT_FOR_ALL_PROPOSALS) && actualPropositions.size() == totalTeachersNo) {// jeśli wszyscy zgłosili swoje propozycje
        if (status.equals(STATUS_WAIT_FOR_ALL_PROPOSALS)
                && actualPropositions.size() == totalTeachersNo-negotiationOfRejectGroup.size()) {// jeśli wszyscy zgłosili swoje propozycje
            checkIsPlaceFree();// sprawdzanie czy miejsca są wolne
        } else if (status.equals(STATUS_WAIT_FOR_ALL_REJECT_PROPOSALS) && rejectPropositions.size() == actualPropositions.size()) {
	    checkIsPlaceFree();// sprawdzanie czy miejsca są wolne
        }
    }
    
    private void checkIsPlaceFree() {
        status = STATUS_CONSIDER_PROPOSALS;
        
        ArrayList<Object> propositions = new ArrayList<>();
        for(String teacher : actualPropositions.keySet()) {// wrzucanie propozycji do ocenienia (sprawdzenia kolizji)
            propositions.add(actualPropositions.get(teacher));
        }
	
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.CHECK_IS_PLACE_FREE);
        
        try {
            msg.setContentObject(propositions);
            myAgent.send(msg);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.checkCollisionsOfStudents()", ex);
        }
    }
    
    private void checkIsPlaceFreeRet(ArrayList<String> teachersSymbols) {
	this.proposalsInPlan.clear();
        boolean placeFound;
        for(String teacher : teachersSymbols) {// rozmieszczanie propozycji
            placeFound = false;
            for(MultiRoomerPlace place : proposalsInPlan) {
                if(place.addRoomer(actualPropositions.get(teacher))) {// jeśli to samo miejsce to doda do tej grupy
                    placeFound = true;
                    break;
                }
            }
            if(placeFound == false) {// jeśli nie ma jeszcze takiego zajętego miejsca to jest pierwszym lokatorem i je zajmuje
                this.proposalsInPlan.add(new MultiRoomerPlace(actualPropositions.get(teacher)));
            }
	    actualPropositions.remove(teacher);
        }
	
	this.rejectPropositions.clear();
	for(String teacher : actualPropositions.keySet()) {// dodanie do listy odrzuconych propozycji, które ubiegają się o zajęte miejsca
	    rejectPropositions.add(teacher);
	}
	
        this.actualPropositions.clear();
        
        negotiation();
    }
    
    private void negotiation() {
	MultiRoomerPlace.sortArrayListByEvaluation(proposalsInPlan);// Posortowanie listy po najlepszych ocenach lokatorów.
	checkCollisionsOfStudents();
    }
    
    private void checkCollisionsOfStudents() {
	if(!proposalsInPlan.isEmpty()) {
	    ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
	    msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
	    msg.setConversationId(DataBaseMySQLBehaviour.CHECK_COLLISIONS_OF_STUDENTS);

	    try {
		msg.setContentObject(proposalsInPlan.get(0).getRoomerAt(0));
		myAgent.send(msg);
	    } catch (IOException ex) {
		logger.log(Level.WARNING, "ScheduleNegotiationBehaviour.checkCollisionsOfStudents()", ex);
	    }
	} else {
	    endOfRound();
	}
    }
    
    private void checkCollisionsOfStudentsRet(ProposedPlace checkedProposition) {
        
	if (checkedProposition.getEvaluation() == 0) {// są kolizje, odrzucono propozycję wykładowcy
	    rejectPropositions.add(checkedProposition.getTEACHER_SYMBOL());
	    proposalsInPlan.get(0).getRoomers().remove(0);
	    
	    if(proposalsInPlan.get(0).getRoomers().isEmpty()) {
		proposalsInPlan.remove(0);
	    }
	} else {// nie ma kolizji
	    sendInsertGroupIntoPlan(new Place(checkedProposition.getDAY(), checkedProposition.getTIME(),
		    checkedProposition.getROOM_NO(), checkedProposition.getGROUP_SYMBOL(), checkedProposition.getEvaluation()));// wstawienie grupy do planu
	    sendToTeachersAcceptProposal(checkedProposition.getTEACHER_SYMBOL());// wysłanie info to agenta wykladowcy, że propozycja zaakceptowana
	    
	    proposalsInPlan.get(0).getRoomers().remove(0);// usunięcie pierwszego lokatora (który został dodany do planu)
	    for(ProposedPlace proposal : proposalsInPlan.get(0).getRoomers()) {// reszta lokatorów (jeśli jest) dodana jest do odrzuconych
		rejectPropositions.add(proposal.getTEACHER_SYMBOL());
	    }
	    proposalsInPlan.remove(0);// usuwanie elementu z poczatku listy
	}
	
	checkCollisionsOfStudents();
    }
    
    private void endOfRound() {
	if (rejectPropositions.isEmpty()) {// koniec tego etapu negocjacji, wszystkie grupy mają już miejsce w planie
	    // powiadomienie wykładowców o rozpoczęciu kolejnego etapu negocjacji
	    if (negotiationOfRejectGroup.size() > 0) {// uruchamiany jest algorytm reorganizacji
		addReorganizationBehaviour();
	    } else {
		informTeachersAboutNegotiatonBeginning();
	    }
	} else {// wysłanie  wykładowcom z listy odrzuconych propozycji
	    sendToTeachersRejectProposal();
	}
    }
    
    private void sendInsertGroupIntoPlan(Place takePlace) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("DataBaseAgent", AID.ISLOCALNAME));
        msg.setConversationId(DataBaseMySQLBehaviour.INSERT_GROUP_INTO_PLAN);
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
        status = STATUS_WAIT_FOR_ALL_REJECT_PROPOSALS;
        ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);        
        msg.setConversationId(REJECT_PROPOSAL);
        for(String symbol : rejectPropositions) {
            msg.addReceiver(new AID("Teacher" + symbol, AID.ISLOCALNAME));
        }
        myAgent.send(msg);
    }
    
    private void teacherNegotiationOfRejectGroup(String symbol) {
        this.negotiationOfRejectGroup.add(symbol);
        if (status.equals(STATUS_WAIT_FOR_ALL_REJECT_PROPOSALS)) {// dodajemy do listy i czekamy na zakończenie aktualnego etapu negocjacji
            this.rejectPropositions.remove(symbol);
        }
        startPartOfNegotiation();
    }
    
    private void addReorganizationBehaviour() {
        this.status = STATUS_REJECT_GROUP_NEGOTIATION;
        this.reorganizationBehaviour = new ScheduleReorganizationBehaviour(myAgent, logger, negotiationOfRejectGroup.get(0));
        myAgent.addBehaviour(this.reorganizationBehaviour);
    }
    
    private void rejectGroupEndOfNegotiation(String teacherSymbol) {
        myAgent.removeBehaviour(this.reorganizationBehaviour);
        this.reorganizationBehaviour = null;
        this.negotiationOfRejectGroup.remove(teacherSymbol);
        if (negotiationOfRejectGroup.size() > 0) {
            addReorganizationBehaviour();
        } else {
            informTeachersAboutNegotiatonBeginning();
        }
    }
    
    private void teacherEndOfNegotiation(String symbol) {
        this.allTeachers.remove(symbol);
        this.totalTeachersNo--;
        this.rejectPropositions.remove(symbol);
        if(this.totalTeachersNo == 0) {
            endOfNegotiation();
        } else {
            startPartOfNegotiation();
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
