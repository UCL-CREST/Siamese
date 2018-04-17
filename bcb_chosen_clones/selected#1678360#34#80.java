    public static void main(String[] args) throws ParseException, FileNotFoundException, IOException {
        InputStream input = new BufferedInputStream(UpdateLanguages.class.getResourceAsStream("definition_template"));
        Translator t = new Translator(input, "UTF8");
        Node template = Translator.Start();
        File langs = new File("support/support/translate/languages");
        for (File f : langs.listFiles()) {
            if (f.getName().endsWith(".lng")) {
                input = new BufferedInputStream(new FileInputStream(f));
                try {
                    Translator.ReInit(input, "UTF8");
                } catch (java.lang.NullPointerException e) {
                    new Translator(input, "UTF8");
                }
                Node newFile = Translator.Start();
                ArrayList<Addition> additions = new ArrayList<Addition>();
                syncKeys(template, newFile, additions);
                ArrayList<String> fileLines = new ArrayList<String>();
                Scanner scanner = new Scanner(new BufferedReader(new FileReader(f)));
                while (scanner.hasNextLine()) {
                    fileLines.add(scanner.nextLine());
                }
                int offset = 0;
                for (Addition a : additions) {
                    System.out.println("Key added " + a + " to " + f.getName());
                    if (a.afterLine < 0 || a.afterLine >= fileLines.size()) {
                        fileLines.add(a.getAddition(0));
                    } else {
                        fileLines.add(a.afterLine + (offset++) + 1, a.getAddition(0));
                    }
                }
                f.delete();
                Writer writer = new BufferedWriter(new FileWriter(f));
                for (String s : fileLines) writer.write(s + "\n");
                writer.close();
                System.out.println("Language " + f.getName() + " had " + additions.size() + " additions");
            }
        }
        File defFile = new File(langs, "language.lng");
        defFile.delete();
        defFile.createNewFile();
        InputStream copyStream = new BufferedInputStream(UpdateLanguages.class.getResourceAsStream("definition_template"));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(defFile));
        int c = 0;
        while ((c = copyStream.read()) >= 0) out.write(c);
        out.close();
        System.out.println("Languages updated.");
    }
