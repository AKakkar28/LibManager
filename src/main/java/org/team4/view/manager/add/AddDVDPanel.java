package org.team4.view.manager.add;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.team4.model.items.DVD;
import org.team4.model.items.Item;
import org.team4.model.items.builder.DVDBuilder;
import org.team4.model.items.decorator.PurchasableItemDecorator;
import org.team4.model.items.decorator.RentableItemDecorator;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class AddDVDPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String EMPTY = "";

	private JPanel panel;

	private JButton addButton;
	private JTextField titleField;
	private JTextField yearPublishedField;
	private JTextField genreField;
	private JTextField durationField;
	private JTextField quantityField;
	private JTextField priceField;
	private JComboBox canRentComboBox;
	private JComboBox canBuyComboBox;
	private JTextField isbnField;


	/**
	 * Create the panel.
	 */
	public AddDVDPanel() {
		initComponents();
	}

	private void initComponents() {
		setBounds(100, 100, 976, 627);
		setLayout(new BorderLayout());

		panel = new JPanel();
		panel.setLayout(null);

		add(panel, BorderLayout.CENTER);

		JLabel titleLabel = new JLabel("Add DVD");
		titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		titleLabel.setBounds(444, 53, 88, 22);
		panel.add(titleLabel);

		addButton();
		addLabels();
		addFields();
	}

	private void addLabels() {


		JLabel WARN_LABEL = new JLabel("PLEASE FILL ALL FIELDS WITH APPROPRIATE DATA");
		WARN_LABEL.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		WARN_LABEL.setHorizontalAlignment(SwingConstants.CENTER);
		WARN_LABEL.setBounds(323, 87, 367, 16);
		panel.add(WARN_LABEL);

		JLabel dvdTitleLabel = new JLabel("Title:");
		dvdTitleLabel.setBounds(182, 146, 61, 16);
		panel.add(dvdTitleLabel);


		JLabel yearLabel = new JLabel("Year Published:");
		yearLabel.setBounds(182, 192, 107, 16);
		panel.add(yearLabel);

		JLabel rentLabel = new JLabel("Can Rent:");
		rentLabel.setBounds(182, 364, 76, 16);
		panel.add(rentLabel);

		JLabel buyLabel = new JLabel("Can Buy:");
		buyLabel.setBounds(182, 407, 61, 16);
		panel.add(buyLabel);

		JLabel genreLabel = new JLabel("Genre:");
		genreLabel.setBounds(182, 235, 61, 16);
		panel.add(genreLabel);

		JLabel durationLabel = new JLabel("Duration (mins):");
		durationLabel.setBounds(182, 276, 123, 16);
		panel.add(durationLabel);

		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(182, 320, 61, 16);
		panel.add(quantityLabel);

		JLabel priceLabel = new JLabel("Price:");
		priceLabel.setBounds(182, 450, 61, 16);
		panel.add(priceLabel);
		
		JLabel isbnLabel = new JLabel("ISBN:");
		isbnLabel.setBounds(182, 489, 61, 16);
		panel.add(isbnLabel);
	}

	private void addFields() {
		titleField = new JTextField();
		titleField.setBounds(317, 141, 411, 26);
		panel.add(titleField);
		titleField.setColumns(10);

		yearPublishedField = new JTextField();
		yearPublishedField.setBounds(317, 187, 411, 26);
		panel.add(yearPublishedField);
		yearPublishedField.setColumns(10);

		genreField = new JTextField();
		genreField.setBounds(317, 230, 411, 26);
		panel.add(genreField);
		genreField.setColumns(10);

		durationField = new JTextField();
		durationField.setBounds(317, 271, 136, 26);
		panel.add(durationField);
		durationField.setColumns(10);

		quantityField = new JTextField();
		quantityField.setBounds(317, 315, 136, 26);
		panel.add(quantityField);
		quantityField.setColumns(10);
		
		canRentComboBox = new JComboBox();
		canRentComboBox.setModel(new DefaultComboBoxModel(new String[] {"false", "true"}));
		canRentComboBox.setBounds(317, 360, 136, 27);
		panel.add(canRentComboBox);

		canBuyComboBox = new JComboBox();
		canBuyComboBox.setModel(new DefaultComboBoxModel(new String[] {"false", "true"}));
		canBuyComboBox.setBounds(317, 403, 136, 27);
		panel.add(canBuyComboBox);

		priceField = new JTextField();
		priceField.setBounds(317, 445, 130, 26);
		panel.add(priceField);
		priceField.setColumns(10);
		
		isbnField = new JTextField();
		isbnField.setBounds(317, 484, 130, 26);
		panel.add(isbnField);
		isbnField.setColumns(10);
	}

	private void addButton() {
		addButton = new JButton("Add");
		addButton.setBounds(433, 537, 117, 29);
		panel.add(addButton);
	}
	
	public DVD getDVD() {
		
		String title = titleField.getText();
		String genre = genreField.getText();
		String isbn = isbnField.getText();
		
		int yearPublished = Integer.parseInt(yearPublishedField.getText());
		int quantity = Integer.parseInt(quantityField.getText());
		
		double duration = Double.parseDouble(durationField.getText());
		double price = Double.parseDouble(priceField.getText());
		
		boolean canBuy = Boolean.parseBoolean(canBuyComboBox.getSelectedItem().toString());
		boolean canRent = Boolean.parseBoolean(canRentComboBox.getSelectedItem().toString()); 
		
		Item newDVD = ((DVDBuilder) new DVDBuilder()
				.title(title)
				.yearPublished(yearPublished))
				.genre(genre)
				.duration(duration)
				.price(price)
				.quantity(quantity)
				.ISBN(isbn)
				.build();
		
		if(canBuy) {
            PurchasableItemDecorator purchasableDecorator = new PurchasableItemDecorator(newDVD);
            newDVD = purchasableDecorator.getItem();
        }
		if(canRent) {
            RentableItemDecorator rentableDecorator = new RentableItemDecorator(newDVD);
            newDVD = rentableDecorator.getItem();
        }
		
		clearFields();
		
		return (DVD) newDVD;
	}
	
	public JButton getAddButton() {
		return addButton;
	}
	
	private void clearFields() {
		titleField.setText(EMPTY);
		genreField.setText(EMPTY);
		yearPublishedField.setText(EMPTY);
		quantityField.setText(EMPTY);
		durationField.setText(EMPTY);
		priceField.setText(EMPTY);
		isbnField.setText(EMPTY);
	}
}
