    public static void main(String[] args) throws IOException, InterruptedException {
        Date now = new Date();
        long cutoff = now.getTime() - 1000 * 60 * 5;
        cutoff = cutoff;
        System.out.println("\nNow is " + format.format(now));
        for (int i = 0; i < args.length; i++) {
            System.out.println("\n");
            File file = new File(args[i]);
            boolean exists = file.exists();
            System.out.println("file = " + file + ", exists = " + exists);
            System.out.println("getPath = " + file.getPath());
            System.out.println("getAbsolutePath = " + file.getAbsolutePath());
            if (file.getParentFile() != null) {
                System.out.println("parent.getPath = " + file.getParentFile().getCanonicalFile().getPath());
                System.out.println("parent.getAbsolutePath = " + file.getParentFile().getCanonicalFile().getAbsolutePath());
            }
            FileStatus stat = new FileStatus(args[i]);
            System.out.println("For " + args[i] + " status is " + stat + ", user = '" + stat.getUser() + "', group = '" + stat.getGroup() + "'");
            System.out.println("last modify = " + date(file.lastModified()));
            char a = (stat.atime > cutoff) ? '*' : ' ';
            char m = (stat.mtime > cutoff) ? '*' : ' ';
            char c = (stat.ctime > cutoff) ? '*' : ' ';
            System.out.println(a + "atime = " + date(stat.atime) + ", " + m + "mtime = " + date(stat.mtime) + ", " + c + "ctime = " + date(stat.ctime) + ", mode = " + Integer.toOctalString(stat.permission));
            Process p = Runtime.getRuntime().exec("samp1 " + file);
            InputStream out = p.getInputStream();
            int cc;
            while ((cc = out.read()) >= 0) {
                System.out.write(cc);
            }
        }
        System.out.println("Done with Sample1.");
    }
