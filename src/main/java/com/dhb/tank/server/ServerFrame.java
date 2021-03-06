package com.dhb.tank.server;

import com.dhb.tank.comms.Constants;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {

	public static final ServerFrame INSTANCE = new ServerFrame();

	Button btnStart = new Button("start");
	TextArea taLeft = new TextArea();
	TextArea taRight = new TextArea();

	Server server = new Server();

	public ServerFrame() {
		this.setSize(1600, 600);
		this.setLocation(300, 30);
		this.add(btnStart, BorderLayout.NORTH);
		Panel p = new Panel(new GridLayout(1, 2));
		p.add(taLeft);
		p.add(taRight);
		this.add(p);
		btnStart.setFont(new Font("verderna", Font.PLAIN, 20));
		taLeft.setFont(new Font("verderna", Font.PLAIN, 18));
		taRight.setFont(new Font("verderna", Font.PLAIN, 18));
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.btnStart.addActionListener((e) -> {
			Thread t = new Thread(() -> {
				server.serverStart();
			});
			t.setDaemon(true);
			t.start();
		});
	}

	public static void main(String[] args) {
		ServerFrame.INSTANCE.setVisible(true);
	}

	public static void exit() {
		ServerFrame frame = ServerFrame.INSTANCE;
		frame.server.serverStop();
	}

	public void updateServerMsg(String msg) {
		String str = taLeft.getText();
		if(!"".equals(str)) {
			this.taLeft.setText(taLeft.getText() + Constants.LINE_SEP + msg);
		}else {
			this.taLeft.setText(msg);
		}
	}

	public void updateClientMsg(String msg) {
		String str = taRight.getText();
		if(!"".equals(str)) {
			this.taRight.setText(taRight.getText() + Constants.LINE_SEP + msg);
		}else {
			this.taRight.setText(msg);
		}
	}

}
