    public static final void main(String[] argv) {
        String machineName = null;
        String envName = null;
        for (int i = 0; i < argv.length; i++) {
            machineName = argv[i].substring(0, argv[i].lastIndexOf("."));
            envName = machineName + "Environment";
            if (new File(envName + ".java").canRead()) {
                System.out.println("*** Running " + envName);
                try {
                    Class.forName("examples." + envName).getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { null });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("*** Completed");
            } else {
                System.out.println("*** Running " + machineName);
                try {
                    ((TransitionMachine) MachineConstructor.newInstance("examples." + machineName, null)).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("*** Completed");
            }
        }
    }
