package com.akka.actor;

import com.akka.event.Confirm;

import akka.actor.UntypedActor;
import akka.persistence.ConfirmablePersistent;

@SuppressWarnings("deprecation")
public class MyDestination extends UntypedActor {
	  public void onReceive(Object message) throws Exception {
		    /*if (message instanceof Msg) {*/
			  if (message instanceof ConfirmablePersistent) {
					ConfirmablePersistent confirmablePersistent = (ConfirmablePersistent) message;
					System.out.println("Handled Message: " + message);
					//Msg msg = (Msg) message;
					//getSender().tell(new Confirm(msg.deliveryId), getSelf());
					getSender().tell(new Confirm(confirmablePersistent.sequenceNr()), getSelf());
					confirmablePersistent.confirm();
		    } else {
		      System.out.println("unhandled message in MyDestination");
		      unhandled(message);
		    }
		  }
		}
