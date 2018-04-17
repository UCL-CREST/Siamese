    public static void main(String[] args) throws Exception {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" + "databaseName=ydeng;user=sa;password=12345678";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement stmt = con.createStatement();
            try {
                stmt.executeUpdate("DROP TABLE XAMin");
            } catch (Exception e) {
            }
            stmt.executeUpdate("CREATE TABLE XAMin (f1 int, f2 varchar(max))");
            stmt.close();
            con.close();
            Logger logger = Logger.getLogger("com.microsoft.sqlserver.jdbc.internals.XA");
            FileHandler fh = new FileHandler("outputLog.txt");
            logger.addHandler(fh);
            logger.setLevel(Level.FINE);
            SQLServerXADataSource ds = new SQLServerXADataSource();
            ds.setUser("sa");
            ds.setPassword("12345678");
            ds.setServerName("localhost");
            ds.setPortNumber(1433);
            ds.setDatabaseName("ydeng");
            XAConnection xaCon = ds.getXAConnection();
            con = xaCon.getConnection();
            XAResource xaRes = null;
            Xid xid = null;
            xid = XidImpl.getUniqueXid(1);
            xaRes = xaCon.getXAResource();
            xaRes.setTransactionTimeout(10);
            System.out.println("Write -> xid = " + xid.toString());
            xaRes.start(xid, XAResource.TMNOFLAGS);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO XAMin (f1,f2) VALUES (?, ?)");
            pstmt.setInt(1, 1);
            pstmt.setString(2, xid.toString());
            pstmt.executeUpdate();
            xaRes.end(xid, XAResource.TMSUCCESS);
            xaRes.prepare(xid);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            pstmt.close();
            con.close();
            xaCon.close();
            con = DriverManager.getConnection(connectionUrl);
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM XAMin");
            rs.next();
            System.out.println("Read -> xid = " + rs.getString(2));
            rs.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
