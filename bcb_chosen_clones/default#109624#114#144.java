    public ResultSetFrame() {
        setTitle("ResultSet");
        setSize(300, 200);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Container contentPane = getContentPane();
        tableNames = new JComboBox();
        tableNames.addActionListener(this);
        JPanel p = new JPanel();
        p.add(tableNames);
        contentPane.add(p, "North");
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "gk", "gk");
            stmt = con.createStatement();
            DatabaseMetaData md = con.getMetaData();
            ResultSet mrs = md.getTables(null, null, null, new String[] { "TABLE" });
            while (mrs.next()) tableNames.addItem(mrs.getString(3));
            mrs.close();
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
    }
