    static void nullCheck() throws java.lang.Exception {
        int loop = 0;
        SMPPPacket[] obj = new SMPPPacket[cs.length];
        System.out.println("\n\n========= Simple null field check ==========");
        try {
            Constructor con;
            Class[] argTypes = { int.class };
            Object[] args = { new Integer(4) };
            for (loop = 0; loop < cs.length; loop++) {
                con = cs[loop].getConstructor(argTypes);
                obj[loop] = (SMPPPacket) con.newInstance(args);
            }
        } catch (Exception x) {
            System.out.print(cs[loop].getName() + ":\n    ");
            x.printStackTrace();
        }
        runTestOnArray(obj);
        System.out.println("\n\n============================================");
    }
