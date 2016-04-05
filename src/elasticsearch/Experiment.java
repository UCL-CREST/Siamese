package elasticsearch;

public class Experiment {
	private static String[] normModes = {"x", "s", "w", "ws", "p", "ps", "pw", "pws", "k", "ks", "kw",
			"kws", "kp", "kps", "kpw", "kpws", "j", "js", "jw", "jws", "jp",
			"jps", "jpw", "jpws", "jk", "jks", "jkw", "jkws", "jkp", "jkps", "jkpw",
			"jkpws", "d", "ds", "dw", "dws", "dp", "dps", "dpw", "dpws", "dk",
			"dks", "dkw", "dkws", "dkp", "dkps", "dkpw", "dkpws", "dj", "djs", "djw",
			"djws", "djp", "djps", "djpw", "djpws", "djk", "djks", "djkw", "djkws", "djkp",
			"djkps", "djkpw", "djkpws"};
	private static int[] ngramSizes = { 1, 2, 3, 4 };
	
	public static void main(String[] args) {
		// dfrExp();
		// tfidfExpOnce();
		
		if (args.length < 3) {
			System.out.println("Usage: java elasticsearch.Experiment <similarity> <input folder> <working dir>");
			System.exit(-1);
		} else {
			String mode = args[0];
			String inputDir = args[1];
			String workingDir = args[2];
			
			// bm25ExpOnce("0.0", "0.0", "true");
			// dfrExpOnce(inputDir, workingDir);
			// tfidfExpOnce();
			
			boolean isPrint = true;
			if (mode.equals("tfidf"))
				tfidfExp(inputDir, workingDir, isPrint);
			else if (mode.equals("tfidf_text"))
				tfidfTextExp(inputDir, workingDir, isPrint);
			else if (mode.equals("tfidf_norm"))
				tfidfNormExp(inputDir, workingDir, isPrint);
			else if (mode.equals("bm25")) 
				bm25Exp(inputDir, workingDir, isPrint);
			else if (mode.equals("bm25_text"))
				bm25TextExp(inputDir, workingDir, isPrint);
			else if (mode.equals("bm25_norm"))
				bm25NormExp(inputDir, workingDir, isPrint);
			else if (mode.equals("dfr"))
				dfrExp(inputDir, workingDir, isPrint);
			else if (mode.equals("ib"))
				ibExp(inputDir, workingDir, isPrint);
			else if (mode.equals("lmdirichlet") || mode.equals("lmd"))
				lmdExp(inputDir, workingDir, isPrint);
			else if (mode.equals("lmjelinekmercer") || mode.equals("lmj"))
				lmjkmExp(inputDir, workingDir, isPrint);
			else
				System.out.println("No similarity found");
		}
		
	}
	
	public static void tfidfTextExp(String inputDir, String workingDir, boolean isPrint) {
		String[] normModes = { "x" };
		int[] ngramSizes = { 4 };
		IndexChecker checker = new IndexChecker();
		String indexSettings = "";
		indexSettings = "{ \"settings\":" + "{ \"mappings\": {  \"doc\":{  \"properties\": "
				+ "{  \"src\": { \"type\": \"string\", \"similarity\": \"default\" } } } } } }";
		// System.out.println(indexSettings);
		checker.runExperiment("localhost", "tfidf", "doc", inputDir, normModes, ngramSizes, true, true, workingDir,
				true, indexSettings, "", isPrint);
	}
	
	public static void tfidfNormExp(String inputDir, String workingDir, boolean isPrint) {
		String[] normModes = { "w" };
		int[] ngramSizes = { 2 };
		IndexChecker checker = new IndexChecker();
		String indexSettings = "";
		indexSettings = "{ \"settings\":" + "{ \"mappings\": {  \"doc\":{  \"properties\": "
				+ "{  \"src\": { \"type\": \"string\", \"similarity\": \"default\" } } } } } }";
		// System.out.println(indexSettings);
		checker.runExperiment("localhost", "tfidf", "doc", inputDir, normModes, ngramSizes, true, true, workingDir,
				true, indexSettings, "", isPrint);
	}
	
	public static void bm25TextExp(String inputDir, String workingDir, boolean isPrint) {
		String[] normModes = { "x" };
		int[] ngramSizes = { 2 };
		IndexChecker checker = new IndexChecker();
		String indexSettings = ""
				+ "{ \"settings\": "
					+ "{ \"similarity\": "
						+ "{ \"bm25_similarity\" : "
							+ "{ \"type\": \"BM25\", "
							+ "\"k1\": \"1.2\", "
							+ "\"b\": \"0.75\""
							+ "} "
						+ "} "
					+ "}, "
				+ "\"mappings\": {  "
					+ "\"doc\":{  \"properties\": "
								+ "{  \"src\": "
								+ "	{  \"type\": \"string\", "
									+ "\"similarity\": \"bm25_similarity\" }  "
								+ "} "
							+ " } "
					+ " } } ";

		String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } } }";
		checker.runExperiment("localhost", "bm25", "doc", inputDir, normModes, ngramSizes, true, true, workingDir,
				true, indexSettings, mappingStr, isPrint);
	}
	
	public static void bm25NormExp(String inputDir, String workingDir, boolean isPrint) {
		String[] normModes = { "w" };
		int[] ngramSizes = { 2 };
		IndexChecker checker = new IndexChecker();
		String indexSettings = ""
				+ "{ \"settings\": "
					+ "{ \"similarity\": "
						+ "{ \"bm25_similarity\" : "
							+ "{ \"type\": \"BM25\", "
							+ "\"k1\": \"1.2\", "
							+ "\"b\": \"0.75\""
							+ "} "
						+ "} "
					+ "}, "
				+ "\"mappings\": {  "
					+ "\"doc\":{  \"properties\": "
								+ "{  \"src\": "
								+ "	{  \"type\": \"string\", "
									+ "\"similarity\": \"bm25_similarity\" }  "
								+ "} "
							+ " } "
					+ " } } ";

		String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } } }";
		checker.runExperiment("localhost", "bm25", "doc", inputDir, normModes, ngramSizes, true, true, workingDir,
				true, indexSettings, mappingStr, isPrint);
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
	
	public static void dfrExp(String inputDir, String workingDir, boolean isPrint) {
		// String[] basicModelArr = { "be", "d", "g", "if", "in", "ine", "p" };
		String[] basicModelArr = { "in", "ine", "p" };
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
	
	private static void lmjkmExp(String inputDir, String workingDir, boolean isPrint) {
		String[] lambdas = { "0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0" };
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
	
	public static void tfidfExpOnce() {
		String[] discountOverlap = { "true" };
		String[] normModes = { "kp" };
		int[] ngramSizes = { 3 };
		IndexChecker checker = new IndexChecker();
				for (String discO : discountOverlap) {
					String indexSettings = "";
					if (!discO.equals("no")) 
						indexSettings = "{"+
										"\"settings\": {" +
											"\"similarity\": {" +
												"\"tfidf_similarity\": {" +
													"\"type\": \"default\"," +
													"\"discount_overlaps\": \"" + discO + "\"" +
												"} } }, " +
										"\"mappings\": { " +
											"\"doc\": {" +
												"\"properties\": { " +
													"\"src\": { " +
														"\"type\": \"string\"," +
														"\"similarity\": \"tfidf_similarity\"" +
													"} } } }, " +
										"\"index\": { " +
											"\"analysis\": { " +
												"\"analyzer\": { " +
													"\"default\": { " +
														"\"type\": \"whitespace\"" +
													"} } } } }";
					else {
						indexSettings = "{ \"settings\":"
								+ "{ \"mappings\": {  \"doc\":{  \"properties\": "
								+ "{  \"src\": { \"type\": \"string\", \"similarity\": \"default\" } } } }"
								+ ", \"index\" : { \"analysis\" : { \"analyzer\" : "
								+ "{ \"default\" : { \"type\" : \"whitespace\" } } } } } }";
					}

					String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";
					// System.out.println(indexSettings);
					checker.runExperiment("localhost", "tfidf_" + discO, "doc",
							"/Users/Chaiyong/Documents/es_exp/methods_full", normModes, ngramSizes, true, true,
							"/Users/Chaiyong/Documents/es_exp", false, indexSettings, mappingStr, false);
				}
	}
	
	public static void bm25ExpOnce(String k1, String b, String discO) {
		IndexChecker checker = new IndexChecker();
		String[] normModes = { "kp" };
		int[] ngramSizes = { 3 };
		String indexSettings = "{ \"settings\": "
				+ "{ \"similarity\": "
				+ "{ \"bm25_similarity\" : "
					+ "{ \"type\": \"BM25\", "
					+ "\"k1\": \"" + k1 + "\", "
					+ "\"b\": \"" + b + "\", "
					+ "\"discount_overlap\": \"" + discO + "\""
					+ "} "
				+ "} "
				+ "}, "
				+ "\"mappings\": {  "
				+ "\"doc\":{  \"properties\": "
				+ "{  \"src\": {  \"type\": \"string\", \"similarity\": \"bm25_similarity\""
				+ "}  }  }  } ,  "
				+ "\"analysis\" : { \"analyzer\" : "
				+ "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

		String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } } }";
		// System.out.println(indexSettings);
		checker.runExperiment("localhost", "bm25_" + k1 + "_" + b + "_" + discO, "doc",
				"/Users/Chaiyong/Documents/es_exp/methods_full", normModes, ngramSizes, true, true,
				"/Users/Chaiyong/Documents/es_exp", false, indexSettings, mappingStr, false);
	}
	
	public static void dfrExpOnce(String inputDir, String workingDir) {
		String[] basicModelArr = { "be" };
		String[] afterEffectArr = { "no" };
		String[] dfrNormalizationArr = { "no" };
		String[] normModes = { "kp" };
		int[] ngramSizes = { 3 };
		
		IndexChecker checker = new IndexChecker();
		
		for (String bm : basicModelArr)
			for (String ae : afterEffectArr)
				for (String norm : dfrNormalizationArr) {
					String indexSettings = "{ \"settings\": "
							+ "{ \"similarity\": "
							+ "{ \"dfr_similarity\" : "
								+ "{ \"type\": \"DFR\", "
								+ "\"basic_model\": \"" + bm + "\", "
								+ "\"after_effect\": \"" + ae + "\", "
								+ "\"normalization\": \"" + norm + "\", "
								+ "\"normalization.h2.c\": \"3.0\" "
								+ "} "
							+ "} "
							+ "}, "
							+ "\"mappings\": {  "
							+ "\"doc\":{  \"properties\": "
							+ "{  \"src\": {  \"type\": \"string\", \"similarity\": \"dfr_similarity\" "
							+ "}  }  }  } ,  "
							+ "\"index\" : {  \"analysis\" : { \"analyzer\" : "
							+ "{ \"default\" : { \"type\" : \"whitespace\" } } } } }";

					String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";
					// System.out.println(indexSettings);
					checker.runExperiment("localhost", "dfr_" + bm + "_" + ae + "_" + norm, "doc",
							inputDir, normModes, ngramSizes, true, true,
							workingDir, false, indexSettings, mappingStr, false);
				}
	}
}
