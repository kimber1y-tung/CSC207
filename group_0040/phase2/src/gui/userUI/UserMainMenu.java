

package gui.userUI;

import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import gui.loginUI.LoginPage;
import gui.loginUI.MainPage;

import java.awt.event.*;
import javax.swing.*;

/**
 * @author Namratha Anil
 */
public class UserMainMenu extends JPanel {
    private MainPage mainFrame;
    public Integer userID;
    private String mode = "";

    public UserMainMenu(String mode) {
        this.mode = mode;
        initComponents();
        label1.setText("To turn vacation on or off, click the button above");
        label3.setText("Welcome User:" + LoginPage.getCurrentUserName());
    }

    /**
     * @param mainFrame
     */
    public void setMainFrame(MainPage mainFrame) {
        this.mainFrame = mainFrame;
    }



    /**
     * changed JPanel to JPanel in class to StartUpPage and logs out user
     */

    private void logOutActionPerformed(ActionEvent e) {
        mainFrame.setView("StartUpPage");
        LoginPage.resetCurrentUserName();
    }


    private void tradeMenuActionPerformed(ActionEvent e) {
        if (mainFrame.getMainController().getUserMenuController().getVacationStatus(LoginPage.getCurrentUserName()) == 1) {
            JOptionPane.showMessageDialog(mainFrame, "Warning! You have set your account on vacation mode. To make trades or \n" +
                    " to view your inventory again, you need to turn vacation mode off");
        } else {
            this.mainFrame.setView("TradeMainMenu");
        }
    }


    private void vacationStatusActionPerformed(ActionEvent e) {
        Map<Integer, String> vacationStatus = new HashMap<>();
        vacationStatus.put(1, "ON");
        vacationStatus.put(0, "OFF");
        mainFrame.getMainController().getUserMenuController().changeVacationStatus(LoginPage.getCurrentUserName());
        label1.setText("VACATION MODE: " + vacationStatus.get(mainFrame.getMainController().getUserMenuController().getVacationStatus(LoginPage.getCurrentUserName())));
        JOptionPane.showMessageDialog(mainFrame, "Vacation Status Was Successfully Changed to: " +
                vacationStatus.get(mainFrame.getMainController().getUserMenuController().getVacationStatus(LoginPage.getCurrentUserName())));
    }
    /**
     * changed JPanel to JPanel in class to UserAccountMenu
     */



    private void accountMenuActionPerformed(ActionEvent e) {
        if (mainFrame.getMainController().getUserMenuController().getVacationStatus(LoginPage.getCurrentUserName()) == 1) {
            JOptionPane.showMessageDialog(mainFrame, "Warning! You have set your account on vacation mode. To make trades or \n" +
                    "to view your inventory again, you need to turn vacation mode off");
        } else {
            mainFrame.setView("UserAccountMenu");
        }
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Josh Fox
		ResourceBundle bundle = ResourceBundle.getBundle("gui.locales.UserMainMenu");
		label2 = new JLabel();
		label3 = new JLabel();
		logOut = new JButton();
		label1 = new JLabel();
		tradeMenu = new JButton();
		vacationStatus = new JButton();
		accountMenu = new JButton();

		//======== this ========
		setBackground(new Color(12, 115, 115));
		setPreferredSize(new Dimension(750, 600));
		setFont(new Font("Verdana", Font.PLAIN, 24));
		setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 204, 204)));
		setMaximumSize(new Dimension(750, 600));
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing.
		border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER
		, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font
		.BOLD ,12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (
		new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order"
		.equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

		//---- label2 ----
		label2.setText(bundle.getString("UserMainMenu.label2.text"));
		label2.setFont(new Font("Verdana", Font.PLAIN, 26));
		label2.setForeground(Color.white);

		//---- label3 ----
		label3.setText(bundle.getString("UserMainMenu.label3.text"));
		label3.setFont(new Font("Verdana", Font.PLAIN, 20));
		label3.setForeground(Color.white);
		label3.setBorder(null);

		//---- logOut ----
		logOut.setText(bundle.getString("UserMainMenu.logOut.text"));
		logOut.setFont(new Font("Verdana", Font.PLAIN, 16));
		logOut.setForeground(Color.white);
		logOut.setBackground(new Color(0, 102, 102));
		logOut.setOpaque(true);
		logOut.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
		logOut.addActionListener(e -> {
			logOutActionPerformed(e);
		});

		//---- label1 ----
		label1.setText(bundle.getString("UserMainMenu.label1.text_2"));
		label1.setForeground(Color.white);
		label1.setFont(new Font("Verdana", Font.PLAIN, 10));

		//---- tradeMenu ----
		tradeMenu.setText(bundle.getString("UserMainMenu.tradeMenu.text_2"));
		tradeMenu.setBackground(new Color(0, 51, 51));
		tradeMenu.setFont(new Font("Verdana", Font.BOLD, 20));
		tradeMenu.setForeground(Color.white);
		tradeMenu.setOpaque(true);
		tradeMenu.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
		tradeMenu.addActionListener(e -> tradeMenuActionPerformed(e));

		//---- vacationStatus ----
		vacationStatus.setText(bundle.getString("UserMainMenu.vacationStatus.text"));
		vacationStatus.setFont(new Font("Verdana", Font.PLAIN, 14));
		vacationStatus.setForeground(Color.white);
		vacationStatus.setBackground(new Color(12, 115, 115));
		vacationStatus.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
		vacationStatus.addActionListener(e -> vacationStatusActionPerformed(e));

		//---- accountMenu ----
		accountMenu.setText(bundle.getString("UserMainMenu.accountMenu.text"));
		accountMenu.setBackground(new Color(0, 51, 51));
		accountMenu.setForeground(Color.white);
		accountMenu.setFont(new Font("Verdana", Font.BOLD, 20));
		accountMenu.setOpaque(true);
		accountMenu.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
		accountMenu.addActionListener(e -> accountMenuActionPerformed(e));

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
							.addGap(14, 14, 14)
							.addComponent(vacationStatus, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
							.addComponent(label3))
						.addGroup(layout.createSequentialGroup()
							.addGap(76, 76, 76)
							.addGroup(layout.createParallelGroup()
								.addComponent(accountMenu, GroupLayout.PREFERRED_SIZE, 599, GroupLayout.PREFERRED_SIZE)
								.addComponent(tradeMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(0, 83, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
							.addGap(20, 20, 20)
							.addComponent(label1, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 446, Short.MAX_VALUE)))
					.addContainerGap())
				.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap(180, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addComponent(label2)
							.addGap(154, 154, 154))
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addComponent(logOut, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
							.addGap(232, 232, 232))))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
							.addGap(18, 18, 18)
							.addComponent(label3, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addGap(108, 108, 108))
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(vacationStatus, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(label1)
							.addGap(88, 88, 88)))
					.addComponent(label2)
					.addGap(31, 31, 31)
					.addComponent(tradeMenu, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(29, 29, 29)
					.addComponent(accountMenu, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
					.addComponent(logOut)
					.addGap(28, 28, 28))
		);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Josh Fox
	private JLabel label2;
	private JLabel label3;
	private JButton logOut;
	private JLabel label1;
	private JButton tradeMenu;
	private JButton vacationStatus;
	private JButton accountMenu;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
