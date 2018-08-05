package br.uefs.ecomp.client.models.business;

import br.uefs.ecomp.client.value.Item;
import br.uefs.ecomp.models.business.CsvReader;
import br.uefs.ecomp.util.StoreInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

public class Store implements StoreInterface {
    private HashMap<String, Item> items;

    public Store(String fileLocation) {
        this.items = new HashMap<>();
        CsvReader csvReader = new CsvReader();  /*realiza a leitura do arquivo de produtos*/
        try {
            csvReader.readInputFile(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedList<String[]> readerItems = csvReader.getItems();
        for (String[] item : readerItems) { /*adiciona os itens lidos numa hash*/
            this.items.put(item[0], new Item(Integer.parseInt(item[0]), item[1], Integer.parseInt(item[2])));
        }
    }

    @Override
    public void updateItem(String itemId, int quantity) throws RemoteException {    /*Atualiza o item via RMI*/
        if (this.items.containsKey(itemId)) {
            this.items.get(itemId).changeQuantity(quantity);
        }
    }

    public HashMap<String, Item> getItems() {
        return this.items;
    }
}
