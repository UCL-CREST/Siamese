    public static void main(String args[]) {
        if (args.length < 4) {
            usage();
        }
        int cacheSize = Integer.parseInt(args[0]);
        String diskName = args[1];
        int diskSize = Integer.parseInt(args[2]);
        String shellCommand = args[3];
        for (int i = 4; i < args.length; i++) {
            shellCommand += " " + args[i];
        }
        Object disk = null;
        try {
            Class diskClass = Class.forName(diskName);
            Constructor ctor = diskClass.getConstructor(new Class[] { Integer.TYPE });
            disk = ctor.newInstance(new Object[] { new Integer(diskSize) });
            if (!(disk instanceof Disk)) {
                pl(diskName + " is not a subclass of Disk");
                usage();
            }
            if (!diskName.equals("FastDisk")) {
                new Thread((Disk) disk, "DISK").start();
            }
        } catch (ClassNotFoundException e) {
            pl(diskName + ": class not found");
            usage();
        } catch (NoSuchMethodException e) {
            pl(diskName + "(int): no such constructor");
            usage();
        } catch (InvocationTargetException e) {
            pl(diskName + ": " + e.getTargetException());
            usage();
        } catch (Exception e) {
            pl(diskName + ": " + e);
            usage();
        }
        pl("Boot: Starting kernel.");
        Kernel.interrupt(Kernel.INTERRUPT_POWER_ON, cacheSize, 0, disk, shellCommand, null);
        System.out.println("Boot: Kernel has stopped.");
        System.exit(0);
    }
