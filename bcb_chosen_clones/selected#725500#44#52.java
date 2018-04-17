    public void outputAll(String namePrefix, ZipOutputStream zipOut) throws Exception {
        Assert.Arg.notNull(namePrefix, "namePrefix");
        Assert.Arg.notNull(zipOut, "zipOut");
        for (String key : this.files.keySet()) {
            ZipEntry zipEntry = new ZipEntry(namePrefix + key);
            zipOut.putNextEntry(zipEntry);
            zipOut.write(this.files.get(key));
        }
    }
