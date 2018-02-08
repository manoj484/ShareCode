package com.akka.actor;

import java.util.concurrent.TimeUnit;

import com.akka.event.Confirm;
import com.akka.event.MsgConfirmed;
import com.akka.event.MsgSent;

import akka.actor.ActorRef;
import akka.japi.Procedure;
import akka.persistence.Deliver;
import akka.persistence.Persistent;
import akka.persistence.PersistentChannel;
import akka.persistence.PersistentChannelSettings;
import akka.persistence.UntypedPersistentActorWithAtLeastOnceDelivery;
import scala.concurrent.duration.Duration;

public class MyPersistentActor extends UntypedPersistentActorWithAtLeastOnceDelivery {
	 // private final ActorPath destination;
    private ActorRef receiver;
    private ActorRef channel;

    @SuppressWarnings("deprecation")
	public MyPersistentActor(ActorRef receiver) {
        this.receiver = receiver;
        System.out.println("MyPersistentActor constructor..");
        this.channel = getContext().actorOf(PersistentChannel.props(
                PersistentChannelSettings.create()
                        .withRedeliverInterval(Duration.create(30, TimeUnit.SECONDS))
                        .withPendingConfirmationsMax(10000) // max # of pending confirmations. suspend delivery until <
                        .withPendingConfirmationsMin(2000)  // min # of pending confirmation. suspend delivery until >
                        .withReplyPersistent(true)          // ack
                        .withRedeliverMax(15)), "channel");
    }
 
 /* public MyPersistentActor(ActorPath destination) {
      this.destination = destination;
  }*/
 
  public void onReceiveCommand(Object message) {
    if (message instanceof String) {
      String s = (String) message;
      System.out.println("get Message :"+s);
      persist(new MsgSent(s), new Procedure<MsgSent>() {
        public void apply(MsgSent evt) {
          updateState(evt);
          if (message.equals("Error")) {
          	throw new RuntimeException();
          }
        }
      });
    } else if (message instanceof Confirm) {
      System.out.println("EventReply :"+message.toString());
      Confirm confirm = (Confirm) message;
      persist(new MsgConfirmed(confirm.deliveryId), new Procedure<MsgConfirmed>() {
        public void apply(MsgConfirmed evt) {
          updateState(evt);
        }
      });
    }  else {
      System.out.println("unhandled message in onReceiveCommand");
      unhandled(message);
    }
  }
  
  public void onReceiveRecover(Object event) {
	System.out.println("onReceiveRecover MessageSent :"+event.toString());
    updateState(event);
  }
  
  @SuppressWarnings("deprecation")
void updateState(Object event) {
    if (event instanceof MsgSent) {
      final MsgSent evt = (MsgSent) event;
      
      /*deliver(destination, new Function<Long, Object>() {
        public Object apply(Long deliveryId) {
          return new Msg(deliveryId, evt.s);
        }
      });*/
      /*if (evt.getData().equals("Error")) {
    	  System.out.println("evt.getData():"+evt.getData());
      	throw new RuntimeException();
      }*/
      Persistent persistent = Persistent.create(evt); //
      channel.tell(Deliver.create((persistent).withPayload((persistent).payload()), receiver.path()), getSelf());//
      System.out.println("MessageSent :"+evt.toString());
    } else if (event instanceof MsgConfirmed) {
      final MsgConfirmed evt = (MsgConfirmed) event;
      System.out.println("MsgConfirmed :"+evt.toString()+"-with deliveryId:"+evt.deliveryId);
      confirmDelivery(evt.deliveryId);
    } else{
    	System.out.println("unhandled message in updateState");
    }
  }
}

