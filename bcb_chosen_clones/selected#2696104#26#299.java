    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(300);
        String member_type;
        String save_type;
        String action;
        String notice;
        if ((String) session.getAttribute("member_type") != null) {
            member_type = (String) session.getAttribute("member_type");
        } else {
            notice = "You must login first!";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        }
        if (request.getParameter("action") != null) {
            action = (String) request.getParameter("action");
        } else {
            if (member_type.equals("student")) {
                if (session.getAttribute("person") != null) {
                    Person person = (Person) session.getAttribute("person");
                    Student student = person.getStudent();
                    request.setAttribute("student", student);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "You are not logged in!";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else if (member_type.equals("alumni")) {
                if (session.getAttribute("person") != null) {
                    Person person = (Person) session.getAttribute("person");
                    Alumni alumni = person.getAlumni();
                    request.setAttribute("alumni", alumni);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "You are not logged in!";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else if (member_type.equals("hospital")) {
                if (session.getAttribute("person") != null) {
                    Person person = (Person) session.getAttribute("person");
                    Hospital hospital = person.getHospital();
                    request.setAttribute("hospital", hospital);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    notice = "You are not logged in!";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else {
                notice = "I don't understand your request.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
        if (action.equals("save_student")) {
            Person person = (Person) session.getAttribute("person");
            Student cur_student = person.getStudent();
            int person_id = Integer.parseInt((String) session.getAttribute("person_id"));
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_student.updateStudent(person_id)) {
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/student.jsp");
                dispatcher.forward(request, response);
                return;
            }
            Person updated_person = new_student.getPerson(person_id);
            session.setAttribute("person", updated_person);
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("save_alumni")) {
            Person person = (Person) session.getAttribute("person");
            Alumni cur_alumni = person.getAlumni();
            int person_id = Integer.parseInt((String) session.getAttribute("person_id"));
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_alumni.updateAlumni(person_id)) {
                session.setAttribute("alumni", new_alumni);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/alumni.jsp");
                dispatcher.forward(request, response);
                return;
            }
            Person updated_person = new_alumni.getPerson(person_id);
            session.setAttribute("person", updated_person);
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else if (action.equals("save_hospital")) {
            Person person = (Person) session.getAttribute("person");
            Hospital cur_hospital = person.getHospital();
            int person_id = Integer.parseInt((String) session.getAttribute("person_id"));
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
            if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
                password = request.getParameter("password");
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes("UTF-8"));
                } catch (Exception x) {
                    notice = "Could not encrypt your password.  Please try again.";
                    request.setAttribute("notice", notice);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                password = (new BASE64Encoder()).encode(md.digest());
            } else {
                password = cur_hospital.getPassword();
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (!new_hospital.updateHospital(person_id)) {
                session.setAttribute("hospital", new_hospital);
                notice = "There was an error saving your information.  Please try again.";
                request.setAttribute("notice", notice);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
                dispatcher.forward(request, response);
                return;
            }
            Person updated_person = new_hospital.getPerson(person_id);
            session.setAttribute("person", updated_person);
            notice = "Information successfully updated.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        } else {
            notice = "There was an error with your request.  Please try again.";
            request.setAttribute("notice", notice);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/member/hospital.jsp");
            dispatcher.forward(request, response);
            return;
        }
    }
