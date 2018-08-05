package br.uefs.ecomp.server;

import br.uefs.ecomp.server.models.business.Buying;
import br.uefs.ecomp.util.BuyingInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class MainServer {

    public MainServer() {
    }

    public static void main(String args[]) {

        try {
            // Bind the remote object's stub in the registry

            System.setProperty("java.rmi.server.hostname", "192.168.43.250");
            Registry registry = LocateRegistry.createRegistry(8080);
            BuyingInterface stub = (BuyingInterface) UnicastRemoteObject.exportObject(Buying.getInstance(), 8080);
            Buying.getInstance().setRegistry(registry);
            registry.bind("MainServer", stub);
            System.err.println("Sidao Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}