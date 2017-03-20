% read each result file separately
tfidf = csvread('rprec_tfidf_text_1gram.csv');
bm25 = csvread('rprec_bm25_text_1gram.csv');
dfr = csvread('rprec_dfr_text_1gram.csv');
ib = csvread('rprec_ib_text_1gram.csv');
lmd = csvread('rprec_lmd_text_1gram.csv');
lmj = csvread('rprec_lmj_text_1gram.csv');
% concatenate all the results
results = horzcat(tfidf,bm25,dfr,ib,lmd,lmj);
% plot a combined boxplot
boxplot(results);
hold on
% add mean as a circle on the boxplot
plot(1, mean(tfidf), 'bo')
plot(2, mean(bm25), 'bo')
plot(3, mean(dfr), 'bo')
plot(4, mean(ib), 'bo')
plot(5, mean(lmd), 'bo')
plot(6, mean(lmj), 'bo')
mean(tfidf)
mean(bm25)
mean(dfr)
mean(ib)
mean(lmd)
mean(lmj)
% xlabel('IR Scoring Function');
ylabel('r-precision');
xticks([1 2 3 4 5 6])
xticklabels({'TFIDF' 'BM25' 'DFR' 'IB' 'LMDirichlet' 'LMJelinekM.'})
set(gca,'FontSize',16);

h=gcf;
set(h,'PaperPositionMode','auto');         
set(h,'PaperOrientation','landscape');
set(h,'Position',[50 50 900 400]);
print('text_search_results','-dpdf','-bestfit')