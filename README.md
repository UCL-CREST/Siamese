# Siamese: Code Clone Search Engine

[logo](/images/logo.png?raw=true "Siamese logo")
[alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

Siamese is a code clone search system powered by Elasticsearch with code clone detection approaches, code normalisation and ngram-based matching, built on top. It can scalably search for clones of type-1 to type-3 from a large corpora of Java source code within seconds.

*Note: **Siamese** stands for **S**calalbe, **I**usingnstant, **A**nd **M**ulti-Repr**es**entation*

## Analyse term frequency and document frequency of terms in the index
1. Modify the class ```TermFreqAnalyser``` with appropriate configurations
2. The result frequency files will be generated (e.g. freq_df_src.csv, freq_df_toksrc.csv). The one without `tok` means the normalised source code tokens, whilst the one with `tok` means the original source code tokens.
3. Modify the sort_term.py script with the generated result frequency files and run the script.
4. Graphs will be genearated. They follow Zipf's law. Hooray!

## Setup:
1. Download elasticsearch-2.2.0
```
wget https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.2.0/elasticsearch-2.2.0.tar.gz
```
And extract it to the disk.
```
tar -xvf elasticsearch-2.2.0.tar.gz
rm elasticsearch-2.2.0.tar.gz
```
2. Modify the configuration file in config/elasticsearch.yml
```
cluster.name: stackoverflow
index.query.bool.max_clause_count: 4096
```
3. Clone the project from GitHub.
```
git clone git@github.com:cragkhit/elasticsearch.git
```
4. Install Maven.
```
sudo apt-get update
sudo apt-get install maven
```
5. Run elasticsearch
```
./elasticsearch-2.2.0/bin/elasticsearch -d
```
6. Install JDK
```
sudo apt-get install default-jdk
```
7. Set JAVA_HOME
```
vim /etc/environment
```
and paste the location of JAVA_HOME into the file.
Note: finding JAVA_HOME by
```
whereis javac
ls -l <the path>
... keep following the path
```
8. Configure `config.properties` file with appropriate settings

9. Execute the experiment.
```
mvn compile exec:java -Dexec.mainClass=crest.siamese.Main -Dexec.args="-cf config.properties"
```

# Early Experimental Results
No. of combinations of IR scoring parameter I searched for:

| Func. | n-gram | code norm. | params | Total |
|-------------------|--------|------------|--------|-------|
| TF-IDF | 4 | 64 | 3 | 768 |
| BM25 | 4 | 64 | 5*5*2 | 12800 |
| DFR | 4 | 64 | 7*3*5 | 26880 |
| IB | 4 | 64 | 2*2*5 | 5120 |
| LM Dirichlet | 4 | 64 | 6 | 1536 |
| LM Jelinek Mercer | 4 | 64 | 10 | 2560 |
| Grand total |  |  |  | 49664 |

## Misc.
### Read Lucene index direclty:
* http://stackoverflow.com/questions/20575254/lucene-4-4-how-to-get-term-frequency-over-all-index
* http://stackoverflow.com/questions/16847857/how-do-you-read-the-index-in-lucene-to-do-a-search
