package org.datavault.common.model.dao;

import java.util.List;
import org.datavault.common.event.Event;

public interface EventDAO {
    
    public void save(Event event);
    
    public List<Event> list();

    public Event findById(String Id);
    
}
