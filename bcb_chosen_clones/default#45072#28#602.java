    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
        } catch (Exception E) {
            out.println("Unable to load driver.");
            E.printStackTrace();
        }
        try {
            String UserSystemID = (String) session.getAttribute("UserSystemID");
            if (UserSystemID != null) {
                String PathDB = getInitParameter("PathDB");
                String UserDB = getInitParameter("UserDB");
                String PassDB = getInitParameter("PassDB");
                String Page_request = request.getParameter("page_request");
                String dbURL = "jdbc:mysql://" + PathDB + "?user=" + UserDB + "&password=" + PassDB;
                connectDB = DriverManager.getConnection(dbURL);
                byte outputbody = 1;
                String SelectedCourse = null;
                String CurrentTerm = null;
                try {
                    Statement sqlstmt = connectDB.createStatement();
                    ResultSet termset = sqlstmt.executeQuery("select Term from CurrentTerm");
                    while (termset.next()) {
                        CurrentTerm = termset.getString("Term");
                    }
                    session.setAttribute("CurrentTerm", CurrentTerm);
                    out.println("<html>");
                    byte CourseCount = 0;
                    byte IncrementCount = 1;
                    if (Page_request.equals("collectassignmentsPage")) {
                        SelectedCourse = (String) session.getAttribute("SelectedCourse");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp");
                        out.println("<span class=\"title\">Collect Assignments</span>");
                        HtmlSection2(out);
                        out.println("<table border=\"0\" cellpadding=\"5\" cellspacing=\"1\"width=\"100%\" height=\"100%\">");
                        out.println("<tr>");
                        out.println("<script src=\"js/screenarea.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        String sql = "SELECT AssignmentSystemID,AssignmentName FROM Assignment " + "WHERE SectionSystemID=" + SelectedCourse + " ORDER BY AssignmentPostTime";
                        ResultSet resultset = sqlstmt.executeQuery(sql);
                        if (resultset.first() == false) {
                            out.println("<span class=\"ahyo14\">No assignments have been given so no student submissions are available for collection.</span>");
                        } else {
                            out.println("<span class=\"ahb14\">Select Assignment for collection:</span>");
                            out.println("<br><br>");
                            out.println("<form method=\"post\" action=\"ePICE2?page_request=collectassignmentsPage\">");
                            out.println("<select name=\"AssignmentSystemID\">");
                            String AssignmentSystemID = null;
                            String AssignmentName = null;
                            resultset.beforeFirst();
                            while (resultset.next()) {
                                AssignmentSystemID = resultset.getString("AssignmentSystemID");
                                AssignmentName = resultset.getString("AssignmentName");
                                out.println("<option value=\"" + AssignmentSystemID + "\">" + AssignmentName + "</option>");
                            }
                            resultset.close();
                            sqlstmt.close();
                            out.println("</select>");
                            session.setAttribute("process", "collect");
                            out.println("<input type=\"submit\" NAME=\"Go\" value=\"Go\"> ");
                            out.println("</form>");
                        }
                        out.println("</td>");
                        VerticalDivBar(out);
                        out.println("<td valign=\"top\" width=\"*\">");
                        sqlstmt.close();
                        HtmlClose1(out, session);
                        gotomenu(out);
                        out.println("<p>");
                        out.println("&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/collectassignmenthelp.html')\">Collect Assignments Help</a>");
                        HtmlClose2(out);
                    }
                    if (Page_request.equals("currenttermPage")) {
                        SelectedCourse = request.getParameter("SelectedCourse");
                        session.setAttribute("SelectedCourse", SelectedCourse);
                        String StrCourseCount = (String) session.getAttribute("CourseCount");
                        if (StrCourseCount == null) {
                            StrCourseCount = "0";
                        }
                        byte IncrementStep = 1;
                        Integer SessionCourseCount = Integer.valueOf(StrCourseCount);
                        if (SessionCourseCount.intValue() > 0) {
                            while (SessionCourseCount.intValue() >= IncrementStep) {
                                if (SelectedCourse.equals((String) session.getAttribute("Course" + IncrementStep))) {
                                    session.setAttribute("SelectedCourseName", (String) session.getAttribute("Course" + IncrementStep + "N"));
                                }
                                IncrementStep += 1;
                            }
                        }
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/antextwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/textwin400200.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp");
                        out.println("<span class=\"title\">Current Term</span>");
                        HtmlSection2(out);
                        doGetHtmlSection3(out);
                        out.println("<span class=\"ahb14\">Announcement(s) on file:</span><p>");
                        String getannouncesql = "select AnnounceHeader,AnnounceText,AnnounceTimeStamp from Announcement where SectionSystemID='" + SelectedCourse + "' order by AnnounceTimeStamp desc";
                        ResultSet announceset = sqlstmt.executeQuery(getannouncesql);
                        if (announceset.first() == false) {
                            out.println("&nbsp&nbsp <span class=\"ahyo14\">There are no announcements posted.</span>");
                        } else {
                            String AnheaderVar = null;
                            String AntextVar = null;
                            String AntstampVar = null;
                            announceset.beforeFirst();
                            while (announceset.next()) {
                                AnheaderVar = announceset.getString("AnnounceHeader");
                                AntextVar = announceset.getString("AnnounceText");
                                AntstampVar = announceset.getString("AnnounceTimeStamp");
                                out.println("<span class=\"ahyo12\">" + AntstampVar + "</span><br>");
                                out.println("&nbsp&nbsp <a class=\"ah12\" href=\"javascript:text_win_400_200('" + AntextVar + "')\">");
                                out.println("<span class=\"ah12\">" + AnheaderVar + "</span></a><br><br>");
                            }
                        }
                        sqlstmt.close();
                        HtmlClose1(out, session);
                        gotomenu(out);
                        currentinst(out, session);
                        out.println("<p>&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/currenttermhelp.html')\">Current Term Help</a>");
                        HtmlClose2(out);
                    }
                    if (Page_request.equals("postannouncementPage")) {
                        SelectedCourse = (String) session.getAttribute("SelectedCourse");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/textwin400200.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/ePICE2Chk.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp");
                        out.println("<span class=\"title\">Post Announcement</span>");
                        HtmlSection2(out);
                        out.println("<form action=\"ePICE2?page_request=postannouncementPage\" method=\"post\" name=\"AnnForm\" onsubmit=\"return CheckSubmittedValues(\'postannouncementPage\')\">");
                        out.println("<table border=\"0\" cellpadding=\"5\" cellspacing=\"1\"width=\"100%\" height=\"100%\">");
                        out.println("<tr>");
                        out.println("<script src=\"js/screenarea.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<span class=\"ahb14\">Announcement(s) on file:</span><p>");
                        String getannouncesql = "select AnnounceHeader,AnnounceText,AnnounceTimeStamp from Announcement where SectionSystemID = '" + SelectedCourse + "' order by AnnounceTimeStamp desc";
                        ResultSet announceset = sqlstmt.executeQuery(getannouncesql);
                        if (announceset.first() == false) {
                            out.println("&nbsp&nbsp<span class=\"ahyo14\">There are no announcements posted.</span>");
                        } else {
                            String AnheaderVar = null;
                            String AntextVar = null;
                            String AntstampVar = null;
                            announceset.beforeFirst();
                            while (announceset.next()) {
                                AnheaderVar = announceset.getString("AnnounceHeader");
                                AntextVar = announceset.getString("AnnounceText");
                                AntextVar.replaceAll("'", "\"'");
                                AntstampVar = announceset.getString("AnnounceTimeStamp");
                                out.println("<span class=\"ahyo12\">" + AntstampVar + "</span><br>");
                                out.println("&nbsp&nbsp <a class=\"ah12\" href=\"javascript:text_win_400_200('" + AntextVar + "')\">");
                                out.println("<span class=\"ah12\">" + AnheaderVar + "</span></a><br><br>");
                            }
                        }
                        sqlstmt.close();
                        out.println("</td>");
                        VerticalDivBar(out);
                        out.println("<td valign=\"top\" width=\"*\">");
                        out.println("<span class=\"ahb14\">Post New Announcement</span>");
                        out.println("<p>");
                        out.println("<span class=\"ah10\">Announcement Header:</span><br>");
                        out.println("<script src=\"js/128dyntextbox.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script language=\"JavaScript\">");
                        out.println("document.AnnForm.sourceheader.focus();");
                        out.println("</script>");
                        out.println("<p>");
                        out.println("<span class=\"ah10\">Announcement Text:</span><br>");
                        out.println("<script src=\"js/dyntextarea.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script> name=\"Antext\"></textarea>");
                        out.println("<p>");
                        out.println("<input type=\"reset\" Value=\" Reset \">&nbsp&nbsp&nbsp<input type=\"Submit\" Value=\"Submit\">");
                        out.println("</form>");
                        HtmlClose1(out, session);
                        gotomenu(out);
                        out.println("<p>");
                        currentinst(out, session);
                        out.println("<p>");
                        out.println("&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/postannouncehelp.html')\">Post Announcement Help</a>");
                        HtmlClose2(out);
                    }
                    if (Page_request.equals("postassignmentPage")) {
                        SelectedCourse = (String) session.getAttribute("SelectedCourse");
                        out.println("<script src=\"js/ePICE2Chk.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp");
                        out.println("<span class=\"title\">Post Assignment</span>");
                        HtmlSection2(out);
                        doGetHtmlSection3(out);
                        out.println("<script src=\"js/screenarea.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<span class=\"ahb14\">Assignment(s) on file:</span><p>");
                        String getassignmentsql = "select AssignmentName,AssignmentPostTime from Assignment where SectionSystemID = '" + SelectedCourse + "' order by AssignmentPostTime";
                        ResultSet assignmentset = sqlstmt.executeQuery(getassignmentsql);
                        if (assignmentset.first() == false) {
                            out.println("&nbsp&nbsp<span class=\"ahyo14\">There are no assignments posted.</span>");
                        } else {
                            String AnameVar = null;
                            String AposttimeVar = null;
                            assignmentset.beforeFirst();
                            while (assignmentset.next()) {
                                AnameVar = assignmentset.getString("AssignmentName");
                                AposttimeVar = assignmentset.getString("AssignmentPostTime");
                                out.println("<span class=\"ahyo12\">" + AposttimeVar + "</span><br><span class=\"ah12\">&nbsp&nbsp " + AnameVar + "</span><br><br>");
                            }
                        }
                        sqlstmt.close();
                        out.println("</td>");
                        VerticalDivBar(out);
                        out.println("<td valign=\"top\" width=\"*\">");
                        out.println("<form action=\"ePICE2?page_request=postassignmentPage\" name=\"postassignmentForm\" method=\"post\" onsubmit=\"return CheckSubmittedValues(\'postassignmentPage\')\">");
                        out.println("<span class=\"ahb14\">Post New Assignment</span>");
                        out.println("<p>");
                        out.println("<span class=\"ah10\">Assignment Name: (must be unique)</span>");
                        out.println("<script src=\"js/128dyntextbox.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<input type=\"hidden\" name=\"process\" value=\"getSourceHeader\"><p>");
                        out.println("<input type=\"reset\" value=\" Reset \">&nbsp&nbsp&nbsp<input type=\"Submit\" Value=\"Submit\">");
                        out.println("</form>");
                        out.println("<script language=\"JavaScript\">");
                        out.println("document.postassignmentForm.sourceheader.focus();");
                        out.println("</script>");
                        HtmlClose1(out, session);
                        gotomenu(out);
                        currentinst(out, session);
                        out.println("<p>&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/postassignmenthelp.html')\">Post Assignment Help</a>");
                        HtmlClose2(out);
                    }
                    if (Page_request.equals("postmaterialPage")) {
                        SelectedCourse = (String) session.getAttribute("SelectedCourse");
                        out.println("<script src=\"js/ePICE2Chk.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp");
                        out.println("<span class=\"title\">Post Material</span>");
                        HtmlSection2(out);
                        doGetHtmlSection3(out);
                        out.println("<script src=\"js/screenarea.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<span class=\"ahb14\">Material(s) on file:</span><p>");
                        String getmaterialsql = "select MaterialName,MaterialPostTime from Material where SectionSystemID = '" + SelectedCourse + "' order by MaterialPostTime";
                        ResultSet materialset = sqlstmt.executeQuery(getmaterialsql);
                        if (materialset.first() == false) {
                            out.println("&nbsp&nbsp<span class=\"ahyo14\">There are no materials posted.</span>");
                        } else {
                            String MnameVar = null;
                            String MposttimeVar = null;
                            materialset.beforeFirst();
                            while (materialset.next()) {
                                MnameVar = materialset.getString("MaterialName");
                                MposttimeVar = materialset.getString("MaterialPostTime");
                                out.println("<span class=\"ahyo12\">" + MposttimeVar + "</span><br><span class=\"ah12\">&nbsp&nbsp " + MnameVar + "</span><br><br>");
                            }
                        }
                        sqlstmt.close();
                        out.println("</td>");
                        VerticalDivBar(out);
                        out.println("<td valign=\"top\" width=\"*\">");
                        out.println("<form action=\"ePICE2?page_request=postmaterialPage\" name=\"postmaterialForm\" method=\"post\" onsubmit=\"return CheckSubmittedValues(\'postmaterialPage\')\">");
                        out.println("<span class=\"ahb14\">Post New Material</span>");
                        out.println("<p>");
                        out.println("<span class=\"ah10\">Material Name: (must be unique)</span>");
                        out.println("<script src=\"js/128dyntextbox.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script><p>");
                        out.println("<input type=\"hidden\" name=\"process\" value=\"getSourceHeader\">");
                        out.println("<input type=\"reset\" value=\" Reset \">&nbsp&nbsp&nbsp<input type=\"Submit\" Value=\"Submit\">");
                        out.println("</form>");
                        out.println("<script language=\"JavaScript\">");
                        out.println("document.postmaterialForm.sourceheader.focus();");
                        out.println("</script>");
                        HtmlClose1(out, session);
                        gotomenu(out);
                        currentinst(out, session);
                        out.println("<p>&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/postmaterialhelp.html')\">Post Material Help</a>");
                        HtmlClose2(out);
                    }
                    if (Page_request.equals("postscoresPage")) {
                        out.println("<script src=\"js/ePICE2Chk.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp<span class=\"title\">");
                        out.println(" Post Scores");
                        out.println("</span>");
                        out.println("</td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<td valign=\"top\" bgcolor=\"000099\">");
                        out.println("<table border=\"0\" cellpadding=\"5\" cellspacing=\"1\"width=\"100%\" height=\"100%\">");
                        out.println("<tr>");
                        out.println("<span class=\"ahb14\">Select the Assignment for Posting Scores:</span><p>");
                        ResultSet assignmentset = sqlstmt.executeQuery("select AssignmentName,AssignmentSystemID from Assignment where SectionSystemID=\'" + (String) session.getAttribute("SelectedCourse") + "\' order by AssignmentName");
                        if (assignmentset.first() == false) {
                            out.println("<span class=\'ahyo14\'>No assignments have been posted for this section.</span><p>");
                            out.println("<span class=\'ahb12\'>You will need to have at least one assignment with at least one student submission before you can post any score.</span><p>");
                            out.println("<span class=\'ahb12\'>Please navigate to the ePICE Post Assignment page or select another ePICE function.</span><p>");
                        } else {
                            String CurrentAssignmentSystemID = null;
                            String CurrentAssignmentName = null;
                            assignmentset.beforeFirst();
                            out.println("<form action=\"ePICE2?page_request=postscoresPage\" method=\"post\" name=\"PostScoresForm\">");
                            out.println("<span class=\"ahb10\">Available Assignments:</span><br>");
                            out.println("<select name=\"assignmentchoice\" size=\"15\">");
                            out.println("<option selected>&lt select assignment &gt");
                            while (assignmentset.next()) {
                                CurrentAssignmentName = assignmentset.getString("AssignmentName");
                                CurrentAssignmentSystemID = assignmentset.getString("AssignmentSystemID");
                                out.println("<option value=\"" + CurrentAssignmentSystemID + "\">" + CurrentAssignmentName);
                            }
                            out.println("</select>");
                            out.println("<input type=\"hidden\" name=\"process\" value=\"enterscores\">");
                            out.println("<input type=\"submit\" value=\"Get Scores\">");
                            out.println("</form>");
                        }
                        HtmlClose1(out, session);
                        gotomenu(out);
                        out.println("<p>");
                        currentinst(out, session);
                        out.println("<p>");
                        if (Page_request.equals("PostScores")) {
                            out.println("&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/postscoreshelp.html')\">Post Scores Roster Help</a>");
                        }
                        sqlstmt.close();
                        out.println("</td>");
                        out.println("</tr>");
                        out.println("</table>");
                    }
                    if (Page_request.equals("replyquestionPage")) {
                        String questid = request.getParameter("Questid");
                        SelectedCourse = (String) session.getAttribute("SelectedCourse");
                        out.println("<script src=\"js/ePICE2Chk.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp");
                        out.println("<span class=\"title\">Reply to Questions</span>");
                        HtmlSection2(out);
                        out.println("<table border=\"0\" cellpadding=\"5\" cellspacing=\"1\"width=\"100%\" height=\"100%\">");
                        out.println("<tr>");
                        out.println("<script src=\"js/screenarea.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<span class=\"ahb16\">Question(s) on file:</span><p>");
                        String getquestionsql = "select QuestionSystemID,QuestionHeader,QuestionTimeStamp,PostFlag from Question where SectionSystemID = '" + SelectedCourse + "' order by QuestionTimeStamp desc";
                        ResultSet questionset = sqlstmt.executeQuery(getquestionsql);
                        if (questionset.first() == false) {
                            out.println("&nbsp&nbsp<span class=\"ahyo14\">There are no questions submitted.</span>");
                        } else {
                            String QuestsystemidVar = null;
                            String QuestheaderVar = null;
                            String QuesttstampVar = null;
                            int QuestreplyVar = 0;
                            questionset.beforeFirst();
                            while (questionset.next()) {
                                QuestsystemidVar = questionset.getString("QuestionSystemID");
                                QuestheaderVar = questionset.getString("QuestionHeader");
                                QuesttstampVar = questionset.getString("QuestionTimeStamp");
                                QuestreplyVar = questionset.getInt("PostFlag");
                                out.println("<span class=\"ahyo12\">" + QuesttstampVar + "</span><br>");
                                out.println("&nbsp&nbsp <a class=\"ah12\" href=\"ePICE2?page_request=replyquestionPage&Questid=" + QuestsystemidVar + "\"");
                                out.println("<span class=\"ah12\">" + QuestheaderVar + "</span>");
                                if (QuestreplyVar == 1) {
                                    out.println("<br>(Reply Posted)");
                                }
                                out.println("</a><br><br>");
                            }
                        }
                        out.println("</td>");
                        VerticalDivBar(out);
                        out.println("<td valign=\"top\" width=\"*\">");
                        if (questid != null) {
                            String getreplysql = "select QuestionHeader,QuestionTimeStamp,QuestionText,ReplyText,ReplyTimeStamp from Question where QuestionSystemID=" + questid;
                            ResultSet replyset = sqlstmt.executeQuery(getreplysql);
                            String QuestheaderVar = null;
                            String QuesttstampVar = null;
                            String QuesttextVar = null;
                            String LasttextVar = null;
                            String ReplytstampVar = null;
                            String Replyoutput = null;
                            while (replyset.next()) {
                                QuestheaderVar = replyset.getString("QuestionHeader");
                                QuesttstampVar = replyset.getString("QuestionTimeStamp");
                                QuesttextVar = replyset.getString("QuestionText");
                                LasttextVar = replyset.getString("ReplyText");
                                ReplytstampVar = replyset.getString("ReplyTimeStamp");
                                out.println("<span class=\"ahyo14\">Question:</span><br>");
                                out.println("<span class=\"ah10\">Submitted: </span><span class=\"ahy10\">" + QuesttstampVar + "</span><br>");
                                out.println("<span class=\"ah10\">Header: </span><span class=\"ahy10\">" + QuestheaderVar + "</span>");
                                out.println("<h1 class=\"ah12\">" + QuesttextVar + "</h1><p>");
                                out.println("<span class=\"ahyo14\">Reply:</span><br>");
                                if (ReplytstampVar != null) {
                                    out.println("<span class=\"ah10\">Posted or Appended: </span><span class=\"ahy10\">" + ReplytstampVar + "</span>");
                                    out.println("<h1 class=\"ah12\">" + LasttextVar + "</h1>");
                                } else {
                                    out.println("<p><span class=\"ah12\">-NONE-</span>");
                                }
                            }
                            out.println("<form action=\"ePICE2?page_request=replyquestionPage\" method=\"post\" name=\"ReplyQuestForm\" onsubmit=\"return CheckSubmittedValues(\'replyquestionPage\')\">");
                            out.println("<p><span class=\"ahyo14\">Post or Append Reply:</span><br>");
                            out.println("<span class=\"ah10\">Reply Text:</span><br>");
                            out.println("<script src=\"js/dyntextarea.js\" language=\"javascript\" type=\"text/javascript\">");
                            out.println("</script> name=\"Replytext\"></textarea>");
                            out.println("<script language=\"JavaScript\">");
                            out.println("document.ReplyQuestForm.Replytext.focus();");
                            out.println("</script>");
                            out.println("<input name=\"Questid\" type=\"hidden\" value='" + questid + "'>");
                            out.println("<input name=\"Lastreplytext\" type=\"hidden\" value='" + LasttextVar + "'>");
                            out.println("<p>");
                            out.println("<input type=\"reset\" value=\" Reset \">&nbsp&nbsp&nbsp<input type=\"submit\" name=\"submit\" value=\"Submit\">");
                            out.println("</form>");
                        }
                        sqlstmt.close();
                        HtmlClose1(out, session);
                        gotomenu(out);
                        out.println("<p>");
                        currentinst(out, session);
                        out.println("<p>");
                        out.println("&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/replyquestionhelp.html')\">Reply to Question Help</a>");
                        HtmlClose2(out);
                    }
                    if (Page_request.equals("retrievequestionPage")) {
                        String questid = request.getParameter("Questid");
                        out.println("<head>");
                        out.println("<title>ePICE Retrieved Question</title>");
                        out.println("<link rel=stylesheet href=\"css/epice1.css\" type=\"text/css\">");
                        out.println("</head>");
                        out.println("<body>");
                        if (questid != null) {
                            String getreplysql = "select QuestionHeader,QuestionTimeStamp,QuestionText,ReplyText,ReplyTimeStamp from Question where QuestionSystemID=" + questid;
                            ResultSet replyset = sqlstmt.executeQuery(getreplysql);
                            String QuestheaderVar = null;
                            String QuesttstampVar = null;
                            String QuesttextVar = null;
                            String ReplytextVar = null;
                            String ReplytstampVar = null;
                            String Replyoutput = null;
                            while (replyset.next()) {
                                QuestheaderVar = replyset.getString("QuestionHeader");
                                QuesttstampVar = replyset.getString("QuestionTimeStamp");
                                QuesttextVar = replyset.getString("QuestionText");
                                ReplytextVar = replyset.getString("ReplyText");
                                ReplytstampVar = replyset.getString("ReplyTimeStamp");
                                out.println("<span class=\"ahyo12\">Question:</span><br>");
                                out.println("<span class=\"ah10\">Submitted: </span><span class=\"ahy10\">" + QuesttstampVar + "</span><br>");
                                out.println("<span class=\"ah10\">Header: </span><span class=\"ahy10\">" + QuestheaderVar + "</span><br>");
                                out.println("<h1 class=\"ah12\">" + QuesttextVar + "</h1><p>");
                                out.println("<span class=\"ahyo12\">Reply:</span><br>");
                                if (ReplytstampVar != null) {
                                    out.println("<span class=\"ah10\">Posted or Appended: </span><span class=\"ahy10\">" + ReplytstampVar + "</span>");
                                    out.println("<h1 class=\"ah12\">" + ReplytextVar + "</h1>");
                                } else {
                                    out.println("<p><span class=\"ah12\">-NONE-</span>");
                                }
                                out.println("<p><span class=\"ahyo12\">**End**</span><br>");
                            }
                        }
                    }
                    if (Page_request.equals("AdminClassRoster")) {
                        out.println("<script src=\"js/ePICE2Chk.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/instructornavmenu.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        out.println("<script src=\"js/helpwin.js\" language=\"javascript\" type=\"text/javascript\">");
                        out.println("</script>");
                        HtmlSection1(out);
                        out.println("<img src=\"graphics/syslogo.jpg\" alt=\"Public\" align=\"left\">&nbsp<span class=\"title\">");
                        out.println(" Administer Class Roster");
                        out.println("</span>");
                        out.println("</td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<td valign=\"top\" bgcolor=\"000099\">");
                        out.println("<table border=\"0\" cellpadding=\"5\" cellspacing=\"1\"width=\"100%\" height=\"100%\">");
                        out.println("<tr>");
                        out.println("<span class=\"ahb16\">Administer Full Enrollment Roster</span><p>");
                        out.println("<span class=\"ahb12\">Please select the current enrollment status of each student:</span><p>");
                        ResultSet courserosterset = sqlstmt.executeQuery("select User.UserSystemID,User.UserFname,User.UserLname,User.UserEmail,Enrolls.EnrollStatus from User,Enrolls where User.UserSystemID=Enrolls.UserSystemID and Enrolls.SectionSystemID=" + (String) session.getAttribute("SelectedCourse") + " order by UserLname,UserFname");
                        if (courserosterset.first() == false) {
                            out.println("<span class=\'ahyo14\'>There are no students enrolled in this section.  Please check back later.</span>");
                        } else {
                            String UserFname = null;
                            String UserLname = null;
                            String UserEmail = null;
                            String EnrollStatus = null;
                            courserosterset.beforeFirst();
                            out.println("<form action=\"ePICE2?page_request=AdminClassRoster\" method=\"post\" name=\"AdminClassRosterForm\">");
                            out.println("<span class=\'ahy10\'>Active &nbsp&nbsp&nbsp Drop</span><br>");
                            while (courserosterset.next()) {
                                UserSystemID = courserosterset.getString("UserSystemID");
                                UserFname = courserosterset.getString("UserFname");
                                UserLname = courserosterset.getString("UserLname");
                                UserEmail = courserosterset.getString("UserEmail");
                                EnrollStatus = courserosterset.getString("EnrollStatus");
                                if (EnrollStatus.equals("y")) {
                                    out.println("&nbsp&nbsp <input type=\"radio\" name=\"" + UserSystemID + "\" value=\"y\" checked> ");
                                    out.println("&nbsp&nbsp <input type=\"radio\" name=\"" + UserSystemID + "\" value=\"n\">");
                                } else {
                                    out.println("&nbsp&nbsp <input type=\"radio\" name=\"" + UserSystemID + "\" value=\"y\"> ");
                                    out.println("&nbsp&nbsp <input type=\"radio\" name=\"" + UserSystemID + "\" value=\"n\" checked>");
                                }
                                out.println("<span class=\'ah12\'>" + UserLname + ", " + UserFname + "</span>&nbsp&nbsp<span class=\'ahb12\'>" + UserEmail + "</span><br>");
                            }
                            out.println("<p>&nbsp&nbsp&nbsp<input type=\"submit\" name=\"submit\" value=\"Submit\">");
                            out.println("</form>");
                        }
                        HtmlClose1(out, session);
                        gotomenu(out);
                        out.println("<p>");
                        currentinst(out, session);
                        out.println("<p>");
                        out.println("&nbsp&nbsp<a class=\"ah10\" href=\"javascript:help_win('help/rosteradminhelp.html')\">Admin Class Roster Help</a>");
                        out.println("</td>");
                        out.println("</tr>");
                        out.println("</table>");
                    }
                    sqlstmt.close();
                    if (outputbody > 0) {
                        out.println("</body>");
                    }
                    out.println("</html>");
                } catch (Exception e) {
                    sendErrorToClient(out, e);
                    log("Error in doGet() method.", e);
                }
            } else {
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Automatic System Log Out</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>ePICE detected an extended period of inactivity.<p>You were logged out automatically as a security precaution.<p>If you wish to continue using ePICE, please return to the ePICE hpme page and login.<p>Thank you for your understanding.</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (SQLException E) {
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState: " + E.getSQLState());
            System.out.println("VendorError(298):" + E.getErrorCode());
        }
    }
