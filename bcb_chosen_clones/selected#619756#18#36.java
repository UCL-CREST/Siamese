    public void run(Connection source, BNetUser user, String param, String[] params, boolean whisperBack, Account commanderAccount, boolean superUser) throws Exception {
        if (commanderAccount == null) throw new CommandFailedWithDetailsException("You must have an account to use setbirthday.");
        try {
            if (params == null) throw new InvalidUseException();
            Date bd = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/y");
                bd = sdf.parse(param);
            } catch (Exception e) {
                Out.exception(e);
            }
            if (bd == null) throw new InvalidUseException();
            commanderAccount.setBirthday(new java.sql.Date(bd.getTime()));
            commanderAccount.updateRow();
            user.sendChat("Your birthday has been set to [ " + new SimpleDateFormat("M/d/y").format(bd) + " ]", whisperBack);
        } catch (InvalidUseException e) {
            user.sendChat("Use: %trigger%setbirthday <date:MM/DD/YY>", whisperBack);
        }
    }
