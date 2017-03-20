package elasticsearch.main;

public class Experiment {

	private static String[] normModes = {"x", "s", "w", "ws", "p", "ps", "pw", "pws", "k", "ks", "kw",
			"kws", "kp", "kps", "kpw", "kpws", "j", "js", "jw", "jws", "jp",
			"jps", "jpw", "jpws", "jk", "jks", "jkw", "jkws", "jkp", "jkps", "jkpw",
			"jkpws", "d", "ds", "dw", "dws", "dp", "dps", "dpw", "dpws", "dk",
			"dks", "dkw", "dkws", "dkp", "dkps", "dkpw", "dkpws", "dj", "djs", "djw",
			"djws", "djp", "djps", "djpw", "djpws", "djk", "djks", "djkw", "djkws", "djkp",
			"djkps", "djkpw", "djkpws"};
	private static int[] ngramSizes = { 1, 2, 3, 4, 5 };
	public static String prefixToRemove = "";
    public static boolean isPrint = false;
    public static int[] textNgramSize = { 1, 2, 3, 4, 5 };

	// collect the best evaluation
	private static double maxART = 0.0;
	private static String setting = "";
	
	public static void main(String[] args) {

		if (args.length < 3) {
            // If missing some arguments, show the help
			System.out.println("Usage: java elasticsearch.main.Experiment <similarity> <input folder> <working dir>");
			System.exit(-1);

		} else {

            String outputFile = "";
			String mode = args[0];
			String inputDir = args[1];
			String workingDir = args[2];

            prefixToRemove = inputDir;
            if (!prefixToRemove.endsWith("/"))
                prefixToRemove += "/"; // append / at the end

			if (mode.equals("tfidf_text")) /* text mode (1-gram + no normalisation) */
				outputFile = tfidfTextExp(inputDir, workingDir, isPrint);
            else if (mode.equals("bm25_text"))
                outputFile = bm25TextExp(inputDir, workingDir, isPrint);
            else if (mode.equals("dfr_text"))
                outputFile = dfrTextExp(inputDir, workingDir, isPrint);
            else if (mode.equals("ib_text"))
                outputFile = ibTextExp(inputDir, workingDir, isPrint);
            else if (mode.equals("lmd_text"))
                outputFile = lmdTextExp(inputDir, workingDir, isPrint);
            else if (mode.equals("lmj_text"))
                outputFile = lmjTextExp(inputDir, workingDir, isPrint);
            else if (mode.equals("tfidf")) /* normal mode (search all parameters + grams + normalisation) */
                tfidfExp(inputDir, workingDir, isPrint);
			else if (mode.equals("bm25"))
				bm25Exp(inputDir, workingDir, isPrint);
			else if (mode.equals("dfr"))
				dfrExp(inputDir, workingDir, isPrint);
			else if (mode.equals("ib"))
				ibExp(inputDir, workingDir, isPrint);
			else if (mode.equals("lmdirichlet") || mode.equals("lmd"))
				lmdExp(inputDir, workingDir, isPrint);
			else if (mode.equals("lmjelinekmercer") || mode.equals("lmj"))
				lmjExp(inputDir, workingDir, isPrint);
			else
				System.out.println("No similarity found");
		}

		System.out.println("Best ARP = " + Experiment.setting + ", " + Experiment.maxART);
	}

	public static void evaluate(String outputFile, String mode, String workingDir) {
		Evaluator evaluator = new Evaluator("resources/clone_clusters.csv", mode, workingDir);
		evaluator.generateSearchKey();
		// evaluator.printSearchKey();
		double arp = evaluator.evaluteRPrec(outputFile, 6);
		// update the max ARP
		if (maxART < arp) {
			maxART = arp;
			setting = outputFile;
		}
	}
	
	public static String tfidfTextExp(String inputDir, String workingDir, boolean isPrint) {
        String discO = "true";
		String[] normModes = Experiment.normModes;
		// String[] normModes = { "x" };
		IndexChecker checker = new IndexChecker();
		String indexSettings = "";
        if (!discO.equals("false"))
            indexSettings = "{ \"similarity\": { \"tfidf_similarity\": { \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                    "\"analysis\": { " +
                    "\"analyzer\": { " +
                    "\"default\": { " +
                    "\"type\": \"whitespace\"" +
                    "} } } }";
        else {
            indexSettings = "{ \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } }";
        }

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";
		String outFile = checker.runExperiment("localhost", "tfidf", "doc",
				inputDir, normModes, ngramSizes, true, true, workingDir,
				true, indexSettings, mappingStr, isPrint);

		return outFile;
	}

    public static void tfidfExp(String inputDir, String workingDir, boolean isPrint) {
        String[] discountOverlap = { "no", "true", "false" };
        IndexChecker checker = new IndexChecker();
        for (String discO : discountOverlap) {
            String indexSettings = "";
            if (!discO.equals("no"))
                indexSettings = "{ \"similarity\": { \"tfidf_similarity\": { \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                        "\"analysis\": { " +
                        "\"analyzer\": { " +
                        "\"default\": { " +
                        "\"type\": \"whitespace\"" +
                        "} } } }";
            else {
                indexSettings = "{ \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } }";
            }

            String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";
            // System.out.println(indexSettings);
            checker.runExperiment("localhost", "tfidf_" + discO, "doc",
                    inputDir, normModes, ngramSizes, true, true,
                    workingDir, false, indexSettings, mappingStr, isPrint);
        }
    }

    public static String bm25TextExp(String inputDir, String workingDir, boolean isPrint) {
        String[] normModes = { "x" };
        double k1 = 1.2;
        double b = 0.75;
        String discO = "true";
        IndexChecker checker = new IndexChecker();
        String indexSettings = "{ \"similarity\": "
                + "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \""+discO+"\" } }, "
                + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
        return checker.runExperiment("localhost", "bm25",
				"doc", inputDir, normModes, ngramSizes, true,
				true, workingDir, true, indexSettings, mappingStr, isPrint);
    }
	
	public static void bm25Exp(String inputDir, String workingDir, boolean isPrint) {
		String[] k1s = { "0.0", "0.6", "1.2", "1.8", "2.4" };
		String[] bs = { "0.0", "0.25", "0.50", "0.75", "1.00" };
		String[] discountOverlaps = { "true", "false" };
		
		IndexChecker checker = new IndexChecker();
		
		// for (String fold : folds) {
			for (String k1 : k1s) {
				for (String b : bs) {
					for (String discO : discountOverlaps) {
						String indexSettings = "{ \"similarity\": "
								+ "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \""+k1+"\", \"b\": \""+b+"\", \"discount_overlap\": \""+discO+"\" } }, "
										+ "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
						String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
						System.out.println(indexSettings);
						checker.runExperiment("localhost", 
								"bm25_" + k1 + "_" + b + "_" + discO, "doc",
								inputDir, normModes, ngramSizes, true, true,
								workingDir, false, indexSettings, mappingStr, isPrint);
					}
				}
			}
		// }
	}

    public static String dfrTextExp(String inputDir, String workingDir, boolean isPrint) {
        String bm = "be";
        String ae = "b";
        String norm = "h1";
        String[] normModes = {"x"};

        IndexChecker checker = new IndexChecker();

        String indexSettings = "{ \"similarity\": { \"dfr_similarity\" : { \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
                + "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
                + "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";
        // System.out.println(indexSettings);
        return checker.runExperiment("localhost", "dfr_" + bm + "_" + ae + "_" + norm,
				"doc", inputDir, normModes, ngramSizes, true, true,
                workingDir, false, indexSettings, mappingStr, isPrint);

    }

	public static void dfrExp(String inputDir, String workingDir, boolean isPrint) {
		String[] basicModelArr = { "be", "d", "g", "if", "in", "ine", "p" };
		String[] afterEffectArr = { "no", "b", "l" };
		String[] dfrNormalizationArr = { "no", "h1", "h2", "h3", "z" };
		
		IndexChecker checker = new IndexChecker();
		
		for (String bm : basicModelArr)
			for (String ae : afterEffectArr)
				for (String norm : dfrNormalizationArr) {
					String indexSettings = "{ \"similarity\": { \"dfr_similarity\" : { \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
										+ "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
							+ "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";

					String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";
					// System.out.println(indexSettings);
					checker.runExperiment("localhost", "dfr_" + bm + "_" + ae + "_" + norm, "doc",
							inputDir, normModes, ngramSizes, true, true,
							workingDir, false, indexSettings, mappingStr, isPrint);
				}
	}

    public static String ibTextExp(String inputDir, String workingDir, boolean isPrint) {
        String dist = "ll";
        String lamb = "df";
        String ibNorm = "h1";
        String[] normModes = { "x" };

        IndexChecker checker = new IndexChecker();

        String indexSettings = "{ \"similarity\": "
                + "{ \"ib_similarity\" : "
                + "{ \"type\": \"IB\", "
                + "\"distribution\": \"" + dist + "\", "
                + "\"lambda\": \"" + lamb + "\", "
                + "\"normalization\": \"" + ibNorm + "\""
                + "} "
                + "},  "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";
        // System.out.println(indexSettings);
        return checker.runExperiment("localhost",
                "ib_" + dist + "_" + lamb + "_" + ibNorm, "doc",
                inputDir, normModes, ngramSizes, true, true,
                workingDir, false, indexSettings, mappingStr, isPrint);

    }
	
	public static void ibExp(String inputDir, String workingDir, boolean isPrint) {
		String[] distributions = { "ll", "spl" };
		String[] lambdas = { "df", "ttf" };
		String[] ibNormalizationArr = { "no", "h1", "h2", "h3", "z" };
		
		IndexChecker checker = new IndexChecker();
		
		for (String dist : distributions) {
			for (String lamb : lambdas) {
				for (String ibNorm : ibNormalizationArr) {
					String indexSettings = "{ \"similarity\": "
							+ "{ \"ib_similarity\" : "
								+ "{ \"type\": \"IB\", "
								+ "\"distribution\": \"" + dist + "\", "
								+ "\"lambda\": \"" + lamb + "\", "
								+ "\"normalization\": \"" + ibNorm + "\""
								+ "} "
							+ "},  "
							+ "\"analysis\" : { \"analyzer\" : "
							+ "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
					String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";
					// System.out.println(indexSettings);
					checker.runExperiment("localhost",
							"ib_" + dist + "_" + lamb + "_" + ibNorm, "doc",
							inputDir, normModes, ngramSizes, true, true,
							workingDir, false, indexSettings, mappingStr, isPrint);
				}
			}
		}
	}

    private static String lmdTextExp(String inputDir, String workingDir, boolean isPrint) {
        String mu = "2000";
        String[] normModes = { "x" };
        int[] ngramSizes = { 1 };

        IndexChecker checker = new IndexChecker();
        String indexSettings = "{ \"similarity\": "
                + "{ \"lmd_similarity\" : "
                + "{ \"type\": \"LMDirichlet\", "
                + "\"mu\": \"" + mu + "\""
                + "} "
                + "}, "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
        return checker.runExperiment("localhost",
                "lmd_" + mu, "doc",
                inputDir, normModes, ngramSizes, true, true,
                workingDir, false, indexSettings, mappingStr, isPrint);
    }

	private static void lmdExp(String inputDir, String workingDir, boolean isPrint) {
		String[] mus = { "500", "750", "1000", "1250", "1500", 
				"1750", "2000", "2250", "2500", "2750", "3000" };
		IndexChecker checker = new IndexChecker();
		for (String mu : mus) {
			String indexSettings = "{ \"similarity\": "
					+ "{ \"lmd_similarity\" : "
						+ "{ \"type\": \"LMDirichlet\", "
						+ "\"mu\": \"" + mu + "\""
						+ "} " 
					+ "}, "
					+ "\"analysis\" : { \"analyzer\" : "
					+ "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
			// System.out.println(indexSettings);

			String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
			checker.runExperiment("localhost", 
					"lmd_" + mu, "doc",
					inputDir, normModes, ngramSizes, true, true,
					workingDir, false, indexSettings, mappingStr, isPrint);
		}
	}

    private static String lmjTextExp(String inputDir, String workingDir, boolean isPrint) {
        String[] normModes = { "x" };
        String lambda = "0.1";
        IndexChecker checker = new IndexChecker();
        String indexSettings = "{ \"similarity\": "
                + "{ \"lmj_similarity\" : "
                + "{ \"type\": \"LMJelinekMercer\", "
                + "\"lambda\": \"" + lambda + "\""
                + "} "
                + "}, "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";
        // System.out.println(indexSettings);
        return checker.runExperiment("localhost",
                "lmj_" + lambda, "doc",
                inputDir, normModes, ngramSizes, true, true,
                workingDir, false, indexSettings, mappingStr, isPrint);
    }

	private static void lmjExp(String inputDir, String workingDir, boolean isPrint) {
		String[] normModes = { "pw" };
		int[] ngramSizes = { 4 };
		String[] lambdas = { "0.9" };
		IndexChecker checker = new IndexChecker();
		for (String lambda : lambdas) {
			String indexSettings = "{ \"similarity\": "
					+ "{ \"lmj_similarity\" : "
						+ "{ \"type\": \"LMJelinekMercer\", "
						+ "\"lambda\": \"" + lambda + "\""
						+ "} "
					+ "}, "
					+ "\"analysis\" : { \"analyzer\" : "
					+ "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

			String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";
			// System.out.println(indexSettings);
			checker.runExperiment("localhost", 
					"lmj_" + lambda, "doc",
					inputDir, normModes, ngramSizes, true, true,
					workingDir, false, indexSettings, mappingStr, isPrint);
		}
	}

	/* New similarity (does not work in the current Elasticsearch version */
	/*
    public static String dfiTextExp(String inputDir, String workingDir, boolean isPrint) {
        String ind = "standardized";
        String[] normModes = { "x" };
        int[] ngramSizes = { 1 };

        IndexChecker checker = new IndexChecker();

        String indexSettings = "{ \"similarity\": "
                + "{ \"dfi_similarity\" : "
                + "{ \"type\": \"DFI\", "
                + "\"distribution\": \"" + ind + "\""
                + "} "
                + "},  "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfi_similarity\" } } } } }";
        // System.out.println(indexSettings);
        return checker.runExperiment("localhost",
                "dfi_" + ind, "doc",
                inputDir, normModes, ngramSizes, true, true,
                workingDir, false, indexSettings, mappingStr, isPrint);

    }
    */
}
