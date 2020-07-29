    public static void sendErrorMessage(String id, String msg) {
        Session session = null;
        String destinations = null;
        String sender = null;
        {
            NamingException nex = null;
            try {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");
                session = (Session) envCtx.lookup("ifw2/error/mailSession");
                destinations = (String) envCtx.lookup("ifw2/error/destinations");
                sender = (String) envCtx.lookup("ifw2/error/sender");
            } catch (NamingException ex) {
                nex = ex;
            }
            if (session == null || destinations == null) {
                if (session == null) LOGGER.error("Cannot notify exception: " + id + ", ifw2/error/mailSession is not defined " + ((nex == null) ? "" : nex.getMessage())); else LOGGER.error("Cannot notify exception: " + id + ", ifw2/error/destinations is not defined " + ((nex == null) ? "" : nex.getMessage()));
                LOGGER.error(msg);
                return;
            }
        }
        final String ENCODING = "ISO8859_1";
        MimeMessage message = new MimeMessage(session);
        try {
            if (sender != null) message.setFrom(new InternetAddress(sender));
            {
                String tk;
                for (StringTokenizer st = new StringTokenizer(destinations, ";, \t\n\r\f"); st.hasMoreTokens(); ) {
                    tk = st.nextToken();
                    message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(tk));
                }
            }
            message.setSubject("Error id: " + id);
            message.setText(msg, ENCODING);
            Transport.send(message);
            LOGGER.error("Error notification " + id + " sended:");
            LOGGER.error(msg);
        } catch (Exception ex) {
            LOGGER.error("Cannot notify exception: " + id + " ...");
            LOGGER.error(msg);
            LOGGER.error("... an error occurred during e-mail delivery:", ex);
        }
    }
