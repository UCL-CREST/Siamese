    public void onCreate() {
        window = ((Window) getFellow("win"));
        userName = ((Textbox) getFellow("user"));
        password = ((Textbox) getFellow("password"));
        session = window.getDesktop().getSession();
        if (Executions.getCurrent().getParameter("login") != null) {
            login = Executions.getCurrent().getParameter("login");
            session.setAttribute("login", login);
        }
        if (Executions.getCurrent().getParameter("password") != null) {
            passwordu = Executions.getCurrent().getParameter("password");
        }
        if (Executions.getCurrent().getParameter("option") != null) {
            option = Executions.getCurrent().getParameter("option");
            session.setAttribute("option", option);
        }
        if (Executions.getCurrent().getParameter("organization") != null) {
            organization = Executions.getCurrent().getParameter("organization");
            session.setAttribute("organization", organization);
        }
        if (Executions.getCurrent().getParameter("project") != null) {
            project = Executions.getCurrent().getParameter("project");
            session.setAttribute("project", project);
        }
        if (login != null) {
            User user = UserDAO.getUserByUserName(login);
            if (user != null) {
                String encodedPassword = null;
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    digest.update(user.getPassword().getBytes());
                    BASE64Encoder encoder = new BASE64Encoder();
                    encodedPassword = encoder.encode(digest.digest());
                } catch (Exception e) {
                }
                if (passwordu.compareTo(encodedPassword) == 0) {
                    session.setAttribute("user", user);
                    session.setAttribute("numero", 5);
                    session.setAttribute("option", option);
                    session.setAttribute("organization", organization);
                    session.setAttribute("project", project);
                    Executions.sendRedirect("menu.zul");
                }
            }
        }
    }
