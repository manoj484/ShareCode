package com.akka.event;

import java.io.Serializable;

public class MsgConfirmed implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public final long deliveryId;
	  
	  public MsgConfirmed(long deliveryId) {
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
