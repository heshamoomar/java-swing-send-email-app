import javax.swing.*;
import javax.swing.border.EtchedBorder; // for changing the border around the education text area, but I don't need it anymore
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;  //
import java.util.Properties;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
// mail and activation libraries had to be downloaded and manually added

public class SignUpForm extends JFrame implements WindowListener {
    // I used this when trying to change fonts
    //String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    // don't forget to initialize Java variables!!!
    // if I use a separate class for handling the email actions I need to set all text fields below as static
    // in order to pass them between classes
    // but in same class static is not required
    protected  JLabel firstNameLabel, lastNameLabel, toEmailLabel, fromEmailLabel, passwordLabel, educationLabel;
    protected static JTextField firstNameTextField;
    protected static JTextField lastNameTextField;
    protected static JTextField toEmailTextField, fromEmailTextField;
    protected static JPasswordField passwordField;
    protected static JTextArea educationTextArea;
    protected JPanel mainPanel, buttonPanel, firstNamePanel, lastNamePanel, toEmailPanel, fromEmailPanel, passwordPanel, educationPanel;
    protected final JButton button;
    protected JScrollPane scrollPane; // could use this for adding scroll bars but not required in assignment
    protected Image App_icon = Toolkit.getDefaultToolkit().getImage("ImageAssets/736543-200.png"),
            Education_icon = Toolkit.getDefaultToolkit().getImage("ImageAssets/open-book-icon-free-vector-transparent (Custom).png");
    protected Font font = new Font("Times New Roman", Font.BOLD, 14);
    SignUpForm(){
        setTitle("SignUp Form");
        //setLayout(new FlowLayout(FlowLayout.CENTER,0,13)); // I used this before, but now I added a main panel which is Flow Layout by default
        addWindowListener(this);
        mainPanel=new JPanel();
        //mainPanel.setBackground(Color.lightGray);
        setDefaultLookAndFeelDecorated(true); //  to match the windows11 style
        setLocationRelativeTo(null); // center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,350);
        setResizable(false);
        setIconImage(App_icon);

        firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(font);
        firstNameTextField = new JTextField();
        firstNameTextField.setColumns(16); // only function that works for me
        firstNameTextField.setToolTipText("Enter Your First Name");
        //firstNameTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)); // already selected for all fields
        firstNamePanel = new JPanel(new GridLayout());
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameTextField);
        //firstNamePanel.setBackground(Color.lightGray);
        mainPanel.add(firstNamePanel);

        lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(font);
        lastNameTextField = new JTextField();
        lastNameTextField.setColumns(16);
        lastNameTextField.setToolTipText("Enter Your Last Name");
        lastNamePanel = new JPanel(new GridLayout());
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameTextField);
        //lastNamePanel.setBackground(Color.lightGray);
        //lastNamePanel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        mainPanel.add(lastNamePanel);

        toEmailLabel = new JLabel("To:");
        toEmailLabel.setFont(font);
        toEmailTextField = new JTextField();
        toEmailTextField.setColumns(16);
        toEmailTextField.setToolTipText("Enter Receiver Email Address");
        toEmailPanel = new JPanel(new GridLayout());
        toEmailPanel.add(toEmailLabel);
        toEmailPanel.add(toEmailTextField);
        //emailPanel.setBackground(Color.lightGray);
        mainPanel.add(toEmailPanel);

        fromEmailLabel = new JLabel("From:");
        fromEmailLabel.setFont(font);
        fromEmailTextField = new JTextField();
        fromEmailTextField.setColumns(16);
        fromEmailTextField.setToolTipText("Enter Sender Email Address");
        fromEmailPanel = new JPanel(new GridLayout());
        fromEmailPanel.add(fromEmailLabel);
        fromEmailPanel.add(fromEmailTextField);
        //emailPanel.setBackground(Color.lightGray);
        mainPanel.add(fromEmailPanel);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(font);
        passwordField = new JPasswordField();
        passwordField.setColumns(16);
        passwordField.setToolTipText("Enter Password");
        passwordPanel = new JPanel(new GridLayout());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        //passwordPanel.setBackground(Color.lightGray);
        mainPanel.add(passwordPanel);

        educationLabel = new JLabel("Education:");
        educationLabel.setFont(font);
        educationLabel.setIcon(new ImageIcon(Education_icon));
        educationTextArea = new JTextArea();
        educationTextArea.setColumns(16);
        educationTextArea.setRows(5);
        //educationTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
        educationTextArea.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        educationTextArea.setToolTipText("Enter Your Education Status");
        educationPanel = new JPanel(new GridLayout());
        educationPanel.add(educationLabel);
        educationPanel.add(educationTextArea);
        educationTextArea.setLineWrap(true);
        //scrollPane = new JScrollPane(educationTextArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //educationPanel.add(scrollPane);
        //educationPanel.setBackground(Color.lightGray);
        mainPanel.add(educationPanel);

        button = new JButton("Submit");
        buttonPanel=new JPanel();
        JLabel label=new JLabel("                                                          "); // I added this to move button
        buttonPanel.add(label);
        buttonPanel.add(button);
        mainPanel.add(buttonPanel);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // here I will set up the functionality of the button
                // it gets the email information from the JFrame, checks for exceptions in all text fields and sends the email
                String to = toEmailTextField.getText();
                String from = fromEmailTextField.getText();
                String password = passwordField.getText();  // using getText() with passwords is deprecated and I tried to use the new getPassword() method instead, but it doesn't work for me
                String subject = firstNameTextField.getText()+' '+lastNameTextField.getText();
                String body = educationTextArea.getText();

                // Set up properties for the SMTP server
                // I could also just use my local host instead
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                // Authenticate with the SMTP server
                Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message.setSubject(subject);
                    message.setText(body);
                    Transport.send(message);
                    JOptionPane.showMessageDialog(SignUpForm.this, "Email sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ex)
                {
                    // handling all the error cases
                    ex.printStackTrace();
                    if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() || to.isEmpty() || from.isEmpty() || password.isEmpty()|| body.isEmpty()) {
                        JOptionPane.showMessageDialog(SignUpForm.this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (firstNameTextField.getText().length() < 3 || lastNameTextField.getText().length() < 3) {
                        JOptionPane.showMessageDialog(SignUpForm.this, "First name and last name must be at least 3 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (!to.contains("@")||!from.contains("@")) {
                        JOptionPane.showMessageDialog(SignUpForm.this, "both Emails must contain the '@' symbol.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (!password.matches(".*[A-Z].*")) {
                        JOptionPane.showMessageDialog(SignUpForm.this, "Password must contain at least one capital letter.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        // Show an error message to the user
                    JOptionPane.showMessageDialog(SignUpForm.this, "Failed to send email. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                     }
                }

            }
        });
        button.addMouseListener(new MouseListener() {
            // have to implement all mouse interface methods
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // to change button color when hovering over
                button.setBackground(Color.getHSBColor(94, 20, 37)); // hue saturation brightness
                // (94, 20, 37) (94, 19, 37)
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor ( "Panel.background" ));
            }
        });


        add(mainPanel);
        setVisible(true);
    }

    // have to implement all the methods in the window listener interface as well
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // check message for existing
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Confirmation",JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
            System.exit(0);
        else
            this.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
