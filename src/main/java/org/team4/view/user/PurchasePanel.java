package org.team4.view.user;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PurchasePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
public PurchasePanel() {
		setBounds(100, 100, 1160, 740);
        setLayout(null);
        
        JLabel label = new JLabel("Purchase ");
        label.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        label.setBounds(534, 5, 91, 25);
		add(label);
    }

}
