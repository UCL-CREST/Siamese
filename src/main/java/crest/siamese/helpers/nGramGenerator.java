/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.helpers;

import java.util.ArrayList;

public class nGramGenerator {
	private int n;
	public nGramGenerator(int n) {
		// set the number "n" in n-grams
		this.n = n;
	}
	
	public String[] generateNGrams(String s) {
		// no. of n-grams from a string S  = size(S)-N+1
		int noOfNGrams = s.length()-this.n+1;
		// create an array to store the n-grams
		String[] ngrams = new String[noOfNGrams];
		for (int i = 0; i < noOfNGrams; i++) {
			ngrams[i] = s.substring(i, i + this.n);
			// System.out.print(ngrams[i] + " ");
		}
		// System.out.println();
		return ngrams;
	}
	
	public ArrayList<String> generateNGramsFromJavaTokens(ArrayList<String> tokens) {
		// no. of n-grams from a string S  = size(S)-N+1
		int noOfNGrams = tokens.size()-this.n+1;
		// create an array to store the n-grams
		ArrayList<String> ngrams = new ArrayList<String>();
		for (int i = 0; i < noOfNGrams; i++) {
			String finalNgrams = "";
			for (int j=0; j < this.n; j++) {
				finalNgrams += tokens.get(i+j);
			}
			ngrams.add(finalNgrams);
			// System.out.print(ngrams.get(i) + " / ");
		}
		// System.out.println();
		return ngrams;
	}
}
