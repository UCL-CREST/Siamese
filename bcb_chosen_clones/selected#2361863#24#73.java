    public static void main(String[] args) {
        for (int i = 0; i < args.length - 2; i++) {
            if (!CommonArguments.parseArguments(args, i, u)) {
                u.usage();
                System.exit(1);
            }
            if (CommonParameters.startArg > (i + 1)) i = CommonParameters.startArg - 1;
        }
        if (args.length < CommonParameters.startArg + 2) {
            u.usage();
            System.exit(1);
        }
        try {
            int readsize = 1024;
            ContentName argName = ContentName.fromURI(args[CommonParameters.startArg]);
            CCNHandle handle = CCNHandle.open();
            File theFile = new File(args[CommonParameters.startArg + 1]);
            if (theFile.exists()) {
                System.out.println("Overwriting file: " + args[CommonParameters.startArg + 1]);
            }
            FileOutputStream output = new FileOutputStream(theFile);
            long starttime = System.currentTimeMillis();
            CCNInputStream input;
            if (CommonParameters.unversioned) input = new CCNInputStream(argName, handle); else input = new CCNFileInputStream(argName, handle);
            if (CommonParameters.timeout != null) {
                input.setTimeout(CommonParameters.timeout);
            }
            byte[] buffer = new byte[readsize];
            int readcount = 0;
            long readtotal = 0;
            while ((readcount = input.read(buffer)) != -1) {
                readtotal += readcount;
                output.write(buffer, 0, readcount);
                output.flush();
            }
            if (CommonParameters.verbose) System.out.println("ccngetfile took: " + (System.currentTimeMillis() - starttime) + "ms");
            System.out.println("Retrieved content " + args[CommonParameters.startArg + 1] + " got " + readtotal + " bytes.");
            System.exit(0);
        } catch (ConfigurationException e) {
            System.out.println("Configuration exception in ccngetfile: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedContentNameStringException e) {
            System.out.println("Malformed name: " + args[CommonParameters.startArg] + " " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Cannot write file or read content. " + e.getMessage());
            e.printStackTrace();
        }
        System.exit(1);
    }
