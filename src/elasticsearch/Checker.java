package elasticsearch;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Checker {
	private static ESConnector es = new ESConnector("cloplag");

	public static void main(String[] args) {
		try {
			es.startup();
			// read the content of the file
			String content = readFile(args[0], Charset.defaultCharset());
			JavaTokenizer tokenizer = new JavaTokenizer(Settings.Normalize.MED_NORM);
			ArrayList<String> tokens = tokenizer.getTokensFromFile(args[0]);
			String query = printArray(tokens, false);
			Path p = Paths.get(args[0]);
			String file = p.getFileName().toString();
			System.out.print(file + ",");
			// System.out.print(file.split("\\$")[0] + ",");
			System.out.println(findTP(es.search(query), file.split("\\$")[0]));
			es.shutdown();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int findTP(ArrayList<String> results, String query) {
		int tp = 0;
		for (int i=0; i<results.size(); i++) {
			// System.out.println(results.get(i));
			if (results.get(i).contains(query)) {
				tp++;
			}
		}
		return tp;
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static String printArray(ArrayList<String> arr, boolean pretty) {
		String s = "";
		for (int i = 0; i < arr.size(); i++) {
			if (pretty && arr.get(i).equals("\n")) {
				System.out.print(arr.get(i));
				continue;
			}
			s += arr.get(i) + " ";
		}
		return s;
	}
}
