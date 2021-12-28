package Core;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;


public class ServerFrame extends JFrame {

	private JPanel contentPane;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public ServerFrame() {
		setTitle("Server");
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 308, 211);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(51, 204, 255));
		setContentPane(contentPane);
		
		
		JButton start = new JButton("START");
		start.setFont(new Font("Times New Roman", Font.BOLD, 14));
		start.setBorder(new EmptyBorder(0, 0, 0, 0));
		start.setBackground(new Color(0, 0, 0));
		start.setForeground(Color.white);
		start.setIcon(new javax.swing.ImageIcon(ServerFrame.class.getResource("/image/play.png")));
		
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 204, 255));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(90)
					.addComponent(start, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
					.addGap(81))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(43)
					.addComponent(start, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		JLabel text = new JLabel("Click here to start server");
		text.setFont(new Font("Tahoma", Font.PLAIN, 14));
		text.setBackground(new Color(70, 80, 70));
		panel.add(text);
		contentPane.setLayout(gl_contentPane);
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(){
					public void run() {
						try {
							new server();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				};
				t.start();

				start.setEnabled(false);
				text.setText("<html><font color='green'>SERVER START SUCCESSFULLY</font></html>");
			}
		});
	}

}
