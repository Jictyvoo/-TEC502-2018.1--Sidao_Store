package br.uefs.ecomp.server.models.business;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import br.uefs.ecomp.client.models.business.Store;
import br.uefs.ecomp.client.value.Item;
import br.uefs.ecomp.util.BuyingInterface;
import br.uefs.ecomp.util.StoreInterface;
import br.uefs.ecomp.util.exception.IncorrectFormatException;
import br.uefs.ecomp.util.exception.InputInformationIncorrectException;

public class Buying implements BuyingInterface {
    private Stack<String> stack;
    private Registry registry;
    private HashMap<String, Item> items;
    private static Buying ourInstance = new Buying();
    private LinkedList<String> remoteStores;

    private Buying() {
        this.stack = new Stack<>();
        this.items = new Store("products.csv").getItems();
        this.remoteStores = new LinkedList<>();
        StringBuilder returnString = new StringBuilder(); //String para armazenar erros encontrados
        int lineNumber = 1; //Contador de linhas do arquivo
        FileReader readingFile = null;
        BufferedReader readingNow = null;
        try {
            readingFile = new FileReader("config.conf"); //Tenta abrir o arquivo
            readingNow = new BufferedReader(readingFile); //Buffer para realizar a leitura

            String lineReaded;
            while ((lineReaded = readingNow.readLine()) != null) { //Lê cada linha do arquivo até chegar no final
                if (lineNumber != 1) {
                    this.remoteStores.add(lineReaded);
                }
                lineNumber += 1;
            }
        } catch (IOException exceptionFound) { //Erro na abertura do arquivo
            returnString.append(exceptionFound.getMessage());
        } finally { //Fecha o arquivo
            if (readingFile != null) {
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
    }

    public static Buying getInstance() {
        return ourInstance;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }   /*Seta o objeto de registro para buscar os objetos de RMI*/

    @Override
    public synchronized boolean buy(String storeIdentification, String item, int amount) throws RemoteException {
        if (this.items.get(item).getQuantity() + amount >= 0) { /*verifica se existe o item*/
            this.stack.push(storeIdentification);
            for (String storeName : this.remoteStores) {
                StoreInterface store = null;
                try {   /*Busca o objeto remoto*/
                    store = (StoreInterface) this.registry.lookup(storeName.split(",")[0]);
                } catch (NotBoundException e) {
                    //e.printStackTrace();
                    System.out.println("Capture Exception: " + e.toString());
                }
                if (store != null) {
                    store.updateItem(item, amount); /*Atualiza o item em todos os objetos remotos*/
                }
            }
            return true;
        }
        return false;
    }
}
