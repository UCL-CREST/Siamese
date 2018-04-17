    public static void main(String[] args) throws Exception {
        String linesep = System.getProperty("line.separator");
        FileOutputStream fos = new FileOutputStream(new File("lib-licenses.txt"));
        fos.write(new String("JCP contains the following libraries. Please read this for comments on copyright etc." + linesep + linesep).getBytes());
        fos.write(new String("Chemistry Development Kit, master version as of " + new Date().toString() + " (http://cdk.sf.net)" + linesep).getBytes());
        fos.write(new String("Copyright 1997-2009 The CDK Development Team" + linesep).getBytes());
        fos.write(new String("License: LGPL v2 (http://www.gnu.org/licenses/old-licenses/gpl-2.0.html)" + linesep).getBytes());
        fos.write(new String("Download: https://sourceforge.net/projects/cdk/files/" + linesep).getBytes());
        fos.write(new String("Source available at: http://sourceforge.net/scm/?type=git&group_id=20024" + linesep + linesep).getBytes());
        File[] files = new File(args[0]).listFiles(new JarFileFilter());
        for (int i = 0; i < files.length; i++) {
            if (new File(files[i].getPath() + ".meta").exists()) {
                Map<String, Map<String, String>> metaprops = readProperties(new File(files[i].getPath() + ".meta"));
                Iterator<String> itsect = metaprops.keySet().iterator();
                while (itsect.hasNext()) {
                    String section = itsect.next();
                    fos.write(new String(metaprops.get(section).get("Library") + " " + metaprops.get(section).get("Version") + " (" + metaprops.get(section).get("Homepage") + ")" + linesep).getBytes());
                    fos.write(new String("Copyright " + metaprops.get(section).get("Copyright") + linesep).getBytes());
                    fos.write(new String("License: " + metaprops.get(section).get("License") + " (" + metaprops.get(section).get("LicenseURL") + ")" + linesep).getBytes());
                    fos.write(new String("Download: " + metaprops.get(section).get("Download") + linesep).getBytes());
                    fos.write(new String("Source available at: " + metaprops.get(section).get("SourceCode") + linesep + linesep).getBytes());
                }
            }
            if (new File(files[i].getPath() + ".extra").exists()) {
                fos.write(new String("The author says:" + linesep).getBytes());
                FileInputStream in = new FileInputStream(new File(files[i].getPath() + ".extra"));
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
            }
            fos.write(linesep.getBytes());
        }
        fos.close();
    }
