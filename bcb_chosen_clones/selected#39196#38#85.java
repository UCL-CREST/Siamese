    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("usage: " + EvaluatorHelper.class.getName() + " <output> <data set file>");
            System.exit(1);
        }
        Helper helper = Helper.getHelper(args[1]);
        Dataset dataset = helper.read(args[1]);
        ZipFile zip = new ZipFile(new File(args[0]), ZipFile.OPEN_READ);
        Enumeration entries = zip.entries();
        Unit<?>[] performance = new Unit<?>[LIMIT];
        int index = 0;
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.getName().endsWith(".out")) {
                File temp = File.createTempFile("PARSER", ".zip");
                temp.deleteOnExit();
                PrintStream writer = new PrintStream(new FileOutputStream(temp));
                BufferedInputStream reader = new BufferedInputStream(zip.getInputStream(entry));
                byte[] buffer = new byte[4096];
                int read = -1;
                while ((read = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, read);
                }
                writer.close();
                reader.close();
                BufferedReader outfile = new BufferedReader(new FileReader(temp));
                String line = null;
                RuleParser parser = new RuleParser();
                ProbabilisticRuleList list = new ProbabilisticRuleList();
                while ((line = outfile.readLine()) != null) {
                    if (line.startsWith("IF")) {
                        ProbabilisticRule rule = new ProbabilisticRule(dataset.getMetadata());
                        list.add(fill(dataset.getMetadata(), rule, parser.parse(line)));
                    }
                }
                outfile.close();
                PooledPRCurveMeasure measure = new PooledPRCurveMeasure();
                performance[index] = measure.evaluate(dataset, list);
                System.out.println(entry.getName() + ": " + performance[index]);
                index++;
                if (index >= LIMIT) {
                    break;
                }
            }
        }
        System.out.println(UnitAveragingMode.get(Double.class).average(performance));
    }
