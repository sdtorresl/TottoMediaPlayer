package database;

import java.sql.Time;
import java.util.Iterator;
import util.*;

/**
 *
 * @author sergio
 */
public class Event {
    private String name;
    private CircularArrayList<Category> categories;
    private Time startTime;
    private Time endTime;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public CircularArrayList<Category> getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     */
    public void setCategories(CircularArrayList<Category> categories) {
        this.categories = categories;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event: ").append(this.name).append("\n");
        sb.append("Start Time: ").append(this.startTime).append("\n");
        sb.append("End Time: ").append(this.endTime).append("\n");
        
        Iterator iterator = categories.iterator();
        while(iterator.hasNext()) {
            sb.append("* ").append(iterator.next().toString());
        }
        
        return sb.toString();
    }
}