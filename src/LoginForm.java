import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;
import java.util.Stack;

public class LoginForm extends JDialog {
    private JTextField txtPassword;
    private JTextField txtUsername;
    private JButton btnReset;
    private JButton btnLogin;

    private JPanel loginPanel;

    public LoginForm(Frame parent) {
        super(parent);
        setTitle("Login Form");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 300));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        // When the login button is clicked, perform the following action:
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();

                // Get the user with the given username and password from the database
                user = getUser(username, password);

                if (user != null) {
                    // If the user is found, close the login dialog and display a success message
                    dispose();
                    JOptionPane.showMessageDialog(LoginForm.this,"Login Successful","Access Granted",JOptionPane.INFORMATION_MESSAGE);

                } else {
                    // If the user is not found, display an error message
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Username or Password Invalid",
                            "try again",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        // When the reset button is clicked, clear the username and password fields
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                txtUsername.setText("");
                txtPassword.setText("");

            }
        });

        setVisible(true);
    }
    public User user;

    private User getUser(String username, String password) {
        User user = null;

        final String db_url = "jdbc:ucanaccess://C://Users//lawre//Documents//Database1.accdb";

        try {
            Connection con = DriverManager.getConnection(db_url);

            Statement statement = con.createStatement();
            String sql = "SELECT * FROM LoginTbl WHERE username=? and password=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.password = resultSet.getString("password");
            }
            statement.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        LoginForm login = new LoginForm(null);

        User user = login.user;
        if (user != null) {
            System.out.println("Success");
        } else {
            System.out.println("Failed");
        }
    }

}