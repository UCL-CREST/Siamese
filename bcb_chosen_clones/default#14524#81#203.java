    public static void main(String[] args) throws Exception {
        genText g = new genText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sqdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        String userName = args[0];
        String cohortName = args[1];
        java.util.Date nowDate = new java.util.Date();
        java.util.Date monthDate = new java.util.Date();
        String dateString = sdf.format(nowDate);
        String sqlDate = sqdf.format(nowDate);
        java.util.Date cws = getCurrentWeekStart(nowDate);
        String monthString = sdf.format(monthDate);
        if (args.length > 2) {
            monthString = args[2];
        }
        monthDate = sdf.parse(monthString);
        String msqlDate = sqdf.format(monthDate);
        java.util.Date mcws = getCurrentWeekStart(monthDate);
        g.getGroup(cohortName);
        Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();
        Connection db = DriverManager.getConnection("jdbc:sybase:Tds:sin.gmp.usyd.edu.au:4100/VolAssessment", "sa", "frumious");
        Statement st = db.createStatement();
        ResultSet rs = null;
        String query = null;
        query = "select id from Users where name in (" + g.groupSqlString + ") and not name = 'anonymous'";
        System.err.println("executing " + query);
        rs = st.executeQuery(query);
        StringBuffer idStringBuffer = new StringBuffer();
        while (rs.next()) {
            idStringBuffer.append(rs.getString(1) + ",");
        }
        String idString = idStringBuffer.toString();
        idString = idString.substring(0, idString.length() - 1);
        query = "select id from Users where name = '" + userName + "'";
        rs = st.executeQuery(query);
        String uid = "";
        while (rs.next()) {
            uid = rs.getString(1);
        }
        System.out.println("tid|||tname|||qcount|||qusercount|||quseraverage|||qmonthcount|||qmonthaverage|||qcohortcount|||qcohoraverage|||qcurriculum|||>>>");
        Vector topics = new Vector();
        Vector blocks = new Vector();
        Vector probs = new Vector();
        query = "select id from Blocks order by studentYear";
        System.err.println("executing " + query);
        ResultSet blockset = st.executeQuery(query);
        while (blockset.next()) {
            query = "select id, releaseDate from Problems where block = " + blockset.getString(1) + " order by releaseDate";
            System.err.println("executing " + query);
            ResultSet probset = db.createStatement().executeQuery(query);
            while (probset.next()) {
                java.util.Date d = (java.util.Date) probset.getDate(2);
                query = "select id, name from LearningTopics where problem = " + probset.getString(1);
                System.err.println("executing " + query);
                ResultSet ltset = db.createStatement().executeQuery(query);
                while (ltset.next()) {
                    String tid = xclean(ltset.getString(1));
                    String tname = xclean(ltset.getString(2));
                    System.err.println("topic " + tid);
                    System.out.print(tid + "|||" + tname + "|||");
                    query = "select id from Questions where learningTopic = " + tid;
                    System.err.println("executing " + query);
                    ResultSet iset = db.createStatement().executeQuery(query);
                    String Questions = new String();
                    int nquestions = 0;
                    while (iset.next()) {
                        Questions = Questions + iset.getString(1) + ",";
                        nquestions++;
                    }
                    System.out.print(nquestions + "|||");
                    if (nquestions == 0) {
                        System.out.println("0|||null|||0|||null|||0|||null|||0|||>>>");
                        continue;
                    }
                    query = "select distinct questionId from AnswerLog where uid = " + uid + " and questionId in (" + Questions.substring(0, Questions.length() - 1) + ") and date < '" + sqlDate + "'";
                    System.err.println("executing " + query);
                    iset = db.createStatement().executeQuery(query);
                    int c = 0;
                    while (iset.next()) c++;
                    System.out.print(c + "|||");
                    query = "select count(mark), sum(mark) from AnswerLog where uid = " + uid + " and questionId in (" + Questions.substring(0, Questions.length() - 1) + ") and date < '" + sqlDate + "'";
                    System.err.println("executing " + query);
                    iset = db.createStatement().executeQuery(query);
                    if (iset.next()) {
                        float sum = iset.getFloat(2);
                        int count = iset.getInt(1);
                        if (count != 0) System.out.print((sum / count) + "|||"); else System.out.print("null|||");
                    } else System.out.print("null|||");
                    query = "select distinct questionId from AnswerLog where uid = " + uid + " and questionId in (" + Questions.substring(0, Questions.length() - 1) + ") and date < '" + msqlDate + "'";
                    System.err.println("executing " + query);
                    iset = db.createStatement().executeQuery(query);
                    c = 0;
                    while (iset.next()) c++;
                    System.out.print(c + "|||");
                    query = "select count(mark), sum(mark) from AnswerLog where uid = " + uid + " and questionId in (" + Questions.substring(0, Questions.length() - 1) + ") and date < '" + msqlDate + "'";
                    System.err.println("executing " + query);
                    iset = db.createStatement().executeQuery(query);
                    if (iset.next()) {
                        float sum = iset.getFloat(2);
                        int count = iset.getInt(1);
                        if (count != 0) System.out.print((sum / count) + "|||"); else System.out.print("null|||");
                    } else System.out.print("null|||");
                    query = "select distinct questionId from AnswerLog where uid in (" + idString + ") and questionId in (" + Questions.substring(0, Questions.length() - 1) + ") and date < '" + sqlDate + "'";
                    System.err.println("executing " + query);
                    iset = db.createStatement().executeQuery(query);
                    c = 0;
                    while (iset.next()) c++;
                    System.out.print(c + "|||");
                    query = "select count(mark), sum(mark) from AnswerLog where uid in (" + idString + ") and questionId in (" + Questions.substring(0, Questions.length() - 1) + ") and date < '" + sqlDate + "'";
                    System.err.println("executing " + query);
                    iset = db.createStatement().executeQuery(query);
                    if (iset.next()) {
                        float sum = iset.getFloat(2);
                        int count = iset.getInt(1);
                        if (count != 0) System.out.print((sum / count) + "|||"); else System.out.print("null|||");
                    } else System.out.print("null|||");
                    if (cws.after(d)) System.out.print("0.70|||"); else System.out.print("0.30|||");
                    System.out.println(">>>");
                    System.out.flush();
                }
            }
        }
    }
