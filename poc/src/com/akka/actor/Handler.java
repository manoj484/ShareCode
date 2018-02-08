package com.akka.actor;

import com.akka.event.Event;
import com.akka.event.Msg;

import akka.actor.UntypedActor;

@SuppressWarnings("deprecation")
public class Handler extends UntypedActor {
	 @Override
	    public void onReceive(Object msg) throws Exception {
		 
		   if (msg instanceof Event) {
			 String data = ((Event) msg).getData();
	        System.out.println("Handled Event: " + msg);
	        getContext().system().eventStream().publish(new Msg(123,"Success:"+data));
		   }
	    }
}
