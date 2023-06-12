package Controller;

import com.DAO.ClearDatabase;
import com.DAO.ExportDatabaseContentToTextFile;
import com.Entity.Student;
import com.Service.CalculateAttendanceRate;
import com.Service.CalculateStudentStats;
import com.Service.CollCall;
import com.Service.SignIn;
import com.Util.Connecting;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Main extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel, buttonPanel, inputPanel, outputPanel;
    private JButton signInButton, attendanceButton, collCallButton, studentStatsButton, exportDataButton, exitButton, submitButton;
    //private JTextField inputField;
    private JTextArea outputTextArea;

    public Main() {
        // ���ô��ڴ�С��λ��
    	setName("���ǩ���͵���ϵͳ");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // ���������Ͱ�ť���
        mainPanel = new JPanel(new GridBagLayout());
        buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();

        // ������ť����ӵ���ť���
        signInButton = new JButton("ǩ��");
        attendanceButton = new JButton("���������");
        collCallButton = new JButton("�������");
        studentStatsButton = new JButton("����ѧ������");
        exportDataButton = new JButton("�������ݿ�����");
        exitButton = new JButton("�˳�����");

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        buttonPanel.add(signInButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(attendanceButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(collCallButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(studentStatsButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(exportDataButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(exitButton, gbc);

        // ��Ӱ�ť��嵽�������
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 0, 10);
        mainPanel.add(buttonPanel, gbc);

        // ������������������
        //inputPanel = new JPanel(new FlowLayout());
        outputPanel = new JPanel(new BorderLayout());

        // ����������ύ��ť�������ǩ������ӵ���Ӧ�������
        //inputField = new JTextField(20);
        //submitButton = new JButton("�ύ");
        outputTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        //inputPanel.add(new JLabel("�����룺"));
        //inputPanel.add(inputField);
        //inputPanel.add(submitButton);

        // ������������������ӵ��������
        //gbc.gridx = 0;
        //gbc.gridy = 1;
        //gbc.gridwidth = 1;
        //gbc.gridheight = 1;
        //gbc.weightx = 1.0;
        //gbc.weighty = 0.0;
        //gbc.insets = new Insets(10, 10, 10, 10);
        //mainPanel.add(inputPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 10, 10, 10);
        mainPanel.add(outputPanel, gbc);

        // ע�ᰴť������
        signInButton.addActionListener(this);
        attendanceButton.addActionListener(this);
        collCallButton.addActionListener(this);
        studentStatsButton.addActionListener(this);
        exportDataButton.addActionListener(this);
        exitButton.addActionListener(this);
        //submitButton.addActionListener(this);

        // ���������ӵ�������
        add(mainPanel);

        // ���ô��ڿɼ���
        setVisible(true);
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signInButton) {
            // ����ǩ������
            try {
                List<Student> signedStudents = SignIn.run(outputTextArea);
                for (Student student : signedStudents) {
                    outputTextArea.append("ǩ���ɹ���������" + student.getName() + "\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "ǩ��ʧ�ܣ�" + ex.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == attendanceButton) {
            // ���ü�������ʷ���
            CalculateAttendanceRate.calculateAttendanceRateAndOutput(outputTextArea);
            JOptionPane.showMessageDialog(this, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (e.getSource() == collCallButton) {
            // ���������������
            
            Student calledStudents = CollCall.collCall();;
			
            JOptionPane.showMessageDialog(this, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == studentStatsButton) {
            // ���ü���ѧ�����ݷ���
            CalculateStudentStats calculator = new CalculateStudentStats();
            outputTextArea.append("ѧ��\t����\t��������\t�ش����\t����״̬\n");
            List<Student> students = calculator.calculateStudentStats(outputTextArea);
            for (Student student : students) {
                outputTextArea.append(student.getId() + "\t" + student.getName() + "\t" + student.getCalledTimes() +
                        "\t" + student.getAnsweredTimes() + "\t" + student.getState() + "%\n");
            }
            JOptionPane.showMessageDialog(this, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (e.getSource() == exportDataButton) {
            // ���õ������ݿ����ݷ���
            ExportDatabaseContentToTextFile.exportDatabaseContentToTextFile();
            JOptionPane.showMessageDialog(this, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitButton) {
            // �˳�����
            System.exit(0);
        } //else if (e.getSource() == submitButton) {
            // �����ύ��ť�Ķ���
           // String inputText = inputField.getText();
            //outputTextArea.append(inputText + "\n");
            // ���������е��ı�
            //inputField.setText("");
        //}
    }

    public static void main(String[] args) {
        // ʹ��ǰ������ݿ�
        ClearDatabase.clearDatabase();

        // ��ѧ����Ϣ�������ݿ�
        List<Student> students = readStudentsFromFile();
        insertStudentsIntoDatabase(students);

        // ����GUI����
        new Main();
    }

    private static List<Student> readStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int calledTimes = Integer.parseInt(parts[2]);
                int answeredTimes = Integer.parseInt(parts[3]);
                int state = Integer.parseInt(parts[4]);
                students.add(new Student(id, name, calledTimes, answeredTimes, state));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    private static void insertStudentsIntoDatabase(List<Student> students) {
        try (Connection conn = Connecting.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (id,name,called_times,answered_times,state) VALUES (?, ?, ?, ?, ?)")) {
            for (Student student : students) {
                stmt.setInt(1, student.getId());
                stmt.setString(2, student.getName());
                stmt.setInt(3, student.getCalledTimes());
                stmt.setInt(4, student.getAnsweredTimes());
                stmt.setInt(5, student.getState());
                stmt.executeUpdate();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
   