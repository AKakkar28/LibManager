package org.team4.controller.results;

import org.team4.funtionality.rent.RentalService;
import org.team4.maintaindb.MaintainDatabase;
import org.team4.model.items.Item;
import org.team4.model.user.User;
import org.team4.view.user.BookItemPanel;
import org.team4.view.user.BookResultsPanel;
import org.team4.view.user.DVDItemPanel;
import org.team4.view.user.DVDResultsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DVDController implements ActionListener {
    private DVDItemPanel dvdItemPanel;
    private final RentalService rent;
    private User user;

    public DVDController(DVDItemPanel dvdItemPanel, User user) {
        this.dvdItemPanel = dvdItemPanel;
        this.user = user;
        rent = new RentalService();
        addListeners();
    }

    private void addListeners(){
        this.dvdItemPanel.getBtnBuy().addActionListener(this);
        this.dvdItemPanel.getBtnRent().addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.dvdItemPanel.getBtnRent()){
            rentSelectedItem();
        }
    }

    private void rentSelectedItem() {
        Item itemToRent = null;
        String itemTitle = "";

        int selectedRow = DVDResultsPanel.getTable().getSelectedRow();
        if (selectedRow >= 0) {
            String isbn = DVDResultsPanel.getTable().getValueAt(selectedRow, 4).toString();
            itemToRent = MaintainDatabase.getInstance().getDVDDatabase().searchExactDVDByISBN(isbn);
            itemTitle = "DVD";
        }


        // to rent the selected item by the user
        if (itemToRent != null) {
            try {
                if (rent.canRentItem(user, itemToRent)) {
                    if (rent.rentItem(user, itemToRent)) {
                        JOptionPane.showMessageDialog(null, itemToRent.getTitle() + " rented successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to rent " + itemTitle + ". Please try again.");
                    }
                }
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a " + itemTitle + " to rent.");
        }
    }
}