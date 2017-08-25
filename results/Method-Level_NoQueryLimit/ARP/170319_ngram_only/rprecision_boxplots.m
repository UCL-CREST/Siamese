scoring_fnc = 'tfidf';
scoring_name = 'TFIDF';
% read each result file separately
x1gram = csvread(strcat('rprec_',scoring_fnc, '_x_1.csv'));
x2gram = csvread(strcat('rprec_',scoring_fnc, '_x_2.csv'));
x3gram = csvread(strcat('rprec_',scoring_fnc, '_x_3.csv'));
x4gram = csvread(strcat('rprec_',scoring_fnc, '_x_4.csv'));
x5gram = csvread(strcat('rprec_',scoring_fnc, '_x_5.csv'));
% concatenate all the results
results = horzcat(x1gram,x2gram,x3gram,x4gram,x5gram);
% plot a combined boxplot
boxplot(results);
hold on
% add mean as a circle on the boxplot
plot(1, mean(x1gram), 'bo')
plot(2, mean(x2gram), 'bo')
plot(3, mean(x3gram), 'bo')
plot(4, mean(x4gram), 'bo')
plot(5, mean(x5gram), 'bo')
mean(x1gram)
mean(x2gram)
mean(x3gram)
mean(x4gram)
mean(x5gram)
xlabel(scoring_name);
ylabel('r-precision');
xticks([1 2 3 4 5])
xticklabels({'1-gram' '2-gram' '3-gram' '4-gram' '5-gram' '6-gram'})
set(gca,'FontSize',20);

h=gcf;
set(h,'PaperPositionMode','auto');         
set(h,'PaperOrientation','landscape');
set(h,'Position',[50 50 400 400]);
print(strcat(scoring_fnc, '_ngram'),'-dpdf','-bestfit');