import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RainbowAttendance {
  static HashMap<AttendanceTicket, Integer> retrieved = new HashMap<>();
  final static String DELIMETER = "___";

  private static void println(String s) {System.out.println(s);}
  private static void println() {System.out.println();}
  private static void print(String s) {System.out.print(s);}

  public static void main(String args[]) throws Exception {
    //Read Attendance from File
    retrieved = AttendanceIO.attendanceFromFile(args[0]);
    //Take Attendance
    System.out.println(args[1]);
    LoginFrame frame = new LoginFrame(args[1], retrieved);
    frame.setTitle("RainbowAttendance");
    frame.setVisible(true);
    frame.setBounds(10,10,370,350);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //frame.setResizable(false);

    //Write to File
    //EOM
  }
}


  class LoginFrame extends JFrame implements ActionListener {
    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Full name:");
    JLabel usernameLabel = new JLabel("Pitt Username:");
    JTextField nameTF = new JTextField();
    JTextField usernameTF = new JTextField();
    JButton enterButton = new JButton("ENTER");
    JButton resetButton = new JButton("RESET");
    JButton end = new JButton("END");
    HashMap<AttendanceTicket, Integer> attendance = new HashMap<>();
    HashSet<AttendanceTicket> today = new HashSet<>();
    String targetFile;

    LoginFrame(String target, HashMap<AttendanceTicket,Integer> data) throws Exception {
      targetFile = target;
      attendance = data;
      SetLayoutManager();
      SetLocationAndSize();
      addComponents();
      addActionEvent();
    }

    public void SetLocationAndSize() {
         nameLabel.setBounds(25,20,100,30);
         usernameLabel.setBounds(25,90,100,30);
         nameTF.setBounds(125,20,150,30);
         usernameTF.setBounds(125,90,150,30);
         enterButton.setBounds(15,150,100,30);
         resetButton.setBounds(130,150,100,30);
         end.setBounds(245,150,100,30);
    }

    public void SetLayoutManager() {
      container.setLayout(null);
    }
    public void addComponents() {
      container.add(nameLabel);
      container.add(usernameLabel);
      container.add(nameTF);
      container.add(usernameTF);
      container.add(enterButton);
      container.add(resetButton);
      container.add(end);
    }
    public void addActionEvent() {
      enterButton.addActionListener(this);
      end.addActionListener(this);
      resetButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == enterButton) {
        String ntxt, utxt;
        ntxt = nameTF.getText();
        utxt = usernameTF.getText();
        AttendanceTicket s = new AttendanceTicket(ntxt,utxt);
        Boolean doneAlready = !today.add(s);
        if (attendance.get(s) == null) attendance.put(s, 1);
        else if (doneAlready) attendance.put(s, attendance.get(s) + 1);
        nameTF.setText("");
        usernameTF.setText("");
      } else if (e.getSource() == resetButton) {
        nameTF.setText("");
        usernameTF.setText("");
      } else {
        try {
        AttendanceIO.writeAttendance(targetFile, attendance);

        JOptionPane.showMessageDialog(this, "Successfully Entered");
        setVisible(false);
        dispose();
      } catch (Exception e2) {
        JOptionPane.showMessageDialog(this, "Error Encountered" + e2.getMessage());
        setVisible(false);
        dispose();
      }

      }


    }
  }
  class AttendanceIO {
    public static void writeAttendance(String targetFile, HashMap<AttendanceTicket,Integer> attendance) throws Exception {

      // Open File and create medium to write to file
      File file = new File(targetFile);
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));

      //

      // Get info to write to file and write to it

      Set<AttendanceTicket> tickets = attendance.keySet();
      for (AttendanceTicket ticket:tickets) {bw.write(ticket.toString() + RainbowAttendance.DELIMETER + attendance.get(ticket) + '\n');}
      //

      //Close medium
      bw.close();
    }
    public static HashMap<AttendanceTicket, Integer> attendanceFromFile(String sourceFile) throws Exception {
        HashMap<AttendanceTicket, Integer> attendanceFromFile = new HashMap<>();
        File file = new File(sourceFile);
        Scanner scan = new Scanner(file);

        String s1;
        AttendanceTicket a;
        String[] s2;

        while (scan.hasNext()) {
          s1 = scan.nextLine();
          s2 = s1.split(RainbowAttendance.DELIMETER);
          a = new AttendanceTicket(s2[0],s2[1]);
          attendanceFromFile.put(a, Integer.parseInt(s2[2]));
        }
        return attendanceFromFile;
    }
}
  class AttendanceTicket {
  String name;
  String id;

  public AttendanceTicket(String name, String id) {
    this.name = name.toLowerCase();
    this.id = id.toLowerCase();
  }

  public String toString() {
    return this.name + RainbowAttendance.DELIMETER +  this.id;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  @Override
  public boolean equals(Object other) {
    return this.hashCode() == other.hashCode();
  }
}
