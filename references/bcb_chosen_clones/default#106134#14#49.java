    @Override
    public void run() {
        File file = new File(LogHandler.path);
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File file) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
                String oldTime = LogHandler.dateFormat.format(cal.getTime());
                return file.getName().toLowerCase().startsWith(oldTime);
            }
        };
        File[] list = file.listFiles(filter);
        if (list.length > 0) {
            FileInputStream in;
            int read = 0;
            byte[] data = new byte[1024];
            for (int i = 0; i < list.length; i++) {
                try {
                    in = new FileInputStream(LogHandler.path + list[i].getName());
                    GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(LogHandler.path + list[i].getName() + ".temp"));
                    while ((read = in.read(data, 0, 1024)) != -1) out.write(data, 0, read);
                    in.close();
                    out.close();
                    new File(LogHandler.path + list[i].getName() + ".temp").renameTo(new File(LogHandler.path + list[i].getName() + ".gz"));
                    list[i].delete();
                } catch (FileNotFoundException e) {
                    TrackingServer.incExceptionCounter();
                    e.printStackTrace();
                } catch (IOException ioe) {
                }
            }
        }
    }
