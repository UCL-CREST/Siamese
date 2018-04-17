    public static void main(String[] args) {
        ReceiveMails rm = receive();
        Runtime.getRuntime().addShutdownHook(new PerformanceAnalysis.WriterHook(new File("C:/test.csv")));
        try {
            ServerSocket listener = ServerSocketFactory.getDefault().createServerSocket(2354);
            Socket socket = listener.accept();
            Scanner scan = new Scanner(socket.getInputStream());
            PrintWriter printer = new PrintWriter(socket.getOutputStream());
            printer.println("Please press \"w\" to send a report!");
            printer.println("Please press \"q\" to finish the test (wait some time please)!");
            printer.flush();
            char input;
            ScannerLoop: do {
                input = scan.next().trim().charAt(0);
                switch(input) {
                    case 'w':
                        printer.println("Preparing report for sending ...");
                        printer.flush();
                        MimeMessage msg = new MimeMessage(PerformanceAnalysis.session);
                        MimeMultipart mmp = new MimeMultipart();
                        MimeBodyPart mbp = new MimeBodyPart();
                        msg.setFrom(new InternetAddress("testresult@performanceanalysis.de"));
                        long time = System.currentTimeMillis();
                        msg.setSubject("Test from " + time);
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("simon.jarke@i-u.de"));
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("dennis.baumgart@i-u.de"));
                        CharArrayWriter writer = new CharArrayWriter(20 * 1024);
                        try {
                            PerformanceAnalysis.results.toCSVWriter(writer);
                            mbp.setFileName("csv-report-" + time + ".csv");
                            mbp.setContent(writer.toString(), "text/plain");
                            mmp.addBodyPart(mbp);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        msg.setContent(mmp);
                        Transport.send(msg);
                        printer.println("Report send out...");
                        printer.flush();
                        break;
                    case 'q':
                        break ScannerLoop;
                    default:
                        printer.println("... wrong input! Please press again");
                        printer.flush();
                }
            } while (true);
            System.out.println("Quitting ...");
            rm.setRunning(false);
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }
