    public static void main(String[] args) {
        String WTKdir = null;
        String sourceFile = null;
        String instrFile = null;
        String outFile = null;
        String jadFile = null;
        Manifest mnf;
        if (args.length == 0) {
            usage();
            return;
        }
        int i = 0;
        while (i < args.length && args[i].startsWith("-")) {
            if (("-WTK".equals(args[i])) && (i < args.length - 1)) {
                i++;
                WTKdir = args[i];
            } else if (("-source".equals(args[i])) && (i < args.length - 1)) {
                i++;
                sourceFile = args[i];
            } else if (("-instr".equals(args[i])) && (i < args.length - 1)) {
                i++;
                instrFile = args[i];
            } else if (("-o".equals(args[i])) && (i < args.length - 1)) {
                i++;
                outFile = args[i];
            } else if (("-jad".equals(args[i])) && (i < args.length - 1)) {
                i++;
                jadFile = args[i];
            } else {
                System.out.println("Error: Unrecognized option: " + args[i]);
                System.exit(0);
            }
            i++;
        }
        if (WTKdir == null || sourceFile == null || instrFile == null) {
            System.out.println("Error: Missing parameter!!!");
            usage();
            return;
        }
        if (outFile == null) outFile = sourceFile;
        FileInputStream fisJar;
        try {
            fisJar = new FileInputStream(sourceFile);
        } catch (FileNotFoundException e1) {
            System.out.println("Cannot find source jar file: " + sourceFile);
            e1.printStackTrace();
            return;
        }
        FileOutputStream fosJar;
        File aux = null;
        try {
            aux = File.createTempFile("predef", "aux");
            fosJar = new FileOutputStream(aux);
        } catch (IOException e1) {
            System.out.println("Cannot find temporary jar file: " + aux);
            e1.printStackTrace();
            return;
        }
        JarFile instrJar = null;
        Enumeration en = null;
        File tempDir = null;
        try {
            instrJar = new JarFile(instrFile);
            en = instrJar.entries();
            tempDir = File.createTempFile("jbtp", "");
            tempDir.delete();
            System.out.println("Create directory: " + tempDir.mkdirs());
            tempDir.deleteOnExit();
        } catch (IOException e) {
            System.out.println("Cannot open instrumented file: " + instrFile);
            e.printStackTrace();
            return;
        }
        String[] wtklib = new java.io.File(WTKdir + File.separator + "lib").list(new OnlyJar());
        String preverifyCmd = WTKdir + File.separator + "bin" + File.separator + "preverify -classpath " + WTKdir + File.separator + "lib" + File.separator + CLDC_JAR + File.pathSeparator + WTKdir + File.separator + "lib" + File.separator + MIDP_JAR + File.pathSeparator + WTKdir + File.separator + "lib" + File.separator + WMA_JAR + File.pathSeparator + instrFile;
        for (int k = 0; k < wtklib.length; k++) {
            preverifyCmd += File.pathSeparator + WTKdir + File.separator + "lib" + wtklib[k];
        }
        preverifyCmd += " " + "-d " + tempDir.getAbsolutePath() + " ";
        while (en.hasMoreElements()) {
            JarEntry je = (JarEntry) en.nextElement();
            String jeName = je.getName();
            if (jeName.endsWith(".class")) jeName = jeName.substring(0, jeName.length() - 6);
            preverifyCmd += jeName + " ";
        }
        try {
            Process p = Runtime.getRuntime().exec(preverifyCmd);
            if (p.waitFor() != 0) {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                System.out.println("Error calling the preverify command.");
                while (in.ready()) {
                    System.out.print("" + in.readLine());
                }
                System.out.println();
                in.close();
                return;
            }
        } catch (Exception e) {
            System.out.println("Cannot execute preverify command");
            e.printStackTrace();
            return;
        }
        File[] listOfFiles = computeFiles(tempDir);
        System.out.println("-------------------------------\n" + "Files to insert: ");
        String[] strFiles = new String[listOfFiles.length];
        int l = tempDir.toString().length() + 1;
        for (int j = 0; j < listOfFiles.length; j++) {
            strFiles[j] = listOfFiles[j].toString().substring(l);
            strFiles[j] = strFiles[j].replace(File.separatorChar, '/');
            System.out.println(strFiles[j]);
        }
        System.out.println("-------------------------------");
        try {
            JarInputStream jis = new JarInputStream(fisJar);
            mnf = jis.getManifest();
            JarOutputStream jos = new JarOutputStream(fosJar, mnf);
            nextJar: for (JarEntry je = jis.getNextJarEntry(); je != null; je = jis.getNextJarEntry()) {
                String s = je.getName();
                for (int k = 0; k < strFiles.length; k++) {
                    if (strFiles[k].equals(s)) continue nextJar;
                }
                jos.putNextEntry(je);
                byte[] b = new byte[512];
                for (int k = jis.read(b, 0, 512); k >= 0; k = jis.read(b, 0, 512)) {
                    jos.write(b, 0, k);
                }
            }
            jis.close();
            for (int j = 0; j < strFiles.length; j++) {
                FileInputStream fis = new FileInputStream(listOfFiles[j]);
                JarEntry je = new JarEntry(strFiles[j]);
                jos.putNextEntry(je);
                byte[] b = new byte[512];
                while (fis.available() > 0) {
                    int k = fis.read(b, 0, 512);
                    jos.write(b, 0, k);
                }
                fis.close();
            }
            jos.close();
            fisJar.close();
            fosJar.close();
        } catch (IOException e) {
            System.out.println("Cannot read/write jar file.");
            e.printStackTrace();
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            FileInputStream fis = new FileInputStream(aux);
            byte[] b = new byte[512];
            while (fis.available() > 0) {
                int k = fis.read(b, 0, 512);
                fos.write(b, 0, k);
            }
            fis.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Cannot write output jar file: " + outFile);
            e.printStackTrace();
        }
        Iterator it;
        Attributes atr;
        atr = mnf.getMainAttributes();
        it = atr.keySet().iterator();
        if (jadFile != null) {
            FileOutputStream fos;
            try {
                File outJarFile = new File(outFile);
                fos = new FileOutputStream(jadFile);
                PrintStream psjad = new PrintStream(fos);
                while (it.hasNext()) {
                    Object ats = it.next();
                    psjad.println(ats + ": " + atr.get(ats));
                }
                psjad.println("MIDlet-Jar-URL: " + outFile);
                psjad.println("MIDlet-Jar-Size: " + outJarFile.length());
                fos.close();
            } catch (IOException eio) {
                System.out.println("Cannot create jad file.");
                eio.printStackTrace();
            }
        }
    }
