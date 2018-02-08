package com.akka.app;

import com.akka.actor.MyDestination;
import com.akka.actor.MyPersistentActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SystemTestAtLeastOnceDelivery {
	
	public static void main(String[] args) throws InterruptedException {
		final ActorSystem actorSystem = ActorSystem.create("actor-atleastOnce");
		
		System.out.println("Actor System started ...");
		final ActorRef myDestination = actorSystem.actorOf(Props.create(MyDestination.class));
        final ActorRef actorRef = actorSystem.actorOf(Props.create(MyPersistentActor.class, myDestination), "channel-processor");
        
        System.out.println("Actor System sending message ...");
        actorRef.tell("msg1", null);
        actorRef.tell("Error", null);
        actorRef.tell("msg2", null);
        
        Thread.sleep(4000);
       
        //actorSystem.shutdown();
        actorSystem.terminate();
        System.out.println("Actor System Shutdown ...");
	}

}
