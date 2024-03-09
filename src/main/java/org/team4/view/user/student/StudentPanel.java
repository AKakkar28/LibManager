package org.team4.view.user.student;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.team4.maintaindb.MaintainDatabase;
import org.team4.maintaindb.MaintainStudent;
import org.team4.model.course.Course;
import org.team4.model.user.Student;
import org.team4.model.user.User;

import javax.swing.JLabel;

public class StudentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JTable table;
	private ArrayList<String> courseNames;
	private MaintainStudent studentMaintainer = MaintainDatabase.getInstance().getStudentDatabase();

	/**
	 * Create the panel.
	 */
	public StudentPanel() {
		setBounds(100, 100, 788, 520);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("TESTING");
		lblNewLabel.setBounds(199, 188, 264, 185);
		add(lblNewLabel);

	}
	
	public StudentPanel(User student) {
        setLayout(new BorderLayout());
        courseNames = getCourseNames(student.getEmail());
        TextbookTableModel model = new TextbookTableModel(courseNames);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
	
	private ArrayList<String> getCourseNames(String studentEmail) {
		for (Student s : studentMaintainer.getStudents()) {
			if (s.getEmail().equals(studentEmail)) {
				return s.getCourses();
			}
		}
		return new ArrayList<String>();
	}
}
