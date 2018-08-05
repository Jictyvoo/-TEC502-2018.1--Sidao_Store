package br.uefs.ecomp.client.views;

import br.uefs.ecomp.client.controllers.Controller;
import br.uefs.ecomp.client.value.Item;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;

class MainUI {

    private JFrame frame;
    private JList<String> list;

    /**
     * Create the application.
     */
    MainUI() {
        this.initialize();
        this.updateList();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        list = new JList<>();
        list.setBounds(47, 12, 139, 191);
        frame.getContentPane().add(list);

        JTextField txtQuantity = new JTextField();
        txtQuantity.setBounds(282, 103, 124, 19);
        frame.getContentPane().add(txtQuantity);
        txtQuantity.setColumns(10);

        JLabel lblQuantity = new JLabel("Quantity");
        lblQuantity.setBounds(198, 105, 66, 15);
        frame.getContentPane().add(lblQuantity);

        JButton btnBuy = new JButton("Buy");
        btnBuy.setBounds(208, 23, 114, 25);
        frame.getContentPane().add(btnBuy);
        btnBuy.addActionListener((event) -> {
            int quantity;
            try {
                quantity = Integer.parseInt(txtQuantity.getText()) * -1;
                if (!Controller.getInstance().buyItem(list.getSelectedValue(), quantity)) {
                    JOptionPane.showMessageDialog(null, "Failed in Buy");
                } else {
                    this.updateList();
                }
            } catch (NumberFormatException | RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateList() { /*Atualiza a Lista impressa na Tela*/
        DefaultListModel<String> list = new DefaultListModel<>();
        Collection<Item> items = null;
        try {
            items = Controller.getInstance().getStore().getItems().values();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        assert items != null;
        for (Item item : items) {
            list.addElement(item.toString());
        }
        this.list.setModel(list);
    }

    void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }
}
