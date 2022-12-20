

package gui.userUI;

import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import gui.loginUI.LoginPage;
import gui.loginUI.MainPage;
import gui.util.DisplayUtil;
import accounts.UserAccount;
import accounts.UserManager;
import controllers.MainController;
import controllers.UserManagerBuilder;
import controllers.UserMenuController;
import items.Item;
import items.ItemInventory;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * @author Namratha Anil
 */
public class WishlistMenu extends JPanel {
    private MainPage mainFrame;
    public UserManagerBuilder userManagerBuilder;
    public MainController mainController;
    private DefaultListModel<String> sysInventoryListModel = new DefaultListModel<String>();
    private DefaultListModel<String> wishListModel = new DefaultListModel<String>();
    private String mode;
    
    public WishlistMenu(String mode) {
        initComponents();
    	this.mode = mode;
        sysInvList.setModel(sysInventoryListModel);
        wishList.setModel(wishListModel);
        this.userManagerBuilder = new UserManagerBuilder(this.mode);
    }


    public void setMainFrame(MainPage mainFrame){
        this.mainFrame = mainFrame;
        this.mainController = mainFrame.getMainController();
    }

    private void backActionPerformed(ActionEvent e) {
        mainFrame.setView("UserAccountMenu");
    }

    private void addActionPerformed(ActionEvent e) {
        UserManager userManager = this.mainController.getUserManager();
        UserAccount currentUser = userManager.getUser (LoginPage.getCurrentUserName());
        UserMenuController userMenuController = new UserMenuController(currentUser, this.mode);
        Integer itemId;
        try{
             itemId = Integer.valueOf(itemID.getText());
        }catch (NumberFormatException  ex) {
            JOptionPane.showMessageDialog(this.mainFrame, "Item Id must be a valid integer number.");
            return;
        }
        userMenuController.addToMyWishList(itemId,this.mainController.getItemInventory());
        JOptionPane.showMessageDialog(this.mainFrame, "Selected item was added to your wishlist");
    }

    private void systemInventoryBActionPerformed(ActionEvent e) {
        sysInventoryListModel.clear();
        ItemInventory itemInventory = this.mainController.getItemInventory();
        List<Item> approvedItems = itemInventory.getApprovedItems();
        String itemInfo;
        for (Item item : approvedItems) {
            if (!item.getOwnerID().equals(LoginPage.getCurrentUserName())){
                itemInfo = DisplayUtil.getItemDisplay(item);
                sysInventoryListModel.addElement(itemInfo);
            }
        }
    }

    private void viewWishlistActionPerformed(ActionEvent e) {
        wishListModel.clear();
        UserAccount user = this.mainController.getUserManager().getUser(LoginPage.getCurrentUserName());
        List<Item> wishListItems = user.getWishlist();
        String itemInfo;
        for (Item item : wishListItems) {
            itemInfo = DisplayUtil.getItemDisplay(item);
            wishListModel.addElement(itemInfo);
        }
    }

    private void deleteActionPerformed(ActionEvent e) {
        UserMenuController userMenuController = this.mainController.getUserMenuController();
        ItemInventory itemInventory = this.mainController.getItemInventory();
        Integer itemId;
        try {
            itemId = Integer.valueOf(itemID.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.mainFrame, "Item Id must be a valid integer number.");
            return;
        }
        if (!(itemInventory.getItemWithID(itemId) == null)){
            userMenuController.removeFromWishList(itemId, this.mainController.getItemInventory());
        JOptionPane.showMessageDialog(this.mainFrame, "Selected item was removed from wishlist");
        }
        else {
            JOptionPane.showMessageDialog(this.mainFrame, "Item doesn't exist. Please enter correct itemID");

        }

    }

    private void initComponents() {
        // Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Namratha Anil
        ResourceBundle bundle = ResourceBundle.getBundle("gui.locales.WishListMenu");
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        wishList = new JList();
        back = new JButton();
        itemID = new JTextField();
        delete = new JButton();
        scrollPane2 = new JScrollPane();
        sysInvList = new JList();
        label3 = new JLabel();
        add = new JButton();
        label4 = new JLabel();
        label5 = new JLabel();
        viewWishlist = new JButton();
        systemInventoryB = new JButton();

        //======== this ========
        setBackground(new Color(12, 115, 115));
        setFont(new Font("Verdana", Font.PLAIN, 13));
        setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
        setMaximumSize(new Dimension(750, 600));
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing
        . border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e" , javax. swing .border . TitledBorder
        . CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .
        awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder () ) )
        ;  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
        ) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } )
        ;

        //---- label1 ----
        label1.setText(bundle.getString("WishlistMenu.label1.text"));
        label1.setFont(new Font("Verdana", Font.PLAIN, 12));
        label1.setForeground(new Color(153, 255, 255));

        //======== scrollPane1 ========
        {

            //---- wishList ----
            wishList.setFont(new Font("Verdana", Font.PLAIN, 16));
            wishList.setForeground(Color.white);
            wishList.setBorder(null);
            wishList.setBackground(Color.darkGray);
            scrollPane1.setViewportView(wishList);
        }

        //---- back ----
        back.setText(bundle.getString("WishlistMenu.back.text"));
        back.setForeground(Color.white);
        back.setFont(new Font("Verdana", Font.PLAIN, 12));
        back.setBackground(Color.darkGray);
        back.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
        back.addActionListener(e -> backActionPerformed(e));

        //---- itemID ----
        itemID.setForeground(Color.white);
        itemID.setFont(new Font("Verdana", Font.PLAIN, 15));
        itemID.setBackground(new Color(12, 115, 115));
        itemID.setBorder(new MatteBorder(0, 0, 2, 0, Color.white));

        //---- delete ----
        delete.setText(bundle.getString("WishlistMenu.delete.text"));
        delete.setFont(new Font("Verdana", Font.PLAIN, 14));
        delete.setForeground(Color.white);
        delete.setBackground(new Color(0, 51, 51));
        delete.setOpaque(true);
        delete.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
        delete.addActionListener(e -> deleteActionPerformed(e));

        //======== scrollPane2 ========
        {

            //---- sysInvList ----
            sysInvList.setFont(new Font("Verdana", Font.PLAIN, 16));
            sysInvList.setForeground(Color.white);
            sysInvList.setBorder(null);
            sysInvList.setBackground(Color.darkGray);
            scrollPane2.setViewportView(sysInvList);
        }

        //---- label3 ----
        label3.setText(bundle.getString("WishlistMenu.label3.text"));
        label3.setForeground(Color.white);
        label3.setFont(new Font("Verdana", Font.PLAIN, 16));

        //---- add ----
        add.setText(bundle.getString("WishlistMenu.add.text"));
        add.setFont(new Font("Verdana", Font.PLAIN, 14));
        add.setForeground(Color.white);
        add.setBackground(new Color(0, 51, 51));
        add.setOpaque(true);
        add.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
        add.addActionListener(e -> addActionPerformed(e));

        //---- label4 ----
        label4.setText(bundle.getString("WishlistMenu.label4.text"));
        label4.setFont(new Font("Verdana", Font.PLAIN, 14));
        label4.setForeground(Color.white);

        //---- label5 ----
        label5.setText(bundle.getString("WishlistMenu.label5.text"));
        label5.setFont(new Font("Verdana", Font.PLAIN, 14));
        label5.setForeground(Color.white);

        //---- viewWishlist ----
        viewWishlist.setText(bundle.getString("WishlistMenu.viewWishlist.text"));
        viewWishlist.setForeground(Color.white);
        viewWishlist.setFont(new Font("Verdana", Font.PLAIN, 12));
        viewWishlist.setBackground(new Color(0, 51, 51));
        viewWishlist.setOpaque(true);
        viewWishlist.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
        viewWishlist.addActionListener(e -> viewWishlistActionPerformed(e));

        //---- systemInventoryB ----
        systemInventoryB.setText(bundle.getString("WishlistMenu.systemInventoryB.text"));
        systemInventoryB.setFont(new Font("Verdana", Font.PLAIN, 12));
        systemInventoryB.setForeground(Color.white);
        systemInventoryB.setBackground(new Color(0, 51, 51));
        systemInventoryB.setOpaque(true);
        systemInventoryB.setBorder(new MatteBorder(1, 2, 2, 1, new Color(0, 204, 204)));
        systemInventoryB.addActionListener(e -> systemInventoryBActionPerformed(e));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(42, 42, 42)
                                    .addComponent(label3, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(34, 34, 34)
                                    .addGroup(layout.createParallelGroup()
                                        .addComponent(label4)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(34, 34, 34)
                                    .addGroup(layout.createParallelGroup()
                                        .addComponent(itemID, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(viewWishlist, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE))))
                            .addGap(41, 41, 41)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(label5)
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                .addComponent(add, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(delete, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(systemInventoryB, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                            .addGap(0, 39, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap(638, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup()
                                .addComponent(label1, GroupLayout.Alignment.TRAILING)
                                .addComponent(back, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addGap(43, 43, 43)
                            .addComponent(label3)
                            .addGap(18, 18, 18)
                            .addComponent(itemID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 53, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(add, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(delete, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(label5, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(label4)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(viewWishlist, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        .addComponent(systemInventoryB, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(back)
                    .addContainerGap(13, Short.MAX_VALUE))
        );
        // End of component initialization  //GEN-END:initComponents
    }

    // Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Namratha Anil
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JList wishList;
    private JButton back;
    private JTextField itemID;
    private JButton delete;
    private JScrollPane scrollPane2;
    private JList sysInvList;
    private JLabel label3;
    private JButton add;
    private JLabel label4;
    private JLabel label5;
    private JButton viewWishlist;
    private JButton systemInventoryB;
    // End of variables declaration  //GEN-END:variables
}
