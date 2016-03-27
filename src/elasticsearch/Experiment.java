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
		tfidfExp();
	}
	
	public static void tfidfExp() {
		String[] discountOverlap = { "no", "true", "false" };
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
					// System.out.println(indexSettings);
					checker.runExperiment("localhost", "tfidf_" + discO, "doc",
							"/Users/Chaiyong/Documents/es_exp/methods", normModes, ngramSizes, true, true,
							"/Users/Chaiyong/Documents/es_exp", false, indexSettings);
				}
	}
	
	public static void dfrExp() {
		String[] basicModelArr = { "be", "d", "g", "if", "in", "ine", "p" };
		String[] afterEffectArr = { "no", "b", "l" };
		String[] dfrNormalizationArr = { "no", "h1", "h2", "h3", "z" };
		
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
					// System.out.println(indexSettings);
					checker.runExperiment("localhost", "dfr_" + bm + "_" + ae + "_" + norm, "doc",
							"/Users/Chaiyong/Documents/es_exp/methods", normModes, ngramSizes, true, true,
							"/Users/Chaiyong/Documents/es_exp", false, indexSettings);
				}
	}
}
