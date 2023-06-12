package com.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import com.Entity.Student;

public class RandomRollCall {
    private int n;  // ������n����
    private Connection conn;
    private List<Student> students;
    private List<Student> callList;
    private int unansweredCount;

    public RandomRollCall(int n) {
        this.n = n;
        this.students = new ArrayList<>();
        this.callList = new ArrayList<>();
        this.unansweredCount = 0;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/itheima", "root", "123456");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void rollCall() {
        // �����ݿ��л�ȡǩ���ɹ���ѧ��
        getSignedStudents();

        while (true) {
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(null, "����ͬѧ���ѻش𣬵���������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            // ���ѡ��һ��ѧ�����е���
            Student student = getRandomStudent();
            callList.add(student);
            updateCalledTimes(student);

            // �ж��Ƿ�ش���ȷ
            if (!answerQuestion(student)) {
                unansweredCount++;
                JOptionPane.showMessageDialog(null, "�ش���󣬵�ǰδ�ش����������� �� " + unansweredCount, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                if (unansweredCount == n) {
                    JOptionPane.showMessageDialog(null, "δ�ش�����Ĵ����Ѿ�����" + n + "�����������ӻش������ͬѧ���������", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    getAnsweredStudents();
                    unansweredCount = 0;
                }
            } else {
                unansweredCount = 0;
            }
        }
    }

    private void getSignedStudents() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE state=1")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int calledTimes = rs.getInt("called_times");
                int answeredTimes = rs.getInt("answered_times");
                int state = rs.getInt("state");
                students.add(new Student(id, name, calledTimes, answeredTimes, state));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private Student getRandomStudent() {
        return students.get(new Random().nextInt(students.size()));
    }

    private void updateCalledTimes(Student student) {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE students SET called_times=? WHERE id=?")) {
            stmt.setInt(1, student.getCalledTimes() + 1);
            stmt.setInt(2, student.getId());
            stmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private boolean answerQuestion(Student student) {
        int answer = Integer.parseInt(JOptionPane.showInputDialog(null, "�������ͬѧ��" + student.getName() + "\n��ش�ǰ����\n�Ƿ�ش���ȷ����0��ʾ����ȷ��1��ʾ��ȷ����", "��ʾ", JOptionPane.QUESTION_MESSAGE));
        if (answer == 1) {
            updateAnsweredTimes(student);
            JOptionPane.showMessageDialog(null, "�ش���ȷ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
            students.remove(student); // ���б����Ƴ���ѧ��
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "�ش����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    private void updateAnsweredTimes(Student student) {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE students SET answered_times=? WHERE id=?")) {
            stmt.setInt(1, student.getAnsweredTimes() + 1);
            stmt.setInt(2, student.getId());
            stmt.executeUpdate();

            // ����ѧ��״̬������ɾ�����ݿ��еļ�¼
            student.setState(0);
            try (PreparedStatement stmt2 = conn.prepareStatement("UPDATE students SET state=0 WHERE id=?")) {
            	stmt2.setInt(1, student.getId());
            	stmt2.executeUpdate();
            	} catch (SQLException se) {
            	se.printStackTrace();
            	}
            	} catch (SQLException se) {
            	se.printStackTrace();
            	}
            	}

            	private void getAnsweredStudents() {
            	    students.clear();
            	    try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE answered_times>0")) {
            	        ResultSet rs = stmt.executeQuery();
            	        while (rs.next()) {
            	            int id = rs.getInt("id");
            	            String name = rs.getString("name");
            	            int calledTimes = rs.getInt("called_times");
            	            int answeredTimes = rs.getInt("answered_times");
            	            int state = rs.getInt("state");
            	            students.add(new Student(id, name, calledTimes, answeredTimes, state));
            	        }
            	    } catch (SQLException se) {
            	        se.printStackTrace();
            	    }
            	}

            	public List<Student> getCallList() {
            	    return callList;
            	}

            	public int getUnansweredCount() {
            	    return unansweredCount;
            	}

            	public void setUnansweredCount(int unansweredCount) {
            	    this.unansweredCount = unansweredCount;
            	}
            	}