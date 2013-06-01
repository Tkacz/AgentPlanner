/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import behaviours.ScheduleInitBehaviour;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Rafal Tkaczyk
 */
public class ScheduleAgent extends Agent {
    
    private Logger logger;

    @Override
    protected void setup() {
        super.setup();
        initLogger();
        logger.log(Level.INFO, "ScheduleAgent run");
        addBehaviour(new ScheduleInitBehaviour(this, logger));
    }
    
    private void initLogger() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());
        FileHandler fileHandler = null;
        try {
             fileHandler = new FileHandler("log/" + dateStr + "_" + getLocalName() + ".log", true);
             fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException | SecurityException ex) {
            System.err.println(ex.toString());
        }
        logger = jade.util.Logger.getMyLogger(this.getClass().getName());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);// zapis tylko do pliku, coby ekranu nie śmiecić
    }
    
    public void stopSystem() {// public, żeby mogły korzystać zachowania
//        try {
//            getContainerController().kill();
//        }catch(StaleProxyException ex) {
//            System.err.println(ex.toString());
//        }
        
        Codec codec = new SLCodec();
        Ontology jmo = JADEManagementOntology.getInstance();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(jmo);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(getAMS());
        msg.setLanguage(codec.getName());
        msg.setOntology(jmo.getName());
        try {
            getContentManager().fillContent(msg, new Action(getAID(), new ShutdownPlatform()));
            send(msg);
        } catch (CodecException | OntologyException e) {
            System.out.println(e.toString());
        }
    }
}
