    public boolean archiveLogs(LogTag[] tags) {
        String logBaseDir = GlobalProps.getHomeDir() + File.separator + LOG_DIR;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int i = 0; i < tags.length; i++) {
            long time = tags[i].logTime;
            if (time < min) {
                min = time;
            }
            if (time > max) {
                max = time;
            }
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(min);
        int minMonth = cal.get(Calendar.MONTH);
        int minYear = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(max);
        int maxMonth = cal.get(Calendar.MONTH);
        int maxYear = cal.get(Calendar.YEAR);
        String nameStr = null;
        if (minYear == maxYear && minMonth == maxMonth) {
            nameStr = TimeUtil.monthNameStr(min);
        } else {
            nameStr = TimeUtil.monthNameStr(min) + "-" + TimeUtil.monthNameStr(max);
        }
        File archiveDir = new File(logBaseDir + File.separator + "archive");
        if (!archiveDir.isDirectory()) {
            archiveDir.mkdirs();
        }
        String archPath = getUniqueLogName(logBaseDir + File.separator + "archive", nameStr, ".zip");
        File archiveFile = new File(archPath);
        byte[] buffer = new byte[2048];
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archiveFile));
            zout.setLevel(Deflater.DEFAULT_COMPRESSION);
            for (int i = 0; i < tags.length; i++) {
                String logPath = logBaseDir + File.separator + tags[i].logType + File.separator + tags[i].logName;
                FileInputStream in = new FileInputStream(new File(logPath));
                ZipEntry entry = new ZipEntry(tags[i].logType.charAt(0) + tags[i].logName);
                entry.setComment(tags[i].logType);
                zout.putNextEntry(entry);
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    zout.write(buffer, 0, len);
                }
                zout.closeEntry();
                in.close();
            }
            zout.close();
            return true;
        } catch (IOException ioe) {
            if (GlobalProps.DEBUG) {
                System.out.println("Error archiving logs");
                ioe.printStackTrace();
            }
            return false;
        }
    }
