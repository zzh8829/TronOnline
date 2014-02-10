package ca.zihao.tronol.client;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * File: LoginPanel.java
 * Date: 08/02/14
 * Time: 1:01 PM
 * Zihao Zhang @ zihao.ca
 */


public class LoginPanel extends JPanel{

    private JPanel mainPanel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton helpButton;
    private JButton aboutButton;
    private String username;
    private String password;

    public LoginPanel() {
        JLabel backgroundLabel = new JLabel(new ImageIcon(ImageLoader.load("background.jpg")));
        JLabel logoLabel = new JLabel(new ImageIcon(ImageLoader.load("logo.png")));

        ExLabel usernameLabel = new ExLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setOutlineColor(Color.DARK_GRAY);
        usernameLabel.setStroke(new BasicStroke(3f));

        usernameField = new JTextField(20);

        ExLabel passwordLabel = new ExLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setOutlineColor(Color.DARK_GRAY);
        passwordLabel.setStroke(new BasicStroke(3f));

        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        helpButton = new JButton("Help");
        aboutButton = new JButton("About");

        logoLabel.setBounds(50,50,700,100);
        usernameLabel.setBounds(210,200,110,50);
        usernameField.setBounds(320,200,280,50);
        passwordLabel.setBounds(210,280,110,50);
        passwordField.setBounds(320,280,280,50);
        loginButton.setBounds(200,360,190,50);
        registerButton.setBounds(410,360,190,50);
        helpButton.setBounds(200,440,190,50);
        aboutButton.setBounds(410,440,190,50);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.add(logoLabel);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);
        mainPanel.add(helpButton);
        mainPanel.add(aboutButton);

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.add(backgroundLabel, BorderLayout.CENTER);

        mainPanel.setBounds(0,0,800,600);
        mainPanel.setOpaque(false);
        backgroundPanel.setBounds(0,0,800,600);
        backgroundPanel.setOpaque(true);

        setLayout(null);
        add(mainPanel);
        add(backgroundPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                username = usernameField.getText();
                password = passwordField.getText();
                if(username.length() > 20 || username.length() < 1) {
                    JOptionPane.showMessageDialog(null,"Please choose a username between 1-20 characters!","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(password.length() > 20 || password.length() < 1) {
                    JOptionPane.showMessageDialog(null,"Please choose a password between 1-20 characters","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                disableGUI();
                new SwingWorker<Void,Void>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        Main.client.login(username,password);
                        return null;
                    }
                    @Override
                    protected void done() {
                        enableGUI();
                    }
                }.execute();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                username = usernameField.getText();
                password = passwordField.getText();
                if(username.length() > 20 || username.length() < 1) {
                    JOptionPane.showMessageDialog(null,"Please choose a username between 1-20 characters!","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(password.length() > 20 || password.length() < 1) {
                    JOptionPane.showMessageDialog(null,"Please choose a password between 1-20 characters","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                disableGUI();
                new SwingWorker<Void,Void>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        Main.client.register(username,password);
                        return null;
                    }
                    @Override
                    protected void done() {
                        enableGUI();
                    }
                }.execute();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JEditorPane ep = new JEditorPane("text/html",
                        "<html>" +
                                "0. <a href=\"http://www.free-iqtest.net\">Test Your IQ</a><br>" +
                                "1. Register account<br>" +
                                "2. Login account<br>" +
                                "3. Create/Join room<br>" +
                                "4. Play Tron Online!" +
                                "</html>");
                ep.addHyperlinkListener(new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
                        if(hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)){
                            try {
                                Desktop.getDesktop().browse(new URI("http://www.free-iqtest.net"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                ep.setEditable(false);
                JOptionPane.showMessageDialog(null,ep,"Help",JOptionPane.QUESTION_MESSAGE);
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JEditorPane ep = new JEditorPane("text/html",
                        "<html>" +
                                "Website: <a href=\"http://tronol.zihao.ca\">tronol.zihao.ca</a><br>" +
                                "Developer: Zihao Zhang<br>" +
                                "Version: v1.1<br>" +
                                "*Glorious C++ Master Race*<br>" +
                                "-Filthy Java Peasant-" +
                                "</html>");
                ep.addHyperlinkListener(new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
                        if(hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)){
                            try {
                                Desktop.getDesktop().browse(new URI("http://tronol.zihao.ca"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                ep.setEditable(false);
                JOptionPane.showMessageDialog(null,ep,"About",JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    void disableGUI(){
        for(Component c: mainPanel.getComponents())
            c.setEnabled(false);
    }

    void enableGUI(){
        for(Component c: mainPanel.getComponents())
            c.setEnabled(true);
    }
}
