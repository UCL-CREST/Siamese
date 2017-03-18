% read each result file separately
tfidf = csvread('rprec_tfidf_text_1gram.csv');
bm25 = csvread('rprec_bm25_text_1gram.csv');
dfr = csvread('rprec_dfr_text_1gram.csv');
ib = csvread('rprec_ib_text_1gram.csv');
% concatenate all the results
results = horzcat(tfidf,bm25,dfr,ib);
% plot a combined boxplot
boxplot(results);