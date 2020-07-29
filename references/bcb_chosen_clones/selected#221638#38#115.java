    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String month = "";
        String year = "";
        GregorianCalendar calendar = new GregorianCalendar();
        month = String.valueOf(calendar.get(GregorianCalendar.MONTH));
        year = String.valueOf(calendar.get(GregorianCalendar.YEAR));
        Date today = new Date();
        if (today.getDate() == 2) {
            String msgBody = "Dear admin of CVM Turkey Team" + "<br /><br />Please find the KPI data of month " + month + " - " + year + " .<br /><br />" + "Best wishes.<br /><br />";
            String html = "<table width=\"400px\"><tr><td></td><td></td><td><b>KOL visit</b></td><td><b>Presentation</b></td><td><b>Training</b></td></tr>";
            List<CreasusUser> creasusUsersList = new ArrayList<CreasusUser>();
            List<Entry> entryList = new ArrayList<Entry>();
            int totalKolVisit = 0;
            int totalPresentation = 0;
            int totalTraining = 0;
            creasusUsersList = getCreasusUserList();
            Collections.sort(creasusUsersList, new Comparator() {

                public int compare(Object o1, Object o2) {
                    CreasusUser p1 = (CreasusUser) o1;
                    CreasusUser p2 = (CreasusUser) o2;
                    return p1.getName().compareToIgnoreCase(p2.getName());
                }
            });
            entryList = getEntryList();
            for (Iterator iterator = creasusUsersList.iterator(); iterator.hasNext(); ) {
                CreasusUser creasusUser = (CreasusUser) iterator.next();
                if (creasusUser.isActive()) {
                    String name = "";
                    String surname = "";
                    if (creasusUser.getName() != null) {
                        name = creasusUser.getName();
                    }
                    if (creasusUser.getSurname() != null) {
                        surname = creasusUser.getSurname();
                    }
                    int kolvisit = 0;
                    int presentation = 0;
                    int training = 0;
                    List<Entry> usersEntryList = getUsersEntries(creasusUser.getEmail(), entryList);
                    for (Iterator iterator2 = usersEntryList.iterator(); iterator2.hasNext(); ) {
                        Entry entry = (Entry) iterator2.next();
                        if (entry.getMode() == 0) {
                            kolvisit = entry.getMonthlyValues()[today.getMonth() - 1];
                            totalKolVisit += kolvisit;
                        } else if (entry.getMode() == 1) {
                            presentation = entry.getMonthlyValues()[today.getMonth() - 1];
                            totalPresentation += presentation;
                        } else if (entry.getMode() == 2) {
                            training = entry.getMonthlyValues()[today.getMonth() - 1];
                            totalTraining += training;
                        }
                    }
                    html += "<tr><td><b>" + name + "</b></td><td><b>" + surname + "</b></td><td>" + kolvisit + "</td><td>" + presentation + "</td><td>" + training + "</td></tr>";
                }
            }
            html += "<tr><td></td><td><b>TOTAL</b></td><td><b>" + totalKolVisit + "</b></td><td><b>" + totalPresentation + "</b></td><td><b>" + totalTraining + "</b></td></tr></table>";
            msgBody += html;
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("cvmtmt@gmail.com", "CVM Turkey Medical Team"));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress("kemal.kendir@novartis.com", "Kemal Kendir"));
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress("mehmet.berktas@gmail.com", "Mehmet Berktas"));
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress("yekmerdogan@gmail.com", "hop hop"));
                msg.setSubject("KPI Data - " + month + " " + year + " ADMIN SENT");
                Multipart mp = new MimeMultipart();
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(msgBody, "text/html");
                mp.addBodyPart(htmlPart);
                msg.setContent(mp);
                Transport.send(msg);
            } catch (AddressException e) {
            } catch (MessagingException e) {
            }
        }
    }
