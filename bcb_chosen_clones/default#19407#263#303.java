    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        String framework = "embedded";
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String protocol = "jdbc:derby:";
        Statement s = null;
        ResultSet rs = null;
        Properties props = new Properties();
        props.put("user", "julius");
        props.put("password", "julius");
        do {
            ResultStatInput.URLName = (String) JOptionPane.showInputDialog(this, "Specify the URL to be used", "Name Required", JOptionPane.PLAIN_MESSAGE, null, null, "http://www.chennaieducation.net/results/annauniv/results.asp");
        } while (ResultStatInput.URLName.length() == 0);
        try {
            conn = DriverManager.getConnection(protocol + "ResultStat" + ";create=true", props);
            JOptionPane.showMessageDialog(this, "Database connection established");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Cannot connect to database server");
        }
        try {
            s = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(ResultStat.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.execute("CREATE TABLE  MarkInfo ( RegNumber bigint NOT NULL, SubCode varchar(10) NOT NULL, Internal float NOT NULL, Extern float NOT NULL,Result varchar(10) NOT NULL,Semester int NOT NULL)");
        } catch (SQLException e) {
            System.out.println("Table MarkInfo already created");
        }
        try {
            s.execute("CREATE TABLE  StudentInfo(RegNumber bigint NOT NULL,Name varchar(50) NOT NULL, PRIMARY KEY  (RegNumber))");
        } catch (SQLException e) {
            System.out.println("Table StudentInfo already created");
        }
        createDatabase();
        try {
            s.execute("CREATE TABLE  SubjectDetails(SubCode varchar(10) NOT NULL,SubName varchar(50) NOT NULL,Semester int NOT NULL, PRIMARY KEY  (SubCode))");
            copyTheValues();
        } catch (SQLException e) {
            System.out.println("Table SubjectDetails already created");
        }
    }
