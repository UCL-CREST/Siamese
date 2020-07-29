    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("edit") != null) {
            try {
                User cu = (User) request.getSession().getAttribute("currentuser");
                UserDetails ud = cu.getUserDetails();
                String returnTo = "editprofile.jsp";
                if (!request.getParameter("password").equals("")) {
                    String password = request.getParameter("password");
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(new String(password).getBytes());
                    byte[] hash = md.digest();
                    String pass = new BigInteger(1, hash).toString(16);
                    cu.setClientPassword(pass);
                }
                ud.setFirstName(request.getParameter("fname"));
                ud.setLastName(request.getParameter("lname"));
                ud.setEmailAddress(request.getParameter("email"));
                ud.setAddress(request.getParameter("address"));
                ud.setZipcode(request.getParameter("zipcode"));
                ud.setTown(request.getParameter("town"));
                ud.setCountry(request.getParameter("country"));
                ud.setTrackingColor(request.getParameter("input1"));
                String vis = request.getParameter("visibility");
                if (vis.equals("self")) {
                    cu.setVisibility(0);
                } else if (vis.equals("friends")) {
                    cu.setVisibility(1);
                } else if (vis.equals("all")) {
                    cu.setVisibility(2);
                } else {
                    response.sendRedirect("error.jsp?id=8");
                }
                em.getTransaction().begin();
                em.persist(cu);
                em.getTransaction().commit();
                response.sendRedirect(returnTo);
            } catch (Throwable e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp?id=5");
            }
            return;
        }
    }
