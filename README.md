# elasticsearch
Similar code search using Elasticsearch

Setup steps:
1. Download elasticsearch-2.2.0 
```
wget https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.2.0/elasticsearch-2.2.0.tar.gz
```
And extract it to the disk.
```
tar -xvf elasticsearch-2.2.0.tar.gz
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
7. Execute the experiment.
```
mvn compile exec:java -Dexec.mainClass=elasticsearch.main.Experiment -Dexec.args="tfidf /home/cragkhit/cloplag/tests_andrea/ /home/cragkhit/elasticsearch/results/170320_full/"
```
