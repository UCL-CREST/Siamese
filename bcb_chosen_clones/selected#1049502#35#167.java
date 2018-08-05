    public ActionForward dbExecute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws DatabaseException {
        Integer key;
        SubmitUserForm form = (SubmitUserForm) pForm;
        if (pRequest.getParameter("key") == null) {
            key = form.getPrimaryKey();
        } else {
            key = Integer.parseInt(pRequest.getParameter("key"));
        }
        User currentUser = (User) (pRequest.getSession().getAttribute("login"));
        if ((currentUser == null) || (!currentUser.getAdminRights() && (currentUser.getPrimaryKey() != key))) {
            return (pMapping.findForward("denied"));
        }
        if (currentUser.getAdminRights()) {
            pRequest.setAttribute("isAdmin", new Boolean(true));
        }
        if (currentUser.getPDFRights()) {
            pRequest.setAttribute("pdfRights", Boolean.TRUE);
        }
        User user = database.acquireUserByPrimaryKey(key);
        if (user.isSuperAdmin() && !currentUser.isSuperAdmin()) {
            return (pMapping.findForward("denied"));
        }
        pRequest.setAttribute("user", user);
        pRequest.setAttribute("taxonomy", database.acquireTaxonomy());
        if (form.getAction().equals("none")) {
            form.setPrimaryKey(user.getPrimaryKey());
        }
        if (form.getAction().equals("edit")) {
            FormError formError = form.validateFields();
            if (formError != null) {
                if (formError.getFormFieldErrors().get("firstName") != null) {
                    pRequest.setAttribute("FirstNameBad", new Boolean(true));
                }
                if (formError.getFormFieldErrors().get("lastName") != null) {
                    pRequest.setAttribute("LastNameBad", new Boolean(true));
                }
                if (formError.getFormFieldErrors().get("emailAddress") != null) {
                    pRequest.setAttribute("EmailAddressBad", new Boolean(true));
                }
                if (formError.getFormFieldErrors().get("mismatchPassword") != null) {
                    pRequest.setAttribute("mismatchPassword", new Boolean(true));
                }
                if (formError.getFormFieldErrors().get("shortPassword") != null) {
                    pRequest.setAttribute("shortPassword", new Boolean(true));
                }
                return (pMapping.findForward("invalid"));
            }
            user.setFirstName(form.getFirstName());
            user.setLastName(form.getLastName());
            user.setEmailAddress(form.getEmailAddress());
            if (!form.getFirstPassword().equals("")) {
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    throw new DatabaseException("Could not hash password for storage: no such algorithm");
                }
                try {
                    md.update(form.getFirstPassword().getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new DatabaseException("Could not hash password for storage: no such encoding");
                }
                user.setPassword((new BASE64Encoder()).encode(md.digest()));
            }
            user.setTitle(form.getTitle());
            user.setDegree(form.getDegree());
            user.setAddress(form.getAddress());
            user.setNationality(form.getNationality());
            user.setLanguages(form.getLanguages());
            user.setHomepage(form.getHomepage());
            user.setInstitution(form.getInstitution());
            if (pRequest.getParameter("hideEmail") != null) {
                if (pRequest.getParameter("hideEmail").equals("on")) {
                    user.setHideEmail(true);
                }
            } else {
                user.setHideEmail(false);
            }
            User storedUser = database.acquireUserByPrimaryKey(user.getPrimaryKey());
            if (currentUser.isSuperAdmin()) {
                if (pRequest.getParameter("admin") != null) {
                    user.setAdminRights(true);
                } else {
                    if (!storedUser.isSuperAdmin()) {
                        user.setAdminRights(false);
                    }
                }
            } else {
                user.setAdminRights(storedUser.getAdminRights());
            }
            if (currentUser.isAdmin()) if (pRequest.getParameter("PDFRights") != null) user.setPDFRights(true); else user.setPDFRights(false);
            if (currentUser.isAdmin()) {
                if (!storedUser.isAdmin() || !storedUser.isSuperAdmin()) {
                    if (pRequest.getParameter("active") != null) {
                        user.setActive(true);
                    } else {
                        user.setActive(false);
                    }
                } else {
                    user.setActive(storedUser.getActive());
                }
            }
            if (currentUser.isAdmin() || currentUser.isSuperAdmin()) {
                String[] categories = pRequest.getParameterValues("categories");
                user.setModeratorRights(new Categories());
                if (categories != null) {
                    try {
                        for (int i = 0; i < categories.length; i++) {
                            Integer catkey = Integer.parseInt(categories[i]);
                            Category cat = database.acquireCategoryByPrimaryKey(catkey);
                            user.getModeratorRights().add(cat);
                        }
                    } catch (NumberFormatException nfe) {
                        throw new DatabaseException("Invalid category key.");
                    }
                }
            }
            if (!currentUser.isAdmin() && !currentUser.isSuperAdmin()) {
                user.setAdminRights(false);
                user.setSuperAdminRights(false);
            }
            database.updateUser(user);
            if (currentUser.getPrimaryKey() == user.getPrimaryKey()) {
                pRequest.getSession().setAttribute("login", user);
            }
            pRequest.setAttribute("helpKey", key);
            if (currentUser.isAdmin() || currentUser.isSuperAdmin()) {
                return (pMapping.findForward("adminfinished"));
            }
            return (pMapping.findForward("finished"));
        }
        return (pMapping.findForward("success"));
    }
