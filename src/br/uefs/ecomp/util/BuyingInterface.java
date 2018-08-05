package br.uefs.ecomp.util;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public interface BuyingInterface extends Remote {
    boolean buy(String storeIdentification, String item, int amount) throws RemoteException;
}
