package com.Service;

import javax.swing.JOptionPane;

import com.Entity.Student;

public class CollCall {
    public static Student collCall() {
        boolean continueRollCall = true;
        while (continueRollCall) {
            String input = JOptionPane.showInputDialog("���������������ͻ��ش���ȷ���˵�������");
            int n = Integer.parseInt(input);
            RandomRollCall rrc = new RandomRollCall(n);
            rrc.rollCall();
            int numUnanswered = rrc.getUnansweredCount();
            if (numUnanswered > n) {
                continueRollCall = false;
                JOptionPane.showMessageDialog(null, "�Ѿ��ﵽ" + n + "����δ�ش����⣬��������ʦΪ��ҽ��������⡣\n��ʦ�����С���", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(null, "�Ƿ���������µ�һ�ֵ�����", "��ʾ", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    continueRollCall = false;
                }
            }
        }
		return null;
    }
}
