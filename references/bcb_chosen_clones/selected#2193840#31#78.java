    public void sendReminder(Calendar firstDay, Connection connection, boolean reallySend, boolean forceSend, Session mailSession, String adminEmail) throws ReminderException {
        if (firstDay.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            throw new ReminderException("Firstday should be Monday");
        }
        Calendar lastDay = Calendar.getInstance();
        lastDay.setTimeInMillis(firstDay.getTimeInMillis());
        lastDay.add(Calendar.DAY_OF_MONTH, 7);
        Calendar activeOn = Calendar.getInstance();
        activeOn.setTimeInMillis(firstDay.getTimeInMillis());
        activeOn.set(Calendar.DAY_OF_MONTH, 1);
        activeOn.add(Calendar.MONTH, 1);
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("First day: " + firstDay.getTime());
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("Last day: " + lastDay.getTime());
        Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").fine("ActiveOn: " + activeOn.getTime());
        List<UserTO> incompleteUsers = processUsers(connection, firstDay.getTime(), lastDay.getTime(), activeOn.getTime(), forceSend);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        for (UserTO user : incompleteUsers) {
            System.out.println("User " + user.getUserid() + " (" + user.getFirstName() + " " + user.getLastName() + ")" + " has incomplete timesheets");
            if (reallySend) {
                if ((user.getEmail() != null) && (user.getEmail().length() > 0)) {
                    Message message = new MimeMessage(mailSession);
                    try {
                        message.setFrom(new InternetAddress(adminEmail));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(adminEmail));
                        message.setSubject("Incomplete timesheets");
                        StringBuffer sb = new StringBuffer();
                        sb.append("Dear ");
                        sb.append(user.getFirstName());
                        sb.append(",\n\n");
                        sb.append("Your timesheets for the week of ");
                        sb.append(sdf.format(firstDay.getTime()));
                        sb.append(" are incomplete, which means either that you have reported hours for less than 4 days ");
                        sb.append(" or that the total number of hours for the week is below 30. ");
                        sb.append(" Could you please fill in the missing data?\n\n");
                        sb.append("Thanks a lot\n\n");
                        sb.append("The Timesheets administrators");
                        message.setText(sb.toString());
                        Transport.send(message);
                    } catch (MessagingException ex) {
                        System.err.println("Cannot send email. " + ex);
                    }
                } else {
                    Logger.getLogger("iTimesheets.EmployeeWeeklyReminder").warning("User " + user.getUserid() + "(" + user.getFirstName() + " " + user.getLastName() + ")" + " has no email");
                }
            }
        }
    }
