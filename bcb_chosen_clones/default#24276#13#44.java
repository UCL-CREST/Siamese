    public static void send(TestType testType) {
        Session session = PerformanceAnalysis.session;
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(PerformanceAnalysis.emailAddress));
            msg.setContent(testType.getData(), "text/plain");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        String id = String.format("%03d", (testno++)) + Util.md5String(System.currentTimeMillis());
        System.out.println("Starting test #" + id);
        for (int i = 0; i < testType.getAmount(); i++) {
            try {
                String addr = testType.name() + "." + id + "@performanceanalysis.de";
                msg.setFrom(new InternetAddress(addr));
                String subj = "perform number " + i + " time " + System.currentTimeMillis();
                msg.setSubject(subj);
                long time = System.currentTimeMillis();
                Transport.send(msg);
                System.out.print(System.currentTimeMillis() - time + ", ");
                Thread.sleep(testType.getInterval());
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Sending finished!");
    }
