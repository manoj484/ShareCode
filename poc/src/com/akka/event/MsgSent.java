package com.akka.event;

import java.io.Serializable;

public class MsgSent implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public final String s;
	  
	  public MsgSent(String s) {
	    this.s = s;
	  }
	  
	  public String getData() {
	        return this.s;
	    }

	    @Override
	    public String toString() {
	        return "Command{" +
	                "data='" + s + '\'' +
	                '}';
	    }
	}
