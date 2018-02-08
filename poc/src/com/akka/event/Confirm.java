package com.akka.event;

import java.io.Serializable;

public class Confirm implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public final long deliveryId;
	  
	  public Confirm(long deliveryId) {
	    this.deliveryId = deliveryId;
	  }
	  
	  public long getData() {
	        return this.deliveryId;
	    }

	    @Override
	    public String toString() {
	        return "Command{" +
	                "data='" + deliveryId + '\'' +
	                '}';
	    }
	}