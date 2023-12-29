package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    String sayHello() throws RemoteException;
}
