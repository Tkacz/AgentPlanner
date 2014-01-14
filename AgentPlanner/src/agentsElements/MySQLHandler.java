/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

import jade.util.Logger;
import jade.util.leap.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Rafal Tkaczyk
 */
public class MySQLHandler {
    
    private static Connection con;
    private final String sql_connect;
    private final String sql_driver;
    private static Statement st;
    private final String sql_login;
    private final String sql_password;
    private final Logger logger;
    private ResultSet rs;
    
    private Properties properties;

    public MySQLHandler(Logger logger) {
        this.logger = logger;
        initProperties();
        con = null;
        
        if (properties.isEmpty()) {
            sql_connect = "jdbc:mysql://localhost/scheduleMFI?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true";
            sql_driver = "com.mysql.jdbc.Driver";
            sql_login = "root";
            sql_password = "pass";
            logger.log(Level.INFO, "Default properties (loading from file failed)");
        } else {
            sql_connect = properties.getProperty("sql_connect");
            sql_driver = properties.getProperty("sql_driver");
            sql_login = properties.getProperty("sql_login");
            sql_password = properties.getProperty("sql_password");
            logger.log(Level.INFO, "Properties loaded from the file");
        }

        if (setDriver()) {
            logger.log(Level.INFO, "Successful conection with DB");
        } else {
            logger.log(Level.WARNING, "Invalid conection with DB");
        }
    }
    
    private void initProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("AgentPlanner.properties"));
        } catch (IOException e) {
            logger.log(Level.WARNING, "MySQLHandlerinitProperties()", e);
        }
    }

    private boolean setDriver() {
        boolean ret = false;
        try {
            Class.forName(sql_driver);
            createStatement();
            return true;
        } catch (ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Driver Exception", ex);
        }
        return ret;
    }

    private boolean open() {
        boolean ret = false;
        try {
            if ((con == null) || con.isClosed()) {
                System.out.println("Laczenie z baza danych");
                con = (Connection) DriverManager.getConnection(sql_connect, sql_login, sql_password);
                System.out.println("Polaczony z baza danych");
            }
            ret = true;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Open Exception", ex);
        }
        return ret;
    }

    public void close() {
        try {
            if (con != null) {
                con.close();
                System.out.println("CLOSE CONNECTION");
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler,close()", ex);
        }
    }

    private void createStatement() {
        if (open()) {
            try {
                if (st == null || st.isClosed()) {
                    st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "createStatement Exception", ex.toString());
            }
        }
    }

    private void select(String comm) {//zapytania
        String log = "Select: " + comm;
        
        if(comm == null) {
            logger.log(Level.WARNING, "MySQLHandler: query == null");
            return;
        }
        
        if(st == null) {
            logger.log(Level.WARNING, "MySQLHandler: Statement == null");
            createStatement();
        }
        
        try {
            while (st.isClosed()) {
                logger.log(Level.WARNING, "MySQLHandler: Statement closed");
                createStatement();
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler: Statement ex: ", ex);
        }
        
        try {
            
            logger.log(Level.INFO, log);
            rs = st.executeQuery(comm);
            rs.beforeFirst();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, comm, ex);
            System.err.println(ex);
        }
    }

    private void execute(String comm) {//dodawanie do tabeli
        String log = "Update: " + comm;
        if(comm == null) {
            logger.log(Level.WARNING, "MySQLHandler: query == null");
            return;
        }
        
        if(st == null) {
            logger.log(Level.WARNING, "MySQLHandler: Statement == null");
            createStatement();
        }
        
        try {
            while (st.isClosed()) {
                logger.log(Level.WARNING, "MySQLHandler: Statement closed");
                createStatement();
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler: Statement ex: ", ex);
        }
        
        try {
            
            logger.log(Level.INFO, log);
            st.execute(comm);
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "executeUpdate Exception", ex);
            System.err.println(ex);
        }
    }
    
    //// PUBLIC METHODS ////
    
    public ArrayList<Object> getTeachersSymbols() {
        String query = "SELECT symbol FROM Teachers;";
        
        select(query);
        ArrayList<Object> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getTeachersSymbol()", ex);
                result = null;
            }
        } else {
            result.add(0);
            result.add(0);
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
        }
        
        return result;
    }
    
    public ArrayList<Object> getDataForTeacherAgent(String symbol) {
        String query = "SELECT symbol, teacher_id, daysPriority, timesPriority, maxHours,"
                + " minHours, maxContHours, maxDayGaps, maxWeekGaps"
                + " FROM Teachers"
                + " WHERE symbol='" + symbol + "';";
        
        select(query);
        ArrayList<Object> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getString(1));
                    result.add(rs.getString(2));
                    result.add(rs.getString(3));
                    result.add(rs.getString(4));
                    result.add(rs.getString(5));
                    result.add(rs.getString(6));
                    result.add(rs.getString(7));
                    result.add(rs.getString(8));
                    result.add(rs.getString(9));
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getDataForTeacherAgent()", ex);
                result = null;
            }
        } else {
            result = null;
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
        }
        
        return result;
    }
    
    public ArrayList<Object> getSubjectDataForTeacher(String symbol) {
        String query = "SELECT g.symbol, g.stud_no, s.symbol, s.time, st.priority"
                + " FROM Teachers t, TeachGroup tg, Groups g, Subjects s, SubjectTypes st"
                + " WHERE t.symbol='" + symbol + "'"
                + " AND t.teacher_id=tg.teacher_id"
                + " AND tg.group_id=g.group_id"
                + " AND g.sub_id=s.sub_id"
                + " AND s.subTyp_id=st.subTyp_id;";
        
        select(query);
        ArrayList<Object> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(new Object[] {
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5)});
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getSubjectDataForTeacher()", ex);
                result = null;
            }
        } else {
            result = null;
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
        }
        
        return result;
    }
    
    /**
     * 
     * @return Numery wszystkich sal i ich pojemności.
     */
    public ArrayList<Integer> getAllRooms() {
        String query = "SELECT no, capacity FROM Rooms ORDER BY no;";
        
        ArrayList<Integer> result = new ArrayList<>();
        select(query);
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getInt(1));// no
                    result.add(rs.getInt(2));// capacity
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getAllRooms()", ex);
                result = null;
            }
        } else {
            result = null;
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
        }
        
        return result;
    }
    
    public ArrayList<Object> getEquipmentOfRooms() {
        String query = "SELECT r.no, e.symbol"
                + " FROM Rooms r INNER JOIN RoomEquip re ON r.room_id=re.room_id"
                + " INNER JOIN Equipment e ON re.equip_id=e.equip_id"
                + " ORDER BY r.no;";
        
        ArrayList<Object> result = new ArrayList<>();
        select(query);
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getInt(1));// no of room
                    result.add(rs.getString(2));// symbol of equipment
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getEquipmentOfRRooms()", ex);
                result = null;
            }
        } else {
            result = null;
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getEquipmentOfRRooms()", ex);
        }
        
        return result;
    }
    
    public ArrayList<String> getEquipmentOfSubjects() {
        String query = "SELECT s.symbol, e.symbol"
                + " FROM Equipment e INNER JOIN SubEquip se ON e.equip_id=se.equip_id"
                + " INNER JOIN Subjects s ON se.sub_id=s.sub_id"
                + " ORDER BY s.symbol;";
        
        ArrayList<String> result = new ArrayList<>();
        select(query);
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getString(1));// symbol of subject
                    result.add(rs.getString(2));// symbol of equipment
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getEquipmentOfRRooms()", ex);
                result = null;
            }
        } else {
            result = null;
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getEquipmentOfRRooms()", ex);
        }
        
        return result;
    }
    
    public ArrayList<String> getAllSymbolsOfSubjects() {
        String query = "SELECT symbol FROM Subjects;";
        
        ArrayList<String> result = new ArrayList<>();
        select(query);
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getString(1));// symbol of subject
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getEquipmentOfRRooms()", ex);
                result = null;
            }
        } else {
            result = null;
        }
        
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getEquipmentOfRRooms()", ex);
        }
        
        return result;
    }
    
    /**
     * Get data to init (Schedule) schedule.
     * @return List: result.get(0) == DAYS; result.get(1) == TIMES; result.get(2) == list of all room numbers. 
     */
    public ArrayList<Object> getScheduleData() {
        ArrayList<Object> result = new ArrayList<>();
        result.add(this.getDays());
        result.add(this.getTimes());
        result.add(this.getAllRoomNumbers());
        return result;
    }
    
    /**
     * Get property from 'Properties' table, key='days'. 
     * @return DAYS
     */
    private Integer getDays() {
        String query = "SELECT val FROM Properties WHERE prop_key='days';";
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getDays()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    /**
     * Get property from 'Properties' table, key='times'.
     * @return TIMES
     */
    private Integer getTimes() {
        String query = "SELECT val FROM Properties WHERE prop_key='times';";
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getTimes()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    private ArrayList<Integer> getAllRoomNumbers() {
        String query = "SELECT no FROM Rooms;";
        ArrayList<Integer> result = new ArrayList<>();
        select(query);
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getAllRoomNumbers()", ex);
                return null;
            }
            
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
            }
            return result;
        } else {
            return null;
        }
    }
    
    public Integer getTotalTeachersNumber() {
        String query = "SELECT count(*) FROM Teachers;";
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getTotalTeachersNumber()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    public ArrayList<Integer> getStudentIdsOfGroup(String group) {
        String query = "SELECT s.stud_id FROM Groups g, StudGroup sg, Students s"
                + " WHERE g.group_id=sg.group_id AND sg.stud_id=s.stud_id"
                + " AND g.symbol='" + group  +"';";
        ArrayList<Integer> result = new ArrayList<>();
        select(query);
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getAllRoomNumbers()", ex);
                return null;
            }
            
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
            }
            return result;
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @param group Symbol grupy.
     * @return Ilość studentów uczęszczających w danej grupie.
     */
    public Integer getStudentsNumberOfGroup(String group) {
        String query = "SELECT count(*)"
                + " FROM Students s INNER JOIN StudGroup sg ON s.stud_id=sg.stud_id"
                + " INNER JOIN Groups g ON sg.group_id=g.group_id"
                + " WHERE g.symbol='" + group + "';";
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getStudentsNumberOfGroup()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    public Integer getNegotiationLevel() {
        String query = "SELECT val FROM Properties WHERE prop_key='negotiation_level';";
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getNegotiationLevel()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    /**
     * Sprawdza ilu studentów, powiązanych z daną grupą ma w tym czasie kolizję.
     * @param group Symbol grupy.
     * @param day Dzień.
     * @param time Czas.
     * @param studIds
     * @return Liczba studentów, którzy mają kolizję.
     */
    public Integer getCollisionStudentsNumber(String group, int day, int time, ArrayList<Integer> studIds) {        
        String query = "SELECT DISTINCT count(s.stud_id)"
                + " FROM Students s INNER JOIN StudGroup sg ON s.stud_id=sg.stud_id"
                + " INNER JOIN Groups g ON sg.group_id=g.group_id"
                + " INNER JOIN Plan p ON g.group_id=p.group_id"
                + " WHERE g.symbol<>'" + group + "'"
                + " AND p.day=" + day + " AND p.time=" + time;

        int size = studIds.size();
        if (size == 0) {
            return 0;
        } else if(size == 1) {
            query += " AND s.stud_id=" + studIds.get(0) + ";";
        } else {
            query += " AND ( s.stud_id=" + studIds.get(0);
            for (int i = 1; i < size; i++) {
                query += " OR s.stud_id=" + studIds.get(i);
            }
            query += " OR s.stud_id=" + studIds.get(size-1) + ");";
        }
        
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getTotalTeachersNumber()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    /**
     * Sprawdza ilu studentów, powiązanych z daną grupą ma w tym czasie kolizję.
     * @param group Symbol grupy.
     * @param group2 Symbol grupy, której dodatkowo nie bierzemy pod uwagę (przydatnie przy sprawdzaniu kolizji przy zamianie grup)
     * @param day Dzień.
     * @param time Czas.
     * @param studIds
     * @return Liczba studentów, którzy mają kolizję.
     */
    public Integer getCollisionStudentsNumber(String group, String group2, int day, int time, ArrayList<Integer> studIds) {        
        String query = "SELECT DISTINCT count(s.stud_id)"
                + " FROM Students s INNER JOIN StudGroup sg ON s.stud_id=sg.stud_id"
                + " INNER JOIN Groups g ON sg.group_id=g.group_id"
                + " INNER JOIN Plan p ON g.group_id=p.group_id"
                + " WHERE g.symbol<>'" + group + "' AND g.symbol<>'" + group2 +"'"
                + " AND p.day=" + day + " AND p.time=" + time;

        int size = studIds.size();
        if (size == 0) {
            return 0;
        } else if(size == 1) {
            query += " AND s.stud_id=" + studIds.get(0) + ";";
        } else {
            query += " AND ( s.stud_id=" + studIds.get(0);
            for (int i = 1; i < size; i++) {
                query += " OR s.stud_id=" + studIds.get(i);
            }
            query += " OR s.stud_id=" + studIds.get(size-1) + ");";
        }
        
        select(query);
        if (rs != null) {
            try {
                rs.next();
                int result = rs.getInt(1);
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return result;
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getTotalTeachersNumber()", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    public boolean isPlaceFree(int day, int time, int room_no) {
        String query = "SELECT count(*) FROM Plan"
                + " WHERE day=" + day
                + " AND time=" + time
                + " AND room_no=" + room_no + ";";
        
        select(query);
        try {
            rs.next();
            if(0 == rs.getInt(1)) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "MySQLHandler.getRoomsForGroup()", ex);
                }
                return true;
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getTotalTeachersNumber()", ex);
            return false;
        }
        return false;
    }
    
    public String[] getRoomer(int day, int time, int room_no) {
        String query = "SELECT t.symbol, g.symbol"
                + " FROM Teachers t INNER JOIN TeachGroup tg ON t.teacher_id=tg.teacher_id"
                + " INNER JOIN Groups g ON tg.group_id=g.group_id"
                + " INNER JOIN Plan p ON g.group_id=p.group_id"
                + " WHERE p.day=" + day + " AND p.time=" + time + " AND p.room_no=" + room_no + ";";
        
        select(query);
        try {
            rs.next();
            String result[] = new String[2];
            result[0] = rs.getString(1);
            result[1] = rs.getString(2);
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "MySQLHandler.getRoomGroup()", ex);
            }
            return result;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "MySQLHandler.getTotalTeachersNumber()", ex);
            return null;
        }
    }
    
    public void insertGroupIntoPlan(String group, int day, int time, int room_no) {
        String insert = "INSERT INTO Plan (`group_id`, `day`, `time`, `room_no`)"
                + " VALUES ((SELECT group_id FROM Groups WHERE symbol='" + group + "'),"
                + day + ", " + time + ", " + room_no + ");";
        execute(insert);
    }
    
    public void removeGroupFromPlan(String group) {
        String delete = "DELETE p FROM Plan p, Groups g"
                + " WHERE g.group_id=p.group_id AND g.symbol='" + group + "';";
        execute(delete);
    }
    
    public void setGroupAsReject(String group, String teacher) {        
        String insert = "INSERT INTO RejectGroups (`group_id`, `teacher_id`)"
                + " VALUES ((SELECT group_id FROM Groups WHERE symbol='" + group + "'),"
                + " (SELECT teacher_id FROM Teachers WHERE symbol='" + teacher + "'));";
        
        execute(insert);
    }
}
