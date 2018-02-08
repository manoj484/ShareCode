package com.akka.event;
import java.io.Serializable;
public class Event implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String data;

    public Event(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Event{" +
                "data='" + data + '\'' +
                '}';
    }
}
