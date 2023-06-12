package com.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.Util.Connecting;
import com.Entity.Student;

public class SignIn {
    public static List<Student> run(JTextArea outputTextArea) throws SQLException {
        Connection conn = Connecting.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT name FROM students ORDER BY name");
        ResultSet rs = stmt.executeQuery();

        int signedStudents = 0;
        List<Student> signedStudentList = new ArrayList<>();
        while (rs.next()) {
            String studentName = rs.getString("name");
            int signInStatus = -1;

            while (signInStatus != 0 && signInStatus != 1) {
                String inputStatus = JOptionPane.showInputDialog(null, "������ " + studentName + " ��ǩ��״̬��0��ʾδǩ����1��ʾ��ǩ������");
                try {
                    signInStatus = Integer.parseInt(inputStatus);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "�����ʽ�������������֣�");
                }
                
                if (signInStatus == 1) {
                    signedStudents++;
                    signedStudentList.add(new Student(signInStatus, studentName, 0, 0, 1));
                }
                outputTextArea.append("��ǩ��ѧ������" + signedStudents + "\n");
            }

            String updateQuery = "UPDATE students SET state = ? WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setInt(1, signInStatus);
            ps.setString(2, studentName);
            ps.executeUpdate();
        }

        String signedStudentsStr = "��ǩ����ѧ���У�\n";
        for (Student signedStudent : signedStudentList) {
            signedStudentsStr += signedStudent.getName() + "\n";
        }
        outputTextArea.append(signedStudentsStr);

        rs.close();
        stmt.close();
        conn.close();
		return signedStudentList;
    }
}
