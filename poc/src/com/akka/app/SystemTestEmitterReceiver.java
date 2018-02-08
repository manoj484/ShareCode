package com.akka.app;

import com.akka.actor.Emitter;
import com.akka.actor.Handler;
import com.akka.event.Command;
import com.akka.event.Event;
import com.akka.event.Msg;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SystemTestEmitterReceiver {

	public static void main(String[] args) throws InterruptedException {
		final ActorSystem actorSystem = ActorSystem.create("event-system");
		
		final ActorRef emitter = actorSystem.actorOf(Props.create(Emitter.class));
	    final ActorRef handler = actorSystem.actorOf(Props.create(Handler.class));
	    actorSystem.eventStream().subscribe(handler, Event.class);
	    
	    actorSystem.eventStream().subscribe(emitter, Msg.class);
	    
	    System.out.println("Actor System start...");
	        for (int info = 0; info < 8; info++) {
	            emitter.tell(new Command("CMD " + info), null);
	            System.out.println("Actor System sent the message"+ info);
	        }
	        Thread.sleep(1000);
	        System.out.println("Actor System Shutdown ...");
	        actorSystem.terminate();
	}

}
