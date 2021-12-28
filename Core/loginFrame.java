package Core;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


import javax.swing.GroupLayout.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.awt.*;

public class loginFrame extends JFrame {
	
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private String host = "localhost";
	private int port = 9999;
	private Socket socket;
	
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private String username;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginFrame frame = new loginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public loginFrame() {
		setTitle("CHAT APP");
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(51, 204, 255));
		
		
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setFont(new Font("Arial", Font.BOLD, 14));
		
		JLabel lbPassword = new JLabel("Password");
		lbPassword.setFont(new Font("Arial", Font.BOLD, 14));
		
		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		
		JPanel buttons = new JPanel();
		buttons.setBackground(new Color(255, 255, 255));
		
		JPanel notificationContainer = new JPanel();
		notificationContainer.setBackground(new Color(255, 255, 255));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(69)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lbPassword)
							.addGap(21)
							.addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lbUsername)
							.addGap(18)
							.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
					.addGap(81))
				.addComponent(notificationContainer, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(buttons, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGap(20)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbUsername)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbPassword))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(notificationContainer, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttons, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(22))
		);
		
		JLabel notification = new JLabel("");
		notification.setForeground(Color.RED);
		notification.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		notificationContainer.add(notification);
		
		JButton login = new JButton("Sign in");

		login.setForeground(Color.black);
		login.setBackground(new Color(255, 255, 255));
		login.setBorder(new EmptyBorder(0, 0, 0, 0));
		JButton register = new JButton("Sign Up");

		register.setForeground(Color.black);
		register.setBackground(new Color(255, 255, 255));
		register.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String response = Login(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
				
				if ( response.equals("Log in successful") ) {
					username = txtUsername.getText();
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								chatFrame frame = new chatFrame(username, dis, dos);
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					dispose();
				} else {
					login.setEnabled(false);
					register.setEnabled(false);
					txtPassword.setText("");
					notification.setText(response);
				}
			}
		});
		login.setEnabled(false);
		buttons.add(login);
		
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPasswordField confirm = new JPasswordField();
				
				
			    int action = JOptionPane.showConfirmDialog(null, confirm,"Comfirm your password",JOptionPane.OK_CANCEL_OPTION);
			    if (action == JOptionPane.OK_OPTION) {
			    	if (String.copyValueOf(confirm.getPassword()).equals(String.copyValueOf(txtPassword.getPassword()))) {
			    		String response = Signup(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
					
						if ( response.equals("Sign up successful") ) {
							username = txtUsername.getText();
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										int confirm = JOptionPane.showConfirmDialog(null, "Sign Up successfully!!!\nWelcome " + username + " to CHAT APP! ", "Sign successfully", JOptionPane.DEFAULT_OPTION);
										 
										chatFrame frame = new chatFrame(username, dis, dos);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							dispose();
						} else {
							login.setEnabled(false);
							register.setEnabled(false);
							txtPassword.setText("");
							notification.setText(response);
						}
					} else {
			    		notification.setText("Confirmed password does not match!");
			    	}
			    }
			}
		});
		register.setEnabled(false);
		buttons.add(register);
		
		JLabel headerContent = new JLabel("LOGIN");
		headerContent.setFont(new Font("Poor Richard", Font.BOLD, 22));
		headerContent.setForeground(Color.black);
		headerPanel.add(headerContent);
		contentPane.setLayout(gl_contentPane);
		
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtUsername.getText().isEmpty() || String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
					login.setEnabled(false);
					register.setEnabled(false);
				} else {
					login.setEnabled(true);
					register.setEnabled(true);
				}
			}
		});
		
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtUsername.getText().isEmpty() || String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
					login.setEnabled(false);
					register.setEnabled(false);
				} else {
					login.setEnabled(true);
					register.setEnabled(true);
				}
			}
		});
		
		this.getRootPane().setDefaultButton(login);
	}

	public String Login(String username, String password) {
		try {
			Connect();
			
			dos.writeUTF("Log in");
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.flush();
			
			String response = dis.readUTF();
			return response;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Network error: Log in fail";
		}
	}
	
	
	public String Signup(String username, String password) {
		try {
			Connect();
			
			dos.writeUTF("Sign up");
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.flush();
			
			String response = dis.readUTF();
			return response;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Network error: Sign up fail";
		}
	}
	
	public void Connect() {
		try {
			if (socket != null) {
				socket.close();
			}
			socket = new Socket(host, port);
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getUsername() {
		return this.username;
	}
}
