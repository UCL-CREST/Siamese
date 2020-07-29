    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletConfig config = getServletConfig();
        ServletContext context = config.getServletContext();
        try {
            String driver = context.getInitParameter("driver");
            Class.forName(driver);
            String dbURL = context.getInitParameter("db");
            String username = context.getInitParameter("username");
            String password = "";
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found.");
        } catch (SQLException e) {
            System.out.println("Error opening the db connection: " + e.getMessage());
        }
        String action = "";
        String notice;
        String error = "";
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(300);
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        } else {
            notice = "Unknown action!";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
            dispatcher.forward(request, response);
            return;
        }
        if (action.equals("edit_events")) {
            String sql;
            String month_name = "";
            int month;
            int year;
            Event event;
            if (request.getParameter("month") != null) {
                month = Integer.parseInt(request.getParameter("month"));
                String temp = request.getParameter("year_num");
                year = Integer.parseInt(temp);
                int month_num = month - 1;
                event = new Event(year, month_num, 1);
                month_name = event.getMonthName();
                year = event.getYearNumber();
                if (month < 10) {
                    sql = "SELECT * FROM event WHERE date LIKE '" + year + "-0" + month + "-%'";
                } else {
                    sql = "SELECT * FROM event WHERE date LIKE '" + year + "-" + month + "-%'";
                }
            } else {
                event = new Event();
                month_name = event.getMonthName();
                month = event.getMonthNumber() + 1;
                year = event.getYearNumber();
                sql = "SELECT * FROM event WHERE date LIKE '" + year + "-%" + month + "-%'";
            }
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                request.setAttribute("year", Integer.toString(year));
                request.setAttribute("month", Integer.toString(month));
                request.setAttribute("month_name", month_name);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_events.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving events from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_event")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Event event = new Event();
            event = event.getEvent(id);
            if (event != null) {
                request.setAttribute("event", event);
                request.setAttribute("id", Integer.toString(id));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_event.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error retrieving event from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("save_event")) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String year = request.getParameter("year");
            String start_time = "";
            String end_time = "";
            if (request.getParameter("all_day") == null) {
                String start_hour = request.getParameter("start_hour");
                String start_minutes = request.getParameter("start_minutes");
                String start_ampm = request.getParameter("start_ampm");
                String end_hour = request.getParameter("end_hour");
                String end_minutes = request.getParameter("end_minutes");
                String end_ampm = request.getParameter("end_ampm");
                if (Integer.parseInt(start_hour) < 10) {
                    start_hour = "0" + start_hour;
                }
                if (Integer.parseInt(end_hour) < 10) {
                    end_hour = "0" + end_hour;
                }
                start_time = start_hour + ":" + start_minutes + " " + start_ampm;
                end_time = end_hour + ":" + end_minutes + " " + end_ampm;
            }
            Event event = null;
            if (!start_time.equals("") && !end_time.equals("")) {
                event = new Event(title, description, month, day, year, start_time, end_time);
            } else {
                event = new Event(title, description, month, day, year);
            }
            if (event.saveEvent()) {
                notice = "Calendar event saved!";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error saving calendar event.";
                request.setAttribute("notice", notice);
                request.setAttribute("event", event);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_event.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("update_event")) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String year = request.getParameter("year");
            String start_time = "";
            String end_time = "";
            int id = Integer.parseInt(request.getParameter("id"));
            if (request.getParameter("all_day") == null) {
                String start_hour = request.getParameter("start_hour");
                String start_minutes = request.getParameter("start_minutes");
                String start_ampm = request.getParameter("start_ampm");
                String end_hour = request.getParameter("end_hour");
                String end_minutes = request.getParameter("end_minutes");
                String end_ampm = request.getParameter("end_ampm");
                if (Integer.parseInt(start_hour) < 10) {
                    start_hour = "0" + start_hour;
                }
                if (Integer.parseInt(end_hour) < 10) {
                    end_hour = "0" + end_hour;
                }
                start_time = start_hour + ":" + start_minutes + " " + start_ampm;
                end_time = end_hour + ":" + end_minutes + " " + end_ampm;
            }
            Event event = null;
            if (!start_time.equals("") && !end_time.equals("")) {
                event = new Event(title, description, month, day, year, start_time, end_time);
            } else {
                event = new Event(title, description, month, day, year);
            }
            if (event.updateEvent(id)) {
                notice = "Calendar event updated!";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error updating calendar event.";
                request.setAttribute("notice", notice);
                request.setAttribute("event", event);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_event.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("delete_event")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Event event = new Event();
            if (event.deleteEvent(id)) {
                notice = "Calendar event successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_events");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting event from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_events");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_members")) {
            String sql = "SELECT * FROM person ORDER BY lname";
            if (request.getParameter("member_type") != null) {
                String member_type = request.getParameter("member_type");
                if (member_type.equals("all")) {
                    sql = "SELECT * FROM person ORDER BY lname";
                } else {
                    sql = "SELECT * FROM person where member_type LIKE '" + member_type + "' ORDER BY lname";
                }
                request.setAttribute("member_type", member_type);
            }
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_members.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving members from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_person")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String member_type = request.getParameter("member_type");
            Person person = new Person();
            person = person.getPerson(id);
            if (member_type.equals("student")) {
                Student student = person.getStudent();
                request.setAttribute("student", student);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_student.jsp");
                dispatcher.forward(request, response);
                return;
            } else if (member_type.equals("alumni")) {
                Alumni alumni = person.getAlumni();
                request.setAttribute("alumni", alumni);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_alumni.jsp");
                dispatcher.forward(request, response);
                return;
            } else if (member_type.equals("hospital")) {
                Hospital hospital = person.getHospital(id);
                request.setAttribute("hospital", hospital);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_hospital.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("update_alumni")) {
            int person_id = Integer.parseInt(request.getParameter("person_id"));
            Person person = new Person();
            person = person.getPerson(person_id);
            Alumni cur_alumni = person.getAlumni();
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String email = request.getParameter("email");
            String company_name = request.getParameter("company_name");
            String position = request.getParameter("position");
            int mentor = 0;
            if (request.getParameter("mentor") != null) {
                mentor = 1;
            }
            String graduation_date = request.getParameter("graduation_year") + "-" + request.getParameter("graduation_month") + "-01";
            String password = "";
            if (request.getParameter("password") != null) {
                password = request.getParameter("password");
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes("UTF-8"));
                } catch (Exception x) {
                    notice = "Could not encrypt your password.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                    dispatcher.forward(request, response);
                    return;
                }
                password = (new BASE64Encoder()).encode(md.digest());
            } else {
                password = cur_alumni.getPassword();
            }
            int is_admin = 0;
            if (request.getParameter("is_admin") != null) {
                is_admin = 1;
            }
            Alumni new_alumni = new Alumni(fname, lname, address1, address2, city, state, zip, email, password, is_admin, company_name, position, graduation_date, mentor);
            if (!new_alumni.getEmail().equals(cur_alumni.getEmail())) {
                if (new_alumni.checkEmailIsRegistered()) {
                    notice = "That email address is already registered!";
                    request.setAttribute("notice", notice);
                    request.setAttribute("alumni", new_alumni);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_alumni.updateAlumni(person_id)) {
                session.setAttribute("alumni", new_alumni);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                dispatcher.forward(request, response);
                return;
            }
            notice = "Member information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("update_hospital")) {
            int person_id = Integer.parseInt(request.getParameter("person_id"));
            Person person = new Person();
            person = person.getPerson(person_id);
            Hospital cur_hospital = person.getHospital(person_id);
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String email = request.getParameter("email");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String url = request.getParameter("url");
            String password = "";
            if (cur_hospital.getPassword() != null) {
                if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
                    password = request.getParameter("password");
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                        md.update(password.getBytes("UTF-8"));
                    } catch (Exception x) {
                        notice = "Could not encrypt your password.  Please try again.";
                        request.setAttribute("notice", notice);
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                        dispatcher.forward(request, response);
                        return;
                    }
                    password = (new BASE64Encoder()).encode(md.digest());
                } else {
                    password = cur_hospital.getPassword();
                }
            }
            int is_admin = 0;
            if (request.getParameter("is_admin") != null) {
                is_admin = 1;
            }
            Hospital new_hospital = new Hospital(fname, lname, address1, address2, city, state, zip, email, password, is_admin, name, phone, url);
            if (!new_hospital.getEmail().equals(cur_hospital.getEmail())) {
                if (new_hospital.checkEmailIsRegistered()) {
                    notice = "That email address is already registered!";
                    request.setAttribute("notice", notice);
                    request.setAttribute("hospital", new_hospital);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_hospital.updateHospital(person_id)) {
                session.setAttribute("hospital", new_hospital);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                dispatcher.forward(request, response);
                return;
            }
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("update_student")) {
            int person_id = Integer.parseInt(request.getParameter("person_id"));
            Person person = new Person();
            person = person.getPerson(person_id);
            Student cur_student = person.getStudent();
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String email = request.getParameter("email");
            String start_date = request.getParameter("start_year") + "-" + request.getParameter("start_month") + "-01";
            String graduation_date = "";
            if (!request.getParameter("grad_year").equals("") && !request.getParameter("grad_month").equals("")) {
                graduation_date = request.getParameter("grad_year") + "-" + request.getParameter("grad_month") + "-01";
            }
            String password = "";
            if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
                password = request.getParameter("password");
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes("UTF-8"));
                } catch (Exception x) {
                    notice = "Could not encrypt your password.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                    dispatcher.forward(request, response);
                    return;
                }
                password = (new BASE64Encoder()).encode(md.digest());
            } else {
                password = cur_student.getPassword();
            }
            int is_admin = 0;
            if (request.getParameter("is_admin") != null) {
                is_admin = 1;
            }
            Student new_student = new Student(fname, lname, address1, address2, city, state, zip, email, password, is_admin, start_date, graduation_date);
            if (!new_student.getEmail().equals(cur_student.getEmail())) {
                if (new_student.checkEmailIsRegistered()) {
                    notice = "That email address is already registered!";
                    request.setAttribute("notice", notice);
                    request.setAttribute("student", new_student);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_student.updateStudent(person_id)) {
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
                dispatcher.forward(request, response);
                return;
            }
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("delete_person")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String member_type = request.getParameter("member_type");
            Person person = new Person();
            person = person.getPerson(id);
            if (person.deletePerson(member_type)) {
                notice = person.getFname() + ' ' + person.getLname() + " successfully deleted from database.";
                request.setAttribute("notice", notice);
                person = null;
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=edit_members&member_type=all");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("manage_pages")) {
            String sql = "SELECT * FROM pages WHERE parent_id=0 OR parent_id IN (SELECT id FROM pages WHERE title LIKE 'root')";
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                sql = "SELECT * FROM pages WHERE parent_id=" + id;
            }
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_pages.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving content pages from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("add_page")) {
            String sql = "SELECT id, title FROM pages WHERE parent_id=0 OR parent_id IN (SELECT id FROM pages WHERE title LIKE 'root')";
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_page.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                notice = "Error retrieving content pages from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("save_page")) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            Page page = null;
            if (request.getParameter("parent_id") != null) {
                int parent_id = Integer.parseInt(request.getParameter("parent_id"));
                page = new Page(title, content, parent_id);
            } else {
                page = new Page(title, content, 0);
            }
            if (page.savePage()) {
                notice = "Content page saved!";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "There was an error saving the page.";
                request.setAttribute("page", page);
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_page.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_page")) {
            String sql = "SELECT * FROM pages WHERE parent_id=0";
            int id = Integer.parseInt(request.getParameter("id"));
            Page page = new Page();
            page = page.getPage(id);
            try {
                dbStatement = connection.createStatement();
                dbResultSet = dbStatement.executeQuery(sql);
                request.setAttribute("resultset", dbResultSet);
            } catch (SQLException e) {
                notice = "Error retrieving content pages from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
            if (page != null) {
                request.setAttribute("page", page);
                request.setAttribute("id", Integer.toString(id));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_page.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error retrieving content page from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("update_page")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            int parent_id = 0;
            if (request.getParameter("parent_id") != null) {
                parent_id = Integer.parseInt(request.getParameter("parent_id"));
            }
            Page page = new Page(title, content, parent_id);
            if (page.updatePage(id)) {
                notice = "Content page was updated successfully.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error updating the content page.";
                request.setAttribute("notice", notice);
                request.setAttribute("page", page);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_page.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("delete_page")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Page page = new Page();
            if (page.deletePage(id)) {
                notice = "Content page (and sub pages) deleted successfully.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the content page(s).";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("list_residencies")) {
            Residency residency = new Residency();
            dbResultSet = residency.getResidencies();
            request.setAttribute("result", dbResultSet);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/list_residencies.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("delete_residency")) {
            int job_id = Integer.parseInt(request.getParameter("id"));
            Residency residency = new Residency();
            if (residency.deleteResidency(job_id)) {
                notice = "Residency has been successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=list_residencies");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the residency.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit_residency")) {
            int job_id = Integer.parseInt(request.getParameter("id"));
            Residency residency = new Residency();
            dbResultSet = residency.getResidency(job_id);
            if (dbResultSet != null) {
                try {
                    int hId = dbResultSet.getInt("hospital_id");
                    String hName = residency.getHospitalName(hId);
                    request.setAttribute("hName", hName);
                    dbResultSet.beforeFirst();
                } catch (SQLException e) {
                    error = "There was an error retreiving the residency.";
                    session.setAttribute("error", error);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/edit_residency.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "There was an error in locating the residency you selected.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("new_residency")) {
            Residency residency = new Residency();
            dbResultSet = residency.getHospitals();
            request.setAttribute("result", dbResultSet);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/add_residency.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("add_residency")) {
            Person person = (Person) session.getAttribute("person");
            if (person.isAdmin()) {
                String hName = request.getParameter("hName");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String start_month = request.getParameter("startDateMonth");
                String start_day = request.getParameter("startDateDay");
                String start_year = request.getParameter("startDateYear");
                String start_date = start_year + start_month + start_day;
                String end_month = request.getParameter("endDateMonth");
                String end_day = request.getParameter("endDateDay");
                String end_year = request.getParameter("endDateYear");
                String end_date = end_year + end_month + end_day;
                String deadline_month = request.getParameter("deadlineDateMonth");
                String deadline_day = request.getParameter("deadlineDateDay");
                String deadline_year = request.getParameter("deadlineDateYear");
                String deadline_date = deadline_year + deadline_month + deadline_day;
                int hId = Integer.parseInt(request.getParameter("hId"));
                Residency residency = new Residency(title, start_date, end_date, deadline_date, description, hId);
                if (residency.saveResidency()) {
                    notice = "Residency has been successfully saved.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=list_residencies");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "Error saving the residency.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("update_residency")) {
            Person person = (Person) session.getAttribute("person");
            int job_id = Integer.parseInt(request.getParameter("job_id"));
            if (person.isAdmin()) {
                String hName = request.getParameter("hName");
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String start_month = request.getParameter("startDateMonth");
                String start_day = request.getParameter("startDateDay");
                String start_year = request.getParameter("startDateYear");
                String start_date = start_year + start_month + start_day;
                String end_month = request.getParameter("endDateMonth");
                String end_day = request.getParameter("endDateDay");
                String end_year = request.getParameter("endDateYear");
                String end_date = end_year + end_month + end_day;
                String deadline_month = request.getParameter("deadlineDateMonth");
                String deadline_day = request.getParameter("deadlineDateDay");
                String deadline_year = request.getParameter("deadlineDateYear");
                String deadline_date = deadline_year + deadline_month + deadline_day;
                int hId = Integer.parseInt(request.getParameter("hId"));
                Residency residency = new Residency(job_id, title, start_date, end_date, deadline_date, description);
                if (residency.updateResidency(job_id)) {
                    notice = "Residency has been successfully saved.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=list_residencies");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "Error saving the residency.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("add_hospital")) {
            Person person = (Person) session.getAttribute("person");
            if (person.isAdmin()) {
                String name = request.getParameter("name");
                String url = request.getParameter("url");
                String address1 = request.getParameter("address1");
                String address2 = request.getParameter("address2");
                String city = request.getParameter("city");
                String state = request.getParameter("state");
                String zip = request.getParameter("zip");
                String phone = request.getParameter("phone");
                String lname = request.getParameter("name");
                Hospital hospital = new Hospital(lname, address1, address2, city, state, zip, name, phone, url);
                if (!hospital.saveHospitalAdmin()) {
                    notice = "There was an error saving your information.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=new_residency");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Unknown request.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Get Admin News List")) {
            News news = new News();
            if (news.getNewsList() != null) {
                dbResultSet = news.getNewsList();
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/list.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not get news list.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Get News List")) {
            News news = new News();
            if (news.getNewsList() != null) {
                dbResultSet = news.getNewsList();
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/news_list.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not get news list.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/gsu_fhce/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("detail")) {
            String id = request.getParameter("id");
            News news = new News();
            if (news.getNewsDetail(id) != null) {
                dbResultSet = news.getNewsDetail(id);
                request.setAttribute("result", dbResultSet);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/news_detail.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not get news detail.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("delete")) {
            int id = 0;
            id = Integer.parseInt(request.getParameter("id"));
            News news = new News();
            if (news.deleteNews(id)) {
                notice = "News successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the news.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            News news = new News();
            news = news.getNews(id);
            if (news != null) {
                request.setAttribute("news", news);
                request.setAttribute("id", Integer.toString(id));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/news_update.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error retrieving news from the database.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Update News")) {
            String title = request.getParameter("title");
            String date = (request.getParameter("year")) + (request.getParameter("month")) + (request.getParameter("day"));
            String content = request.getParameter("content");
            int id = Integer.parseInt(request.getParameter("newsid"));
            News news = new News(title, date, content);
            if (news.updateNews(id)) {
                notice = "News successfully updated.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not update news.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("Add News")) {
            String id = "";
            String title = request.getParameter("title");
            String date = request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day");
            String content = request.getParameter("content");
            News news = new News(title, date, content);
            if (news.addNews()) {
                notice = "News successfully added.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=Get Admin News List");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Could not add news.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("admin/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("manage_mship")) {
            Mentor mentor = new Mentor();
            dbResultSet = mentor.getMentorships();
            if (dbResultSet != null) {
                request.setAttribute("result", dbResultSet);
            } else {
                notice = "There are no current mentorships.";
                request.setAttribute("notice", notice);
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/list_mentorships.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("delete_mship")) {
            int mentorship_id = Integer.parseInt(request.getParameter("id"));
            Mentor mentor = new Mentor();
            if (mentor.delMentorship(mentorship_id)) {
                notice = "Mentorship successfully deleted.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=manage_mship");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "Error deleting the mentorship.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=manage_mship");
                dispatcher.forward(request, response);
                return;
            }
        } else if (action.equals("new_mship")) {
            Mentor mentor = new Mentor();
            ResultSet alumnis = null;
            ResultSet students = null;
            alumnis = mentor.getAlumnis();
            students = mentor.getStudents();
            request.setAttribute("alumni_result", alumnis);
            request.setAttribute("student_result", students);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/create_mship.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("create_mship")) {
            int student_id = Integer.parseInt(request.getParameter("student_id"));
            int alumni_id = Integer.parseInt(request.getParameter("alumni_id"));
            Mentor mentor = new Mentor();
            if (mentor.addMentorship(student_id, alumni_id)) {
                notice = "Mentorship successfully created.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin?action=manage_mship");
                dispatcher.forward(request, response);
                return;
            } else {
                notice = "There was an error creating the mentorship.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin/create_mship.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
    }
