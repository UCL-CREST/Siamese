    void write() throws IOException {
        if (!allowUnlimitedArgs && args != null && args.length > 1) throw new IllegalArgumentException("Only one argument allowed unless allowUnlimitedArgs is enabled");
        String shebang = "#!" + interpretter;
        for (int i = 0; i < args.length; i++) {
            shebang += " " + args[i];
        }
        shebang += '\n';
        IOUtils.copy(new StringReader(shebang), outputStream);
    }
