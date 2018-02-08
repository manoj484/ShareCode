package com.akka.actor;

import com.akka.event.Command;
import com.akka.event.Event;
import com.akka.event.Msg;

import akka.actor.UntypedActor;

@SuppressWarnings("deprecation")
public class Emitter extends UntypedActor {
	@Override
    public void onReceive(Object msg) {

        if (msg instanceof Command) {

            String data = ((Command) msg).getData();
            getContext().system().eventStream().publish(new Event(data));
            
        } else if (msg instanceof Msg) {
        	String dataMsg = ((Msg) msg).getData();
        	System.out.println("Got the respopnse message as :"+ dataMsg);
        }
    }
}
