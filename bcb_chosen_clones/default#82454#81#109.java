    public static void main(String[] args) throws Exception {
        genText g = new genText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sqdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        String cohortName = args[0];
        java.util.Date nowDate = new java.util.Date();
        String dateString = sdf.format(nowDate);
        String sqlDate = sqdf.format(nowDate);
        java.util.Date cws = getCurrentWeekStart(nowDate);
        g.getGroup(cohortName);
        Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();
        Connection db = DriverManager.getConnection("jdbc:sybase:Tds:sin.gmp.usyd.edu.au:4100/VolAssessment", "sa", "frumious");
        String query = null;
        query = "select id from Users where name in (" + g.groupSqlString + ") and not name = 'anonymous'";
        ResultSet users = db.createStatement().executeQuery(query);
        StringBuffer idStringBuffer = new StringBuffer();
        while (users.next()) {
            query = "select name from Users where id = " + users.getString(1);
            ResultSet user = db.createStatement().executeQuery(query);
            while (user.next()) {
                System.out.print(user.getString(1) + "|||");
            }
            query = "select avg(mark), count(id) from AnswerLog where uid = " + users.getString(1);
            ResultSet averages = db.createStatement().executeQuery(query);
            while (averages.next()) {
                emit(averages.getFloat(1) + "|||" + averages.getInt(2) + "|||>>>");
            }
        }
    }
