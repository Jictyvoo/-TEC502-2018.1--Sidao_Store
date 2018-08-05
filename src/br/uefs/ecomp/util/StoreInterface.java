package br.uefs.ecomp.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StoreInterface extends Remote {
    void updateItem(String itemId, int quantity) throws RemoteException;
}
