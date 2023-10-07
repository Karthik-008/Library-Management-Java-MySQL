//import java.io.*;
//import javax.imageio.plugins.tiff.ExifGPSTagSet;
import javax.swing.*;
//import com.mysql.cj.protocol.Resultset;

//import java.awt.*;
import java.awt.event.*;

import java.sql.*;

//import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//import java.util.ArrayList;
import java.util.Date;
//import java.util.Locale;
import java.util.concurrent.TimeUnit;

import net.proteanit.sql.DbUtils;

public class LibraryUI extends JFrame {

    static String sql = "";
    static Connection con = Connect.getConnection();

    public static void login() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel l1, l2;

        l1 = new JLabel("Username");
        l1.setBounds(30, 15, 100, 30);

        l2 = new JLabel("Password");
        l2.setBounds(30, 50, 100, 30);

        JTextField f_user = new JTextField();
        f_user.setBounds(110, 15, 200, 30);

        JPasswordField f_pass = new JPasswordField();
        f_pass.setBounds(110, 50, 200, 30);

        JButton login_but = new JButton("Login");
        login_but.setBounds(130, 90, 80, 25);

        login_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String username = f_user.getText();
                String password = new String(f_pass.getPassword());

                if (username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Username!");

                } else if (password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Password!");

                } else {
                    try {
                        // Connection con = Connect.getConnection();

                        Statement st = con.createStatement();

                        sql = "select * from users where username = '" + username + "' and password = '" + password
                                + "'";

                        ResultSet rs = st.executeQuery(sql);

                        if (rs.next() == false) {
                            System.out.println("No User");
                            JOptionPane.showMessageDialog(null, "Wrong username/password");

                        }

                        else {
                            frame.dispose();
                            Statement st1 = con.createStatement();

                            sql = "select * from users where username = '" + username + "' and password = '" + password
                                    + "'";

                            ResultSet rs1 = st1.executeQuery(sql);
                            // rs.beforeFirst();

                            while (rs1.next()) {
                                String admin = rs1.getString("admin");
                                int uid = rs1.getInt("uid");

                                if (admin.equals("1")) {
                                    admin_menu();
                                } else {
                                    user_menu(uid);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        frame.add(f_user);
        frame.add(f_pass);
        frame.add(l1);
        frame.add(l2);
        frame.add(login_but);

        frame.setSize(400, 180);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    public static void user_menu(int uid) {
        JFrame frame = new JFrame("User Functions");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton view_but = new JButton("View Books");
        view_but.setBounds(20, 20, 120, 25);

        view_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Books Available");

                // Connection con = Connect.getConnection();

                sql = "select * from books";
                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);

                    JTable book_list = new JTable();
                    book_list.setModel(DbUtils.resultSetToTableModel(rs));

                    JScrollPane scrollPane = new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(800, 400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });

        JButton my_book = new JButton("My Books");
        my_book.setBounds(150, 20, 120, 25);
        my_book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("My Books");
                int uid_int = uid;

                sql = "select distinct issued.*,books.bname,books.genre,books.price from issued,books "
                        + "where ((issued.uid=" + uid_int
                        + ") and (books.bid in (select bid from issued where issued.uid=" + uid_int
                        + "))) group by iid";
                String sql1 = "select * from issued where uid=" + uid_int;

                try {
                    Statement stmt = con.createStatement();
                    // ArrayList books_list = new ArrayList<>();

                    ResultSet rs = stmt.executeQuery(sql1);
                    JTable book_list = new JTable();
                    book_list.setModel(DbUtils.resultSetToTableModel(rs));
                    JScrollPane scrollPane = new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(800, 400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });

        frame.add(my_book);
        frame.add(view_but);
        frame.setSize(300, 100);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void admin_menu() {
        JFrame frame = new JFrame("Admin Functions");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton view_but = new JButton("View Books");
        view_but.setBounds(20, 20, 120, 25);
        view_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Books Available");

                sql = "select * from books";

                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);

                    JTable book_list = new JTable();
                    book_list.setModel(DbUtils.resultSetToTableModel(rs));
                    JScrollPane scrollPane = new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(800, 400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });

        JButton users_but = new JButton("View Users");
        users_but.setBounds(150, 20, 120, 25);
        users_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Users List");

                sql = "select * from users";

                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);

                    JTable user_list = new JTable();
                    user_list.setModel(DbUtils.resultSetToTableModel(rs));

                    JScrollPane scrollPane = new JScrollPane(user_list);

                    f.add(scrollPane);
                    f.setSize(800, 400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }
        });

        JButton issued_but = new JButton("view Issued Books");
        issued_but.setBounds(280, 20, 160, 25);
        issued_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Issued books");

                sql = "select * from issued";

                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);

                    JTable issued_list = new JTable();
                    issued_list.setModel(DbUtils.resultSetToTableModel(rs));

                    JScrollPane scrollPane = new JScrollPane(issued_list);

                    f.add(scrollPane);
                    f.setSize(800, 400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });

        JButton add_user = new JButton("Add User");
        add_user.setBounds(20, 60, 120, 25);

        add_user.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Enter User Details");
                JLabel l1, l2;

                l1 = new JLabel("Username");
                l1.setBounds(30, 15, 100, 30);
                l2 = new JLabel("Password");
                l2.setBounds(30, 50, 100, 30);

                JTextField f_user = new JTextField();
                f_user.setBounds(110, 15, 200, 30);

                JPasswordField f_pass = new JPasswordField();
                f_pass.setBounds(110, 50, 200, 30);

                JRadioButton a1 = new JRadioButton("Admin");
                a1.setBounds(55, 80, 200, 30);

                JRadioButton a2 = new JRadioButton("User");
                a2.setBounds(130, 80, 200, 30);

                ButtonGroup bg = new ButtonGroup();
                bg.add(a1);
                bg.add(a2);

                JButton create_but = new JButton("Create");
                create_but.setBounds(130, 130, 80, 25);
                create_but.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String username = f_user.getText();
                        String password = new String(f_pass.getPassword());
                        String admin = "false";

                        if (a1.isSelected()) {
                            admin = "true";
                        }

                        try {
                            Statement st = con.createStatement();
                            sql = "insert into users(username,password,admin) values('" + username + "','" + password
                                    + "'," + admin + ")";
                            st.executeUpdate(sql);
                            JOptionPane.showMessageDialog(null, "User Added!");
                            f.dispose();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }
                });

                f.add(create_but);
                f.add(a2);
                f.add(a1);
                f.add(l1);
                f.add(l2);
                f.add(f_pass);
                f.add(f_user);
                f.setSize(350, 200);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });

        JButton add_book = new JButton("Add Book");
        add_book.setBounds(150, 60, 120, 25);

        add_book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Enter book details : ");
                JLabel l1, l2, l3;

                l1 = new JLabel("Book Name");
                l1.setBounds(30, 15, 100, 30);

                l2 = new JLabel("Genre");
                l2.setBounds(30, 53, 100, 30);

                l3 = new JLabel("Price");
                l3.setBounds(30, 90, 100, 30);

                JTextField f_bname = new JTextField();
                f_bname.setBounds(110, 15, 200, 30);

                JTextField f_genre = new JTextField();
                f_genre.setBounds(110, 53, 200, 30);

                JTextField f_price = new JTextField();
                f_price.setBounds(110, 90, 200, 30);

                JButton create_but = new JButton("Submit");
                create_but.setBounds(130, 130, 80, 25);
                create_but.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String bname = f_bname.getText();
                        String genre = f_genre.getText();
                        String price = f_price.getText();

                        // int price_int = Integer.parseInt(price);

                        try {
                            Statement st = con.createStatement();
                            sql = "insert into books(bname,genre,price) values('" + bname + "','" + genre + "'," + price
                                    + ")";
                            st.executeUpdate(sql);

                            JOptionPane.showMessageDialog(null, "Book Added!");
                            f.dispose();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);

                        }
                    }
                });

                f.add(l1);
                f.add(l2);
                f.add(l3);
                f.add(f_bname);
                f.add(f_price);
                f.add(f_genre);
                f.add(create_but);
                f.setSize(350, 200);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });

        JButton issue_book = new JButton("Issue Book");
        issue_book.setBounds(450, 20, 120, 25);

        issue_book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Enter Details");

                JLabel l1, l2, l3, l4;

                l1 = new JLabel("Book ID(BID)");
                l1.setBounds(30, 15, 100, 30);

                l2 = new JLabel("User ID(UID)");
                l2.setBounds(30, 53, 100, 30);

                l3 = new JLabel("Period(days)");
                l3.setBounds(30, 90, 100, 30);

                l4 = new JLabel("Issued Date(dd-mm-yyyy)");
                l4.setBounds(30, 127, 150, 30);

                JTextField f_bid, f_uid, f_period, f_issue;

                f_bid = new JTextField();
                f_bid.setBounds(110, 15, 200, 30);

                f_uid = new JTextField();
                f_uid.setBounds(110, 53, 200, 30);

                f_period = new JTextField();
                f_period.setBounds(110, 90, 200, 30);

                f_issue = new JTextField();
                f_issue.setBounds(180, 130, 130, 30);

                JButton create_but = new JButton("Submit");
                create_but.setBounds(130, 170, 80, 30);
                create_but.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {

                        String bid = f_bid.getText();
                        String uid = f_uid.getText();
                        String period = f_period.getText();
                        String issue = f_issue.getText();

                        try {
                            Statement st = con.createStatement();
                            sql = "insert into issued(uid,bid,issued_date,period) values("
                                    + uid + "," + bid + ",'" + issue + "'," + period + ")";

                            st.executeUpdate(sql);
                            JOptionPane.showMessageDialog(null, "Book Issued");
                            f.dispose();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);

                        }

                    }
                });

                f.add(l1);
                f.add(l2);
                f.add(l3);
                f.add(l4);
                f.add(f_bid);
                f.add(f_uid);
                f.add(f_period);
                f.add(f_issue);
                f.add(create_but);
                f.setSize(350, 250);
                f.setLayout(null);
                f.setLocationRelativeTo(null);
                f.setVisible(true);

            }
        });

        JButton return_book = new JButton("Return Book");
        return_book.setBounds(280, 60, 160, 25);

        return_book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame f = new JFrame("Enter details");

                JLabel l1, l2;

                l1 = new JLabel("Issue Id(IID)");
                l1.setBounds(30, 15, 100, 30);

                l2 = new JLabel("Return Date(dd-mm-yyyy)");
                l2.setBounds(30, 50, 150, 30);

                JTextField f_iid = new JTextField();
                f_iid.setBounds(110, 15, 200, 30);

                JTextField f_return = new JTextField();
                f_return.setBounds(110, 50, 200, 30);

                JButton create_but = new JButton("Return");
                create_but.setBounds(130, 100, 80, 25);

                create_but.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String iid = f_iid.getText();
                        String return_date = f_return.getText();
                        String diff_str = "";
                        int days = 0;

                        try {
                            Statement st = con.createStatement();
                            String date1 = null;
                            String date2 = return_date;

                            sql = "select issued_date from issued where iid=" + iid;

                            ResultSet rs = st.executeQuery(sql);

                            while (rs.next()) {
                                date1 = rs.getString(1);
                            }

                            try {
                                Date date_1 = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
                                Date date_2 = new SimpleDateFormat("dd-MM-yyyy").parse(date2);

                                long diff = date_2.getTime() - date_1.getTime();
                                days = (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            sql = "update issued set return_date='" + return_date + "' where iid=" + iid;
                            st.executeUpdate(sql);

                            f.dispose();

                            Statement st1 = con.createStatement();
                            sql = "select period from issued where iid=" + iid;
                            ResultSet rs1 = st1.executeQuery(sql);

                            while (rs1.next()) {
                                diff_str = rs1.getString(1);
                            }

                            int diff1 = Integer.parseInt(diff_str);

                            if (days > diff1) {
                                int fine = (days - diff1) * 10;
                                sql = "update issued set fine=" + fine + " where iid=" + iid;
                                st1.executeUpdate(sql);
                                String fine_str = ("Fine : Rs." + fine);
                                JOptionPane.showMessageDialog(null, fine_str);

                            }
                            JOptionPane.showMessageDialog(null, "Book Returned");

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e);

                        }
                    }
                });

                f.add(l1);
                f.add(l2);
                f.add(create_but);
                f.add(f_iid);
                f.add(f_return);
                f.setSize(400, 300);
                f.setVisible(true);
                f.setLayout(null);
                f.setLocationRelativeTo(null);

            }
        });

        frame.add(add_user);
        frame.add(view_but);
        frame.add(users_but);
        frame.add(add_book);
        frame.add(return_book);
        frame.add(issue_book);
        frame.add(issued_but);
        frame.setSize(600, 200);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}
