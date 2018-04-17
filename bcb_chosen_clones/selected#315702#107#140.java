    public static void main(String[] argv) throws IOException {
        int i;
        for (i = 0; i < argv.length; i++) {
            if (argv[i].charAt(0) != '-') break;
            ++i;
            switch(argv[i - 1].charAt(1)) {
                case 'b':
                    try {
                        flag_predict_probability = (atoi(argv[i]) != 0);
                    } catch (NumberFormatException e) {
                        exit_with_help();
                    }
                    break;
                default:
                    System.err.printf("unknown option: -%d%n", argv[i - 1].charAt(1));
                    exit_with_help();
                    break;
            }
        }
        if (i >= argv.length || argv.length <= i + 2) {
            exit_with_help();
        }
        BufferedReader reader = null;
        Writer writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(argv[i]), Linear.FILE_CHARSET));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(argv[i + 2]), Linear.FILE_CHARSET));
            Model model = Linear.loadModel(new File(argv[i + 1]));
            doPredict(reader, writer, model);
        } finally {
            closeQuietly(reader);
            closeQuietly(writer);
        }
    }
