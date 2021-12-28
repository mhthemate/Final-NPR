package Core;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
//import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;


public class chatFrame extends JFrame {


    private JButton btnSend;

    private JScrollPane chatPanel;

    private JLabel lbReceiver = new JLabel(" ");

    private JPanel contentPane;

    private JTextField txtMessage;

    private JTextPane chatWindow;
    JButton btnSelectAll;

    JComboBox<String> onlineUsers = new JComboBox<String>();


    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;

    private HashMap<String, JTextPane> chatWindows = new HashMap<String, JTextPane>();

    Thread receiver;

    StyledDocument messageDoc;
    Style userStyleSend;

    private void autoScroll() {
        chatPanel.getVerticalScrollBar().setValue(chatPanel.getVerticalScrollBar().getMaximum());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    private void newEmoji(String username, String emoji, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
        } else {
            doc = chatWindows.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, Color.gray);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }

        try {
            doc.insertString(doc.getLength(), username + ": ", userStyle);
        } catch (BadLocationException e) {
        }

        Style iconStyle = doc.getStyle("Icon style");
        if (iconStyle == null) {
            iconStyle = doc.addStyle("Icon style", null);
        }
        String emojiPath = emoji.substring(emoji.lastIndexOf("/icon"));
        StyleConstants.setIcon(iconStyle, new ImageIcon(getClass().getResource(emojiPath)));

        try {
            doc.insertString(doc.getLength(), "invisible text", iconStyle);
        } catch (BadLocationException e) {
        }

        
        try {
            doc.insertString(doc.getLength(), "\n", userStyle);
        } catch (BadLocationException e) {
        }

        autoScroll();
    }

    
    private void newMessage(String username, String message, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
        } else if (username.equals(" ")) {
            doc = chatWindows.get(" ").getStyledDocument();
        } else {
            doc = chatWindows.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, Color.gray);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }

        try {
            doc.insertString(doc.getLength(), username + ": ", userStyle);
        } catch (BadLocationException e) {
        }

        Style messageStyle = doc.getStyle("Message style");
        if (messageStyle == null) {
            messageStyle = doc.addStyle("Message style", null);
            StyleConstants.setForeground(messageStyle, Color.BLACK);
            StyleConstants.setBold(messageStyle, false);
        }

        try {
            doc.insertString(doc.getLength(), message + "\n", messageStyle);
        } catch (BadLocationException e) {
        }

        autoScroll();
    }

    
    private void groupMessage(String username, String message, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
        } else if (username.equals(" ")) {
            doc = chatWindows.get(" ").getStyledDocument();
        } else {
            doc = chatWindows.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, Color.gray);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }

        userStyleSend = userStyle;
        Style messageStyle = doc.getStyle("Message style");
        if (messageStyle == null) {
            messageStyle = doc.addStyle("Message style", null);
            StyleConstants.setForeground(messageStyle, Color.BLACK);
            StyleConstants.setBold(messageStyle, false);
        }
        messageDoc = doc;

        autoScroll();
    }

    
    public chatFrame(String username, DataInputStream dis, DataOutputStream dos) {
        setTitle("CHAT APP");
        this.username = username;
        this.dis = dis;
        this.dos = dos;
        receiver = new Thread(new Receiver(dis));
        receiver.start();

        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 586, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(51, 153, 255));
        setContentPane(contentPane);

        JPanel header = new JPanel();
        header.setBackground(new Color(51, 204, 255));

        txtMessage = new JTextField();
        txtMessage.setFont(new Font("Time New Roman", Font.BOLD, 11));

        txtMessage.setColumns(10);

        btnSend = new JButton("");
        btnSend.setEnabled(false);

        btnSend.setIcon(new ImageIcon(getClass().getResource("/icon/component/paper-plane.png")));
        btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));

        chatPanel = new JScrollPane();
        chatPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(51, 204, 255));

        JPanel emojis = new JPanel();
        emojis.setBackground(new Color(255, 255, 255));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addComponent(header, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(emojis, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED))
                                        .addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(header, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(emojis, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtMessage, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)))
        );

        JLabel smileIcon = new JLabel(new ImageIcon(chatFrame.class.getResource("/icon/emoji/smile.png")));
		smileIcon.addMouseListener(new IconListener(smileIcon.getIcon().toString()));
		emojis.add(smileIcon);
		
		
		JLabel bigSmileIcon = new JLabel(new ImageIcon(chatFrame.class.getResource("/icon/emoji/big-smile.png")));
		bigSmileIcon.addMouseListener(new IconListener(bigSmileIcon.getIcon().toString()));
		emojis.add(bigSmileIcon);
		
		
		JLabel loveIcon = new JLabel(new ImageIcon(chatFrame.class.getResource("/icon/emoji/heart.png")));
		loveIcon.addMouseListener(new IconListener(loveIcon.getIcon().toString()));
		emojis.add(loveIcon);
		
		JLabel sadIcon = new JLabel(new ImageIcon(chatFrame.class.getResource("/icon/emoji/sad.png")));
		sadIcon.addMouseListener(new IconListener(sadIcon.getIcon().toString()));
		emojis.add(sadIcon);
		
		JLabel suspiciousIcon = new JLabel(new ImageIcon(chatFrame.class.getResource("/icon/emoji/suspicious.png")));
		suspiciousIcon.addMouseListener(new IconListener(suspiciousIcon.getIcon().toString()));
		emojis.add(suspiciousIcon);
		
		JLabel angryIcon = new JLabel(new ImageIcon(chatFrame.class.getResource("/icon/emoji/angry.png")));
		angryIcon.addMouseListener(new IconListener(angryIcon.getIcon().toString()));
		emojis.add(angryIcon);
		

//        JLabel userImage = new JLabel(new ImageIcon(getClass().getResource("/icon/component/avatar.png")));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(51, 204, 255));
        
        JLabel lblNewLabel_1 = new JLabel("Online Users");      
        lblNewLabel_1.setIcon(new ImageIcon(getClass().getResource("/icon/component/1280px-Green_Light.svg.png")));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));

        btnSelectAll = new JButton("Group chat");
        btnSelectAll.setIcon(new ImageIcon(getClass().getResource("/icon/component/group.png")));
        btnSelectAll.setBorder(new EmptyBorder(0, 0, 0, 0));
        btnSelectAll.setBackground(new Color(51, 204, 255));
        btnSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMessage.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nothing to send!");
                } else {

                    lbReceiver.setText((String) onlineUsers.getSelectedItem());
                    int size = onlineUsers.getItemCount();
                    for (int i = 0; i < size; i++) {
                        String member = onlineUsers.getItemAt(i);
                        try {
                            dos.writeUTF("Text");
                            dos.writeUTF(member);
                            dos.writeUTF("@" + txtMessage.getText());
                            dos.flush();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            newMessage("ERROR", "Network error!", true);
                        }

                    }

                    if (chatWindows.get(" ") != null) {
                        lbReceiver.setText(" ");
                        groupMessage(username, txtMessage.getText(), true);
                        Style messageStyle = messageDoc.getStyle("Message style");
                        try {
                           
                            messageDoc.insertString(messageDoc.getLength(), username + ": ", userStyleSend);
                            messageDoc.insertString(messageDoc.getLength(), txtMessage.getText() + "\n", messageStyle);
                            txtMessage.setText("");
                        } catch (BadLocationException ex) {
                            Logger.getLogger(chatFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JTextPane temp = new JTextPane();
                        temp.setFont(new Font("Time New Roman", Font.PLAIN, 14));
                        temp.setEditable(false);
                        chatWindows.put(" ", temp);
                        lbReceiver.setText(" ");
                        
                        groupMessage(username, txtMessage.getText(), true);
                        Style messageStyle = messageDoc.getStyle("Message style");
                        try {
                            
                            messageDoc.insertString(messageDoc.getLength(), username + ": ", userStyleSend);
                            messageDoc.insertString(messageDoc.getLength(), txtMessage.getText() + "\n", messageStyle);
                            txtMessage.setText("");
                        } catch (BadLocationException ex) {
                            Logger.getLogger(chatFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }

        });

        GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
        gl_leftPanel.setHorizontalGroup(
                gl_leftPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addGap(10)
//                                .addComponent(userImage, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                .addGap(10))
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addGap(28)
                                .addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10))
                        .addGroup(Alignment.TRAILING, gl_leftPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(onlineUsers, 0, 50, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(Alignment.LEADING, gl_leftPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnSelectAll,GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                .addContainerGap(10, Short.MAX_VALUE))
        );
        
        onlineUsers.setBorder(new EmptyBorder(0, 0, 0, 0));
        onlineUsers.setBackground(new Color(255, 235, 242));
        onlineUsers.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    lbReceiver.setText((String) onlineUsers.getSelectedItem());
                    if (chatWindow != chatWindows.get(lbReceiver.getText())) {
                        txtMessage.setText("");
                        chatWindow = chatWindows.get(lbReceiver.getText());
                        chatPanel.setViewportView(chatWindow);
                        chatPanel.validate();
                    }

                    if (lbReceiver.getText().isEmpty()) {
                        btnSend.setEnabled(false);

                    } else {
                        btnSend.setEnabled(true);
                    }
                } else {
                    lbReceiver.setText(" ");
                    if (chatWindow != chatWindows.get(lbReceiver.getText())) {
                        txtMessage.setText("");
                        chatWindow = chatWindows.get(lbReceiver.getText());
                        chatPanel.setViewportView(chatWindow);
                        chatPanel.validate();
                        btnSend.setEnabled(true);
                    }
                }

            }
        });

        gl_leftPanel.setVerticalGroup(
                gl_leftPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addGap(5)
//                                .addComponent(userImage)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                .addGap(41)
                                .addComponent(lblNewLabel_1)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(onlineUsers, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(btnSelectAll, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(181, Short.MAX_VALUE))
        );

        JLabel lbUsername = new JLabel(this.username);
        lbUsername.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(lbUsername);
        leftPanel.setLayout(gl_leftPanel);

        JLabel headerContent = new JLabel("CHAT APP");
        headerContent.setFont(new Font("Poor Richard", Font.BOLD, 24));
        headerContent.setForeground(Color.white);
        header.add(headerContent);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(240, 213, 223));
        chatPanel.setColumnHeaderView(usernamePanel);

//        lbReceiver.setFont(new Font("Arial", Font.BOLD, 16));
//        lbReceiver.setIcon(new ImageIcon(getClass().getResource("/icon/component/avatar.png")));
        usernamePanel.add(lbReceiver);

        chatWindows.put(" ", new JTextPane());
        chatWindow = chatWindows.get(" ");
        chatWindow.setFont(new Font("Arial", Font.PLAIN, 14));
        chatWindow.setEditable(false);

        chatPanel.setViewportView(chatWindow);
        contentPane.setLayout(gl_contentPane);

        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (txtMessage.getText().isEmpty() || lbReceiver.getText().isEmpty()) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    dos.writeUTF("Text");
                    dos.writeUTF(lbReceiver.getText());
                    dos.writeUTF(txtMessage.getText());
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    newMessage("ERROR", "Network error!", true);
                }

                newMessage(username, txtMessage.getText(), true);
                txtMessage.setText("");
            }
        });

        this.getRootPane().setDefaultButton(btnSend);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                try {
                    dos.writeUTF("Log out");
                    dos.flush();

                    try {
                        receiver.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    if (dos != null) {
                        dos.close();
                    }
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    
    class Receiver implements Runnable {

        private DataInputStream dis;

        public Receiver(DataInputStream dis) {
            this.dis = dis;
        }

        @Override
        public void run() {
            try {

                while (true) {
                    
                    String method = dis.readUTF();

                    if (method.equals("Text")) {
                        
                        String sender = dis.readUTF();
                        String message = dis.readUTF();

                        if (message.contains("@")) {
                            if (chatWindows.get(" ") != null) {
                                lbReceiver.setText(" ");
                                groupMessage(lbReceiver.getText(), message.substring(1), false);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    
                                    messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
                                    messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n", messageStyle);
                                    txtMessage.setText("");
                                } catch (BadLocationException ex) {
                                    Logger.getLogger(chatFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                JTextPane temp = new JTextPane();
                                temp.setFont(new Font("Arial", Font.PLAIN, 14));
                                temp.setEditable(false);
                                chatWindows.put(" ", temp);
                                lbReceiver.setText(" ");
                                
                                groupMessage(lbReceiver.getText(), message.substring(1), false);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    
                                    messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
                                    messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n", messageStyle);
                                    txtMessage.setText("");
                                } catch (BadLocationException ex) {
                                    Logger.getLogger(chatFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        } else {
                           
                            newMessage(sender, message, false);
                        }
                    } else if (method.equals("Emoji")) {
                        
                        String sender = dis.readUTF();
                        String emoji = dis.readUTF();

                       
                        newEmoji(sender, emoji, false);
                    } else if (method.equals("Online users")) {
                        
                        String[] users = dis.readUTF().split(",");
                        onlineUsers.removeAllItems();

                        String chatting = lbReceiver.getText();

                        boolean isChattingOnline = false;

                        for (String user : users) {
//                            group.add(user);
                            if (user.equals(username) == false) {
                                onlineUsers.addItem(user);
                                if (chatWindows.get(user) == null) {
                                    JTextPane temp = new JTextPane();
                                    temp.setFont(new Font("Arial", Font.PLAIN, 14));
                                    temp.setEditable(false);
                                    chatWindows.put(user, temp);
                                }
                            }
                            if (chatting.equals(user)) {
                                isChattingOnline = true;
                            }
                            if (chatting.equals("All")) {
                                isChattingOnline = true;
                            }
                        }

                        if (isChattingOnline == false) {
                           onlineUsers.setSelectedItem(" ");
                            JOptionPane.showMessageDialog(null, chatting + " is offline!\nYou will be redirect to default chat window");
                        } else {
                            onlineUsers.setSelectedItem(chatting);
                        }

                        onlineUsers.validate();
                    } else if (method.equals("Safe to leave")) {
                        
                        break;
                    }

                }

            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class IconListener extends MouseAdapter {

        String emoji;

        public IconListener(String emoji) {
            this.emoji = emoji;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (txtMessage.isEnabled() == true) {

                try {
                    dos.writeUTF("Emoji");
                    dos.writeUTF(lbReceiver.getText());
                    dos.writeUTF(this.emoji);
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    newMessage("ERROR", "Network error!", true);
                }

                newEmoji(username, this.emoji, true);
            }
        }
    }
}
