package com.example.linukey.data.source.remote;

/**
 * Created by linukey on 12/20/16.
 */

public class RemoteObserverEvent {
    public ClientResult clientResult;
    public Object object;

    public RemoteObserverEvent(ClientResult clientResult, Object object){
        this.clientResult = clientResult;
        this.object = object;
    }
}