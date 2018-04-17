    public void zipPackege(final String dirName, final String urldoPliku, String ikony) throws IOException, FileNotFoundException {
        final String nazwaPliku = urldoPliku;
        int x;
        final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(nazwaPliku)));
        zos.setMethod(ZipOutputStream.STORED);
        String filePath = dirName + "/" + "Paczka.kml";
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
        ZipEntry entry = new ZipEntry("Paczka.kml");
        entry.setMethod(ZipEntry.STORED);
        entry.setSize(new File(filePath).length());
        entry.setCrc(doChecksum(filePath));
        zos.putNextEntry(entry);
        while ((x = in.read()) != -1) {
            zos.write(x);
        }
        in.close();
        for (String s : conditionsNames) {
            filePath = ikony + "/" + s + ".png";
            in = new BufferedInputStream(new FileInputStream(filePath));
            entry = new ZipEntry("icons/" + s + ".png");
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(new File(filePath).length());
            entry.setCrc(doChecksum(filePath));
            zos.putNextEntry(entry);
            while ((x = in.read()) != -1) {
                zos.write(x);
            }
            in.close();
        }
        for (int i = start; i < end; i++) {
            for (String s : layers) {
                filePath = dirName + "/" + s + "_" + i + ".png";
                in = new BufferedInputStream(new FileInputStream(filePath));
                entry = new ZipEntry(s + "_" + i + ".png");
                entry.setMethod(ZipEntry.STORED);
                entry.setSize(new File(filePath).length());
                entry.setCrc(doChecksum(filePath));
                zos.putNextEntry(entry);
                while ((x = in.read()) != -1) {
                    zos.write(x);
                }
                in.close();
            }
        }
        for (int i = start; i < end; i++) {
            filePath = dirName + "/legend_" + i + ".png";
            in = new BufferedInputStream(new FileInputStream(filePath));
            entry = new ZipEntry("legend_" + i + ".png");
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(new File(filePath).length());
            entry.setCrc(doChecksum(filePath));
            zos.putNextEntry(entry);
            while ((x = in.read()) != -1) {
                zos.write(x);
            }
            in.close();
        }
        filePath = ikony + "/1.mp3";
        in = new BufferedInputStream(new FileInputStream(filePath));
        entry = new ZipEntry("1.mp3");
        entry.setMethod(ZipEntry.STORED);
        entry.setSize(new File(filePath).length());
        entry.setCrc(doChecksum(filePath));
        zos.putNextEntry(entry);
        while ((x = in.read()) != -1) {
            zos.write(x);
        }
        in.close();
        zos.close();
    }
