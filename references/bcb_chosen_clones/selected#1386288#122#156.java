    public void sendMailToAdmin(UserBean user) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "cse.mrt.ac.lk");
            Session session1 = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session1);
            message.setFrom(new InternetAddress("lifproject-admin@cse.mrt.ac.lk"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("niranjan.uom@gmail.com", false));
            message.setSubject("News: LIF User details");
            StringBuffer messageText = new StringBuffer();
            String lineBreak = "<br>";
            messageText.append(lineBreak).append(" User Details are :\n\n");
            messageText.append(lineBreak).append(" LoginName    : " + user.getLoginName());
            messageText.append(lineBreak).append(" Password     : " + user.getPassword());
            messageText.append(lineBreak).append(" Organization : " + user.getOrganization());
            messageText.append(lineBreak).append(" Telephone    : " + user.getTelephone1());
            messageText.append(lineBreak).append(" Email        : " + user.getEmail());
            messageText.append(lineBreak).append(" Contact Name : " + user.getContactName1());
            Set<SecurityRole> roles = user.getRoles();
            if (user.getRoles() != null) {
                messageText.append(lineBreak).append(" Requested Roles are: ");
                for (SecurityRole role : roles) {
                    messageText.append(role.name()).append(lineBreak);
                }
                messageText.append(lineBreak).append(" Requested profile     : " + user.getProfileName());
            }
            message.setContent(messageText.toString(), "text/html;charset=utf-8");
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
