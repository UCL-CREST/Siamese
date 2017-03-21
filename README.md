# elasticsearch
Similar code search using Elasticsearch

Setup steps:
1. Download elasticsearch-2.2.0 from https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.2.0/elasticsearch-2.2.0.tar.gz and extract it to the disk.
2. Modify the configuration file in config/elasticsearch.yml
cluster.name: stackoverflow
index.query.bool.max_clause_count: 4096
3. Clone the project from GitHub.
4. Install Maven: sudo apt-get install maven
