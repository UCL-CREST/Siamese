    public static void main(String args[]) {
        String url = "jdbc:timesten:direct:DSN=soppreproDSN;uid=sop_prepro;pwd=sop_prepro;oraclepwd=sop_prepro";
        Connection con;
        String createString;
        Format formatter = new SimpleDateFormat("hh:mm:ss.SSS");
        long first = Long.parseLong(args[0]);
        long second = Long.parseLong(args[1]);
        createString = "select sec_global,Mensaje from FED_TMENSAJES_CF " + " where SEC_GLOBAL between " + first + " and " + second + " order by sec_global";
        System.out.println(createString);
        Statement stmt;
        try {
            Class.forName("com.timesten.jdbc.TimesTenDriver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, "sop_prepro", "sop_prepro");
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
            ResultSet rs = stmt.executeQuery(createString);
            try {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Msg_" + args[0] + "-" + args[1] + ".txt"), "UTF8"));
                while (rs.next()) {
                    out.write(rs.getString(2).trim() + "\n");
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
