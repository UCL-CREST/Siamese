    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        String format = req.getParameter("format");
        EntityManager em = EMF.get().createEntityManager();
        String uname = (req.getParameter("uname") == null) ? "" : req.getParameter("uname");
        String passwd = (req.getParameter("passwd") == null) ? "" : req.getParameter("passwd");
        String name = (req.getParameter("name") == null) ? "" : req.getParameter("name");
        String email = (req.getParameter("email") == null) ? "" : req.getParameter("email");
        if (uname == null || uname.equals("") || uname.length() < 4) {
            if (format != null && format.equals("xml")) resp.getWriter().print(Error.unameTooShort(uname).toXML(em)); else resp.getWriter().print(Error.unameTooShort(uname).toJSON(em));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (User.fromUserName(em, uname) != null) {
            if (format != null && format.equals("xml")) resp.getWriter().print(Error.userExists(uname).toXML(em)); else resp.getWriter().print(Error.userExists(uname).toJSON(em));
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            em.close();
            return;
        }
        if (passwd.equals("") || passwd.length() < 6) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (format != null && format.equals("xml")) resp.getWriter().print(Error.passwdTooShort(uname).toXML(em)); else resp.getWriter().print(Error.passwdTooShort(uname).toJSON(em));
            em.close();
            return;
        }
        User u = new User();
        u.setUsername(uname);
        u.setPasswd(passwd);
        u.setName(name);
        u.setEmail(email);
        u.setPaid(false);
        StringBuffer apikey = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            String api = System.nanoTime() + "" + System.identityHashCode(this) + "" + uname;
            algorithm.update(api.getBytes());
            byte[] digest = algorithm.digest();
            for (int i = 0; i < digest.length; i++) {
                apikey.append(Integer.toHexString(0xFF & digest[i]));
            }
        } catch (NoSuchAlgorithmException e) {
            resp.setStatus(500);
            if (format != null && format.equals("xml")) resp.getWriter().print(Error.unknownError().toXML(em)); else resp.getWriter().print(Error.unknownError().toJSON(em));
            log.severe(e.toString());
            em.close();
            return;
        }
        u.setApiKey(apikey.toString());
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(u);
            tx.commit();
        } catch (Throwable t) {
            log.severe("Error adding user " + uname + " Reason:" + t.getMessage());
            tx.rollback();
            resp.setStatus(500);
            if (format != null && format.equals("xml")) resp.getWriter().print(Error.unknownError().toXML(em)); else resp.getWriter().print(Error.unknownError().toJSON(em));
            return;
        }
        log.info("User " + u.getName() + " was created successfully");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        if (format != null && format.equals("xml")) resp.getWriter().print(u.toXML(em)); else resp.getWriter().print(u.toJSON(em));
        em.close();
    }
