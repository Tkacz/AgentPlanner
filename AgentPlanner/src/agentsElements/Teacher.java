/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsElements;

/**
 *
 * @author Rafal Tkaczyk
 */
public class Teacher {
    private final String symbol;
    private final String teacher_id;
    private final int daysPriority[];
    private final int timesPriority[][];
    private final int maxHours;
    private final int minHours;
    private final int maxContHours;
    private final int maxDayGaps;
    private final int maxWeekGaps;

    public Teacher(Object data[]) {
        this.symbol = (String) data[0];
        this.teacher_id = (String) data[1];
        this.daysPriority = getDaysPriority((String) data[2]);
        this.timesPriority = getTimesPriority(daysPriority.length, (String) data[3]);
        this.maxHours = Integer.parseInt((String) data[4]);
        this.minHours = Integer.parseInt((String) data[5]);
        this.maxContHours = Integer.parseInt((String) data[6]);
        this.maxDayGaps = Integer.parseInt((String) data[7]);
        this.maxWeekGaps = Integer.parseInt((String) data[8]);
    }
    
    private int[] getDaysPriority(String str) {
        int size = str.length();
        int result[] = new int[size];        
        for(int i = 0; i < size; i++) {
            result[i] = str.charAt(i)-48;
        }
        
        return result;
    }
    
    private int[][] getTimesPriority(int days, String str) {
//        int size = str.length();
        int times = str.length()/days;
        int result[][] = new int[days][times];
        int k = 0;
        for(int i = 0; i < days; i++) {
            for(int j = 0; j < times; j++, k++) {
                result[i][j] = str.charAt(k)-48;
            }
        }
        
        return result;
    }

    public int[] getDaysPriority() {
        return daysPriority;
    }
    
    public int getDaysPriorityAt(int index) {
        return daysPriority[index];
    }

    public int getMaxContHours() {
        return maxContHours;
    }

    public int getMaxDayGaps() {
        return maxDayGaps;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public int getMaxWeekGaps() {
        return maxWeekGaps;
    }

    public int getMinHours() {
        return minHours;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public int getTeacher_idAsInt() {
        return Integer.parseInt(teacher_id);
    }
    
    public int[][] getTimesPriority() {
        return timesPriority;
    }
    
    public int getTimesPriorityAt(int day, int time) {
        return timesPriority[day][time];
    }

    @Override
    public String toString() {
        String print = "";
        print += "symbol: " + this.symbol + "\n";
        print += "id:" + this.teacher_id + "\n";
        print += "dayPriority:";
        for(int i = 0; i < daysPriority.length; i++) {
            print += " " + daysPriority[i];
        }
        print += "\ntimesPriority: ";
        for(int i = 0; i < daysPriority.length; i++) {
            print += "(";
            for(int j = 0; j < timesPriority[i].length; j++) {
                print += " " + timesPriority[i][j];
            }
            print += ") ";
        }
        print += "\nmaxHours: " + maxHours + "\n";
        print += "minHours: " + minHours + "\n";
        print += "maxContHours: " + maxContHours + "\n";
        print += "maxDayGaps: " + maxDayGaps + "\n";
        print += "maxWeekGaps: " + maxWeekGaps + "\n";
        return print;
    }
}
