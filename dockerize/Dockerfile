FROM ubuntu

# Install Ubuntu dependencies
RUN apt-get update
RUN apt-get install -y git wget

# Install Elasticsearch
RUN wget https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.2.0/elasticsearch-2.2.0.tar.gz
RUN tar -xvf elasticsearch-2.2.0.tar.gz
RUN rm elasticsearch-2.2.0.tar.gz

# Install JDK
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y maven

# Install Siamese from Github
RUN git clone https://github.com/UCL-CREST/Siamese.git

# Add config script to Elasticsearch
RUN echo "cluster.name: stackoverflow\nindex.query.bool.max_clause_count: 4096" >> /elasticsearch-2.2.0/config/elasticsearch.yml

# Update Elasticsearch log in a config file
RUN sed -i "s|/Users/waynetsui/Desktop/GitHub/siamese/elasticsearch-2.2.0|$PWD/elasticsearch-2.2.0|g" /Siamese/config.properties

# Change directory to Siamease
WORKDIR /Siamese

# Compile packages
RUN mvn compile package
RUN cp -i target/siamese-0.0.6-SNAPSHOT.jar .

WORKDIR /