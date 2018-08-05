    private MailBox authenticate() throws Exception {
        String line = "POP3 Server Ready" + ((mChallenge == null) ? "" : (" " + mChallenge));
        MailBox mailbox = null;
        mStream.writeOk(line);
        while (mailbox == null) {
            line = mStream.readLine();
            if (isQuit(line)) break;
            String body;
            if ((body = isCommand(line, "USER")) != null) {
                String user = body;
                MailBox mb = mRepository.getMailBox(user);
                if (mb == null) mStream.writeErr("No mailbox for: " + line); else {
                    mStream.writeOk();
                    String pswd, line2 = mStream.readLine();
                    if (isQuit(line2)) break;
                    if ((pswd = isCommand(line2, "PASS")) == null) mStream.writeErr("Expected PASS but got {" + line2 + "}"); else {
                        String realPswd = mb.getPassword();
                        if ((realPswd != null) ? pswd.equals(realPswd) : mb.checkPassword(pswd)) {
                            mailbox = mb;
                            mStream.writeOk();
                        } else mStream.writeErr("Bad password for " + user);
                    }
                }
            } else if ((body = isCommand(line, "APOP")) != null) {
                if (mChallenge == null) mStream.writeErr("APOP Not Supported"); else {
                    int space = body.indexOf(' ');
                    if (space < 0) mStream.writeErr("Bad APOP command {" + line + "}"); else {
                        String user = body.substring(0, space);
                        String hash = body.substring(space + 1);
                        MailBox mb = mRepository.getMailBox(user);
                        if (mb == null) mStream.writeErr("No mailbox for: " + line); else if (mb.getPassword() == null) mStream.writeErr("User(" + user + ") cannot login using APOP"); else {
                            String secret = mChallenge + mb.getPassword();
                            MessageDigest md5 = MessageDigest.getInstance("MD5");
                            md5.update(secret.getBytes("UTF-8"));
                            byte[] digest = md5.digest();
                            String digestStr = new String(Hex.encodeHex(digest));
                            if (hash.equalsIgnoreCase(digestStr)) {
                                mailbox = mb;
                                mStream.writeOk();
                            } else mStream.writeErr("Bad password for " + user);
                        }
                    }
                }
            } else mStream.writeErr("Authentication required (got '" + line + "')");
        }
        return mailbox;
    }
