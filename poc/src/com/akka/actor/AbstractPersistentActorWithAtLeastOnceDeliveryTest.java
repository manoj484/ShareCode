package com.akka.actor;

import java.io.Serializable;

import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.persistence.AbstractPersistentActorWithAtLeastOnceDelivery;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import akka.japi.Function;

public class AbstractPersistentActorWithAtLeastOnceDeliveryTest {
	class Msg implements Serializable {
		  private static final long serialVersionUID = 1L;
		  public final long deliveryId;
		  public final String s;

		  public Msg(long deliveryId, String s) {
		    this.deliveryId = deliveryId;
		    this.s = s;
		  }
		}

		class Confirm implements Serializable {
		  private static final long serialVersionUID = 1L;
		  public final long deliveryId;

		  public Confirm(long deliveryId) {
		    this.deliveryId = deliveryId;
		  }
		}


		class MsgSent implements Serializable {
		  private static final long serialVersionUID = 1L;
		  public final String s;

		  public MsgSent(String s) {
		    this.s = s;
		  }
		}
		class MsgConfirmed implements Serializable {
		  private static final long serialVersionUID = 1L;
		  public final long deliveryId;

		  public MsgConfirmed(long deliveryId) {
		    this.deliveryId = deliveryId;
		  }
		}

		class MyPersistentActor extends AbstractPersistentActorWithAtLeastOnceDelivery {
		  private final ActorSelection destination;

		  public MyPersistentActor(ActorSelection destination) {
		      this.destination = destination;
		  }

		  @Override public String persistenceId() {
		    return "persistence-id";
		  }

		  @Override
		  public Receive createReceive() {
		    return receiveBuilder().
		      match(String.class, s -> {
		        persist(new MsgSent(s), evt -> updateState(evt));
		      }).
		      match(Confirm.class, confirm -> {
		        persist(new MsgConfirmed(confirm.deliveryId), evt -> updateState(evt));
		      }).
		      build();
		  }

		  public Receive createReceiveRecover() {
		    return receiveBuilder().
		        match(Object.class, evt -> updateState(evt)).build();
		  }

		  void updateState(Object event) {
		    if (event instanceof MsgSent) {
		      final MsgSent evt = (MsgSent) event;
		     // deliver(destination, deliveryId -> new Msg(deliveryId, evt.s));
		      deliver((ActorPath) destination, new Function<Long, Object>() {
		          public Object apply(Long deliveryId) {
		            return new Msg(deliveryId, evt.s);
		          }
		        });
		    } else if (event instanceof MsgConfirmed) {
		      final MsgConfirmed evt = (MsgConfirmed) event;
		      confirmDelivery(evt.deliveryId);
		    }
		  }

		@Override
		public PartialFunction<Object, BoxedUnit> receiveCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PartialFunction<Object, BoxedUnit> receiveRecover() {
			// TODO Auto-generated method stub
			return null;
		}
		}

		class MyDestination extends AbstractActor {
		  @Override
		  public Receive createReceive() {
		    return receiveBuilder()
		      .match(Msg.class, msg -> {
		        // ...
		        getSender().tell(new Confirm(msg.deliveryId), getSelf());
		      })
		      .build();
		  }
		}
		public static void main(String [] args) throws InterruptedException{
			final ActorSystem system = ActorSystem.create("example");
		    final ActorRef persistentActor = system.actorOf(Props.create(MyPersistentActor.class));
		    persistentActor.tell("a", null);
		    persistentActor.tell("print", null);
		    Thread.sleep(10000);
	        system.terminate();
		}
}
