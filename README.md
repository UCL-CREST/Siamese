# Siamese: Code Clone Search Engine

![Siamese logo](Logo.png "Siamese logo")

Siamese (**S**calable, **i**ncremental, **a**nd multi-repr**ese**ntation) is a 
code clone search system powered by Elasticsearch with 
code clone detection approaches, including code normalisation, *n*-grams, and query reduction technique, 
built on top. It can scalably search for clones of Type-1 to Type-3/Type-4 from a large corpora of Java source code within seconds.

<!--*Note: **Siamese** stands for **S**calalbe, **I**usingnstant, **A**nd **M**ulti-Repr**es**entation*

## Analyse term frequency and document frequency of terms in the index
1. Modify the class ```TermFreqAnalyser``` with appropriate configurations
2. The result frequency files will be generated (e.g. freq_df_src.csv, freq_df_toksrc.csv). The one without `tok` means the normalised source code tokens, whilst the one with `tok` means the original source code tokens.
3. Modify the sort_term.py script with the generated result frequency files and run the script.
4. Graphs will be genearated. They follow Zipf's law. Hooray!
-->

## Setup:
1\. Download elasticsearch-2.2.0 and extract to disk.

 ```
 mkdir ~/siamese
 cd ~/siamese
 wget https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/tar/elasticsearch/2.2.0/elasticsearch-2.2.0.tar.gz
 tar -xvf elasticsearch-2.2.0.tar.gz
 rm elasticsearch-2.2.0.tar.gz
 ```
 
2\. Modify the configuration file in ```config/elasticsearch.yml```

```
cd elasticsearch-2.2.0
vim config/elasticsearch.yml
```

Add the following lines at the end of the file. Save and quit.

```
cluster.name: stackoverflow
index.query.bool.max_clause_count: 4096
```

3\. Clone the project from GitHub.

```
cd ~/siamese
git clone https://github.com/UCL-CREST/Siamese.git
```

4\. Install JDK and Maven
```
sudo apt-get install default-jdk
sudo apt-get install maven
```

5\. Check if you can call ```javac```. 
```
javac
```

If ```javac``` does not produce any results, your ```JAVA_HOME``` is not set, set the JAVA_HOME by opening the file ```/etc/environment```

```bash
vim /etc/environment
```

and paste the location of JAVA_HOME at the end of the file. You can locate JAVA_HOME by

```
whereis javac
ls -l <the path>
... keep following the path until you find the real path (not a symlink) to the javac
```

5\. Modify the location of elasticsearch in `config.properties`.
```bash
elasticsearchLoc=/my/dir/elasticsearch-2.2.0
``` 
Save and quit.

```bash
cd Siamese
vim config.properties
```

6\. Try starting the elasticsearch service

```
./elasticsearch-2.2.0/bin/elasticsearch
```

You should see elasticsearch execution log like this.
```bash
[2018-10-02 03:50:35,305][INFO ][node                     ] [Warlock] version[2.2.0], pid[27101], build[8ff36d1/2016-01-27T13:32:39Z]
[2018-10-02 03:50:35,305][INFO ][node                     ] [Warlock] initializing ...
[2018-10-02 03:50:35,658][INFO ][plugins                  ] [Warlock] modules [lang-expression, lang-groovy], plugins [], sites []
[2018-10-02 03:50:35,674][INFO ][env                      ] [Warlock] using [1] data paths, mounts [[/ (/dev/sda2)]], net usable_space [107.8gb], net total_space [202.6gb], spins? [no], types [ext4]
[2018-10-02 03:50:35,674][INFO ][env                      ] [Warlock] heap size [989.8mb], compressed ordinary object pointers [true]
[2018-10-02 03:50:36,919][INFO ][node                     ] [Warlock] initialized
[2018-10-02 03:50:36,919][INFO ][node                     ] [Warlock] starting ...
[2018-10-02 03:50:36,982][INFO ][transport                ] [Warlock] publish_address {127.0.0.1:9300}, bound_addresses {[::1]:9300}, {127.0.0.1:9300}
[2018-10-02 03:50:36,989][INFO ][discovery                ] [Warlock] stackoverflow/VPfoqhukSoiP7RtKKgvYmg
[2018-10-02 03:50:40,037][INFO ][cluster.service          ] [Warlock] new_master {Warlock}{VPfoqhukSoiP7RtKKgvYmg}{127.0.0.1}{127.0.0.1:9300}, reason: zen-disco-join(elected_as_master, [0] joins received)
[2018-10-02 03:50:40,063][INFO ][http                     ] [Warlock] publish_address {127.0.0.1:9200}, bound_addresses {[::1]:9200}, {127.0.0.1:9200}
[2018-10-02 03:50:40,064][INFO ][node                     ] [Warlock] started
[2018-10-02 03:50:40,101][INFO ][gateway                  ] [Warlock] recovered [0] indices into cluster_state
```

Then, kill the process (Ctrl+C) and start the elasticsearch engine as a background service (with ```-d``` flag).

```bash
./elasticsearch-2.2.0/bin/elasticsearch -d
```

You can also test that elasticsearch is running in the background by issuing the command below.

```bash
curl -XGET 'localhost:9200/_cat/indices?v&pretty'
```

You should see the output like this, which means there is no index in elasticsearch yet.
```bash
health status index pri rep docs.count docs.deleted store.size pri.store.size 
```

7\. Create an executable jar and copy to the Siamese home directory
```bash
cd Siamese
mvn compile package
cp -i target/siamese-0.0.*.jar .
```

8\. Try to execute Siamese.
```bash
java -jar siamese-0.0.6-SNAPSHOT.jar 
```

9\. You will see how to execute Siamese printed on the screen.
```bash
$ java -jar siamese-0.0.6-SNAPSHOT.jar 
usage: \(v 0.6\) $java -jar siamese.jar -cf <config file> [-i input] [-o
          output] [-c command] [-h help]
Example: java -jar siamese.jar -cf config.properties
Example: java -jar siamese.jar -cf config.properties -i /my/input/dir -o
          /my/output/dir -c index
 -c,--command <arg>        [optional] command to execute [index, search].
                           This will override the configuration file.
 -cf,--configFile <arg>    [* requried *] a configuration file
 -h,--help                 <optional> print help
 -i,--inputFolder <arg>    [optional] location of the input files \(for
                           index or query\). This will override the
                           configuration file.
 -o,--outputFolder <arg>   [optional] location of the search result file.
                           This will override the configuration file.
``` 

10\. An example of running Siamese to index a project "foo".

```bash
java -jar siamese-0.0.6-SNAPSHOT.jar -c index -i /my/dir/foo -cf config.properties
```

11\. Then, tell Siamese to search for clones of "foo" in "bar".
```bash
java -jar siamese-0.0.6-SNAPSHOT.jar -c search -i /my/dir/bar -o /my/output/dir -cf config.properties
```

12\. After Siamese finishes its execution, the output file (clone classes) will be located at ```/my/output/dir```.
The file will be using the pattern ```data_qr_<timestamp>.xml```.

---

### Contact:
If you have any questions or find any issues, please contact Chaiyong Ragkhitwetsagul at ```cragkhit [at] gmail [dot] com```.

---

<!--
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
-->
