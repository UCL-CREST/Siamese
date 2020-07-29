    public boolean init() {
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Yipe: " + e);
        }
        try {
            dbCon = DriverManager.getConnection("jdbc:mysql://localhost/usenet", "cyberfox", "");
            dbQuery = dbCon.createStatement();
            dbFrmQuery = dbCon.prepareStatement("SELECT fromid FROM froms WHERE msgfrom = ?");
            dbMsgQuery = dbCon.prepareStatement("SELECT msgid FROM messages WHERE messageid = ?");
            dbGrpQuery = dbCon.prepareStatement("SELECT groupid FROM newsgroups WHERE newsgroup = ?");
            dbSubQuery = dbCon.prepareStatement("SELECT subjectid FROM subjects WHERE msgsubject = ?");
            dbGroupCountQuery = dbCon.prepareStatement("SELECT localcount,servercount FROM newsgroups WHERE newsgroup = ?");
            dbGroupMessageQuery = dbCon.prepareStatement("SELECT msgsubject, msgfrom, msgdate, messageid, msgbytes, msglines FROM newsgroups NATURAL LEFT JOIN groupmsgs NATURAL LEFT JOIN messages NATURAL LEFT JOIN subjects NATURAL LEFT JOIN froms WHERE (newsgroup = ? && messages.fromid = froms.fromid) ORDER BY ?");
            dbInsertFrom = dbCon.prepareStatement("INSERT INTO froms (msgfrom) values (?)");
            dbInsertSubject = dbCon.prepareStatement("INSERT INTO subjects (msgsubject) values (?)");
            dbInsertNewsgroups = dbCon.prepareStatement("INSERT INTO newsgroups (newsgroup) values (?)");
            dbInsertGroupMsg = dbCon.prepareStatement("INSERT INTO groupmsgs (msgid, groupid) values (?, ?)");
            dbInsertReferences = dbCon.prepareStatement("INSERT INTO msgrefs (msgid, refid, refstr) values (?, ?, ?)");
            dbInsertMessage = dbCon.prepareStatement("INSERT INTO messages (messageid, msgdate, msglines, msgscore, subjectid, fromid) values (?, ?, ?, ?, ?, ?)");
            dbGrpAdd = dbCon.prepareStatement("INSERT INTO newsgroups (newsgroup, firstmsg, lastmsg, servercount, localcount, subscribed) values (?, ?, ?, ?, ?, ?)");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e);
            return (false);
        }
        return (true);
    }
