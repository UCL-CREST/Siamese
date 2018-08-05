    public void createDatabase() {
        String framework = "embedded";
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String protocol = "jdbc:derby:";
        Statement s = null;
        ResultSet rs = null;
        Properties props = new Properties();
        props.put("user", "suhaib");
        props.put("password", "suhaib");
        try {
            tempConnection = DriverManager.getConnection(protocol + "SubjectCodes" + ";create=true", props);
            JOptionPane.showMessageDialog(this, "Database connection established");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Cannot connect to database server");
        }
        try {
            s = tempConnection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(ResultStat.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.execute("CREATE TABLE  SubjectDetails(SubCode varchar(10) NOT NULL,SubName varchar(50) NOT NULL,Semester int NOT NULL, PRIMARY KEY  (SubCode))");
        } catch (SQLException e) {
            System.out.println("Table SubjectDetails already created");
        }
    }
