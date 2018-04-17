    public ActionForward dbExecute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws DatabaseException {
        SubmitRegistrationForm newUserData = (SubmitRegistrationForm) pForm;
        if (!newUserData.getAcceptsEULA()) {
            pRequest.setAttribute("acceptsEULA", new Boolean(true));
            pRequest.setAttribute("noEula", new Boolean(true));
            return (pMapping.findForward("noeula"));
        }
        if (newUserData.getAction().equals("none")) {
            newUserData.setAction("submit");
            pRequest.setAttribute("UserdataBad", new Boolean(true));
            return (pMapping.findForward("success"));
        }
        boolean userDataIsOk = true;
        if (newUserData == null) {
            return (pMapping.findForward("failure"));
        }
        if (newUserData.getLastName().length() < 2) {
            userDataIsOk = false;
            pRequest.setAttribute("LastNameBad", new Boolean(true));
        }
        if (newUserData.getFirstName().length() < 2) {
            userDataIsOk = false;
            pRequest.setAttribute("FirstNameBad", new Boolean(true));
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(newUserData.getEmailAddress())) {
            pRequest.setAttribute("EmailBad", new Boolean(true));
            userDataIsOk = false;
        } else {
            if (database.acquireUserByEmail(newUserData.getEmailAddress()) != null) {
                pRequest.setAttribute("EmailDouble", new Boolean(true));
                userDataIsOk = false;
            }
        }
        if (newUserData.getFirstPassword().length() < 5) {
            userDataIsOk = false;
            pRequest.setAttribute("FirstPasswordBad", new Boolean(true));
        }
        if (newUserData.getSecondPassword().length() < 5) {
            userDataIsOk = false;
            pRequest.setAttribute("SecondPasswordBad", new Boolean(true));
        }
        if (!newUserData.getSecondPassword().equals(newUserData.getFirstPassword())) {
            userDataIsOk = false;
            pRequest.setAttribute("PasswordsNotEqual", new Boolean(true));
        }
        if (userDataIsOk) {
            User newUser = new User();
            newUser.setFirstName(newUserData.getFirstName());
            newUser.setLastName(newUserData.getLastName());
            if (!newUserData.getFirstPassword().equals("")) {
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    throw new DatabaseException("Could not hash password for storage: no such algorithm");
                }
                try {
                    md.update(newUserData.getFirstPassword().getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new DatabaseException("Could not hash password for storage: no such encoding");
                }
                newUser.setPassword((new BASE64Encoder()).encode(md.digest()));
            }
            newUser.setEmailAddress(newUserData.getEmailAddress());
            newUser.setHomepage(newUserData.getHomepage());
            newUser.setAddress(newUserData.getAddress());
            newUser.setInstitution(newUserData.getInstitution());
            newUser.setLanguages(newUserData.getLanguages());
            newUser.setDegree(newUserData.getDegree());
            newUser.setNationality(newUserData.getNationality());
            newUser.setTitle(newUserData.getTitle());
            newUser.setActive(true);
            try {
                database.updateUser(newUser);
            } catch (DatabaseException e) {
                pRequest.setAttribute("UserstoreBad", new Boolean(true));
                return (pMapping.findForward("success"));
            }
            pRequest.setAttribute("UserdataFine", new Boolean(true));
        } else {
            pRequest.setAttribute("UserdataBad", new Boolean(true));
        }
        return (pMapping.findForward("success"));
    }
