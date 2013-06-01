/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import behaviours.DataBaseMySQLBehaviour;
import jade.core.Agent;
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
public class DataBaseAgent extends Agent {
    
    private Logger logger;
    
    @Override
    protected void setup() {
        initLogger();
        logger.log(Level.INFO, "DataBaseAgent run");
        addBehaviour(new DataBaseMySQLBehaviour(this, logger));
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
    
    @Override
    protected void takeDown() {
        super.takeDown();
    }
}
