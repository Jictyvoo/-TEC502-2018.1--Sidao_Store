package br.uefs.ecomp.client.value;
/**
* Classe que serve para armazenar as informações do Item
* */
public class Item {
    private int id;
    private String name;
    private int quantity;

    public Item(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeQuantity(int by) {
        this.quantity += by;
    }

    @Override
    public String toString() {
        return id + " -- " + name + " -- " + quantity;
    }
}
