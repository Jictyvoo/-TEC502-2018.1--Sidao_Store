package br.uefs.ecomp.client.controllers;

import br.uefs.ecomp.client.models.business.Store;
import br.uefs.ecomp.util.BuyingInterface;
import br.uefs.ecomp.util.StoreInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class Controller {

    private BuyingInterface buying;
    private Store store;
    private static Controller instance;
    private String storeName;

    private Controller() throws RemoteException, NotBoundException {

        StringBuilder returnString = new StringBuilder(); //String para armazenar erros encontrados
        int lineNumber = 1; //Contador de linhas do arquivo
        FileReader readingFile = null;
        BufferedReader readingNow = null;
        String host = "127.0.0.1";
        int port = 8000;
        try {
            readingFile = new FileReader("config.conf"); //Tenta abrir o arquivo
            readingNow = new BufferedReader(readingFile); //Buffer para realizar a leitura

            String lineReaded;
            while ((lineReaded = readingNow.readLine()) != null) { //Lê cada linha do arquivo até chegar no final
                if (lineNumber == 1) {
                    this.storeName = lineReaded.split(",")[2].replace(" ", "");
                    host = lineReaded.split(",")[0].replace(" ", "");
                } else if(lineReaded.split(",")[0].replace(" ", "").equals(this.storeName)){
                    port = Integer.parseInt(lineReaded.split(",")[1].replace(" ", ""));
                }
                lineNumber += 1;
            }
        } catch (IOException exceptionFound) { //Erro na abertura do arquivo
            returnString.append(exceptionFound.getMessage());
        } finally { //Fecha o arquivo
            if (readingFile != null) {  /*Faz o tratamento das exceções*/
                try {
                    readingFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (readingNow != null) {
                    try {
                        readingNow.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        this.store = new Store("products.csv");

        System.setProperty("java.rmi.server.hostname", "192.168.43.250");
        Registry registry = LocateRegistry.getRegistry(host, 8080);   /*Pega o registro criado*/
        this.buying = (BuyingInterface) registry.lookup("MainServer");  /*Busca o objeto do Servidor*/
        StoreInterface stub = (StoreInterface) UnicastRemoteObject.exportObject(this.store, port);
        try {
            registry.bind(this.storeName, stub);
        } catch (AlreadyBoundException e) { /*Caso seja um reinicio da Loja, adiciona o objeto novamente*/
            registry.rebind(this.storeName, stub);
        }
    }

    public static Controller getInstance() throws RemoteException, NotBoundException {  /*É um singletone, logo tem o método de instancia*/
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public boolean buyItem(String itemID, int quantity) throws RemoteException {    /*método que chama o RMI para realizar a compra*/
        itemID = itemID.split(" -- ")[0];
        return this.buying.buy(this.storeName, itemID, quantity);
    }

    public Store getStore() {
        return this.store;
    }
}
