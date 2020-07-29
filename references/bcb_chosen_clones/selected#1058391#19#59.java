    public void test_10() {
        System.out.println("==test_10===");
        String cmnd = null;
        if (RuntimeAdditionalTest0.os.equals("Win")) {
            cmnd = RuntimeAdditionalTest0.cm + " /C cat \"" + RuntimeAdditionalTest0.libFile + "\"";
        } else if (RuntimeAdditionalTest0.os.equals("Lin")) {
            cmnd = "cat -v \"" + RuntimeAdditionalTest0.libFile + "\"";
        } else {
            fail("WARNING (test_1): unknown operating system.");
        }
        try {
            Process pi3 = Runtime.getRuntime().exec(cmnd);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            pi3.getOutputStream();
            pi3.getErrorStream();
            java.io.InputStream is = pi3.getInputStream();
            int ia;
            while (true) {
                while ((ia = is.available()) != 0) {
                    byte[] bbb = new byte[ia];
                    is.read(bbb);
                }
                try {
                    pi3.exitValue();
                    while ((ia = is.available()) != 0) {
                        byte[] bbb = new byte[ia];
                        is.read(bbb);
                    }
                    break;
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
        } catch (Exception eeee) {
            eeee.printStackTrace();
            fail("ERROR (test_10): unexpected exception.");
        }
    }
