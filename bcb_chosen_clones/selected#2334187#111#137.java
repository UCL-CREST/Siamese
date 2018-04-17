    public boolean resetPassword(String email) {
        PersistenceManager pm = PMF.getPersistenceManager();
        User user = getUserByEmail(email, pm);
        if (user != null) {
            Random randomGenerator = new Random();
            String password = Integer.toString(randomGenerator.nextInt(100000));
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            String msgBody = "This is your new password at Mathive Playground: " + password;
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("mackanhedvall@gmail.com", "Mathive Playground"));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress("amaeha-6@student.ltu.se"));
                msg.setSubject("Your password has been reset");
                msg.setText(msgBody);
                Transport.send(msg);
                user.setHashPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                return true;
            } catch (AddressException e) {
            } catch (MessagingException e) {
            } catch (UnsupportedEncodingException e) {
            } finally {
                pm.close();
            }
        }
        return false;
    }
