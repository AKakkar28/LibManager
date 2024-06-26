package org.team4.view.manager.validate;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.team4.maintaindb.MaintainDatabase;
import org.team4.maintaindb.MaintainUser;
import org.team4.model.user.User;

import javax.swing.JButton;
import javax.swing.JComboBox;

public class ValidateUserPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayList<User> nonValidatedUsers;
	private MaintainUser maintainUser = MaintainDatabase.getInstance().getUserDatabase();
	
	private NonValidatedUsersTableModel nonValidatedUsersTableModel;

	private JButton updateButton;

    /**
	 * Create the panel.
	 */
	public ValidateUserPanel() {
		this.nonValidatedUsers = null;
		try {
			this.nonValidatedUsers = maintainUser.getAllUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initValidate();
	}
	
	public void initValidate() {
		setBounds(100, 100, 988, 720);
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
	    panel.setLayout(null);
		
		add(panel, BorderLayout.CENTER);
		
		nonValidatedUsersTableModel = new NonValidatedUsersTableModel(this.nonValidatedUsers);
        JTable nonValidatedUsersTable = new JTable(nonValidatedUsersTableModel);
		
		TableColumn validatedColumn = nonValidatedUsersTable.getColumnModel().getColumn(3);
        JComboBox<Boolean> comboBox = new JComboBox<>(new Boolean[]{true, false});
        validatedColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane usersPane = new JScrollPane(nonValidatedUsersTable);
		usersPane.setBounds(35, 63, 916, 593);
		panel.add(usersPane);
		
		updateButton = new JButton("Update Users");
		updateButton.setBounds(834, 668, 117, 29);
		
		panel.add(updateButton);	
		
		JLabel TitleLabel = new JLabel("Validate User");
		panel.add(TitleLabel);
		TitleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		TitleLabel.setBounds(428, 25, 132, 16);
	}

	public JButton getUpdateButton(){
		return this.updateButton;
	}
	
	public void updateSelectedUsers() {
        int rowCount = nonValidatedUsersTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Boolean validatedStatus = (Boolean) nonValidatedUsersTableModel.getValueAt(i, 3);
            User user = nonValidatedUsers.get(i);
            user.setValidated(validatedStatus);
        }

        // Now, update the CSV with changed fields
        try {
        	maintainUser.update();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
}


