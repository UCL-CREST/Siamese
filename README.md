# Siamese: Code Clone Search Engine

![Siamese logo](Logo.png "Siamese logo")

Siamese (**S**calable, **i**ncremental, **a**nd **m**ulti-repr**ese**ntation) is a
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

## Build from Source:
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

11\. Then, tell Siamese to search for clones of "bar" in the index of "foo".
```bash
java -jar siamese-0.0.6-SNAPSHOT.jar -c search -i /my/dir/bar -o /my/output/dir -cf config.properties
```

12\. After Siamese finishes its execution, the output file (clone classes) will be located at ```/my/output/dir```.
The file will be using the pattern ```data_qr_<timestamp>.xml```.

13\. If you want to enforce similarity threshold on the search results,
modify the ```config.properties``` file to enable fuzzywuzzy or tokenratio (recommended) similarity.
Choose any similarity thresholds you like for the four code representations (r0, r1, r2, r3) respectively.

```bash
computeSimilarity : tokenratio
simThreshold      : 50%,50%,50%,50%
```

---

## Downloads
* **Executable Tool (JAR file)**:
    * **Siamese:** Siamese executable can be downloaded here: [Siamese v. 0.6](https://drive.google.com/open?id=1lQX4SvQbxi9WYH4ndRilc_s45gBsi0Sx). Please make sure you have Java 8 installed on your machine.

        1\. To execute Siamese, unzip the file and follow the steps below:
        ```bash
        $cd siamese
        $./elasticsearch-2.2.0/bin/elasticsearch -d
        $java -jar siamese-0.0.5-SNAPSHOT.jar
        ```
        Then you'll see the usage and example of how to use Siamese.
        ```
        usage: (v 0.5) $java -jar siamese.jar -cf <config file> [-i input] [-o output] [-c command] [-h help]
          Example: java -jar siamese.jar -cf config.properties
          Example: java -jar siamese.jar -cf config.properties -i /my/input/dir -o /my/output/dir -c index
           -c,--command <arg>        [optional] command to execute [index, search].
                                     This will override the configuration file.
           -cf,--configFile <arg>    [* requried *] a configuration file
           -h,--help                 <optional> print help
           -i,--inputFolder <arg>    [optional] location of the input files (for
                                     index or query). This will override the
                                     configuration file.
           -o,--outputFolder <arg>   [optional] location of the search result file.
                                     This will override the configuration file.
        ```
        2\. An example of running Siamese to index a project "foo".

        ```bash
        java -jar siamese-0.0.6-SNAPSHOT.jar -c index -i /my/dir/foo -cf config.properties
        ```

        3\. Then, tell Siamese to search for clones of "bar" in "foo".
        ```bash
        java -jar siamese-0.0.6-SNAPSHOT.jar -c search -i /my/dir/bar -o /my/output/dir -cf config.properties
        ```

        4\. After Siamese finishes its execution, the output file (clone classes) will be located at ```/my/output/dir```.
        The file will be using the pattern ```data_qr_<timestamp>.xml```.

        5\. If you want to enforce similarity threshold on the search results,
        modify the ```config.properties``` file to enable fuzzywuzzy or tokenratio (recommended) similarity.
        Choose any similarity thresholds you like for the four code representations (r0, r1, r2, r3) respectively.

        ```bash
        computeSimilarity : tokenratio
        simThreshold      : 50%,50%,50%,50%
        ```

    * **BigCloneEval:** BigCloneEval is a tool for automated recall evaluation based on BigCloneBench data set. It can be downloaded from: [BigCloneBench](https://github.com/jeffsvajlenko/BigCloneEval)
* **Data sets**: the data sets that we used to evaluate Siamese are listed below:
    * **OCD (Obfuscation/Compilcation/Decompilation)** data set. The OCD data set is from a study by Ragkhitwetsagul et al. and can be found here: [OCD data set](http://crest.cs.ucl.ac.uk/resources/cloplag/).
    * **SOCO (SOurce COde Re-use)** data set. The SOCO data set was created for the detection of source code reuse competition and can be downloaded here: [SOCO](http://users.dsic.upv.es/grupos/nle/soco/). However, the clone oracle has some issues which Ragkhitwetsagul et al. found and fixed. Please download the corrected clone oracle from: [Fixed clone oracle](http://crest.cs.ucl.ac.uk/fileadmin/crest/cloplag/soco_train_clones_fixed.txt).
    * **BigCloneBench** data set. The BigCloneBench is created by Svajlenko et al., it is one of the largest clone benchmarks available to date. It is created from IJaDataset 2.0 of 25,000 Java systems. The benchmark contains 2.8 million files with 8 million manually validated clone pairs of type-1 up to type-4. The data set and the clone oracle can be downloaded here: [BigCloneBench](https://github.com/jeffsvajlenko/BigCloneEval).
    * **GitHub** data set. We used 16,738 and 130,719 GitHub Java projects to evaluate Siamese's precision and incremental update module. Since the projects are all open source, you can download the GitHub projects from [GitHub](https://github.com) directly. The list of the projects we used can be found below:
      * [16,738 GitHub projects](https://raw.githubusercontent.com/UCL-CREST/Siamese/master/resources/github_proj_max_to_10.csv)
      * [130,719 GitHub projects]()
    * **10 highest-voted Stack Overflow code snippets** We reused the code snippets from Kim et al.'s study. The 10 code queries from the 10 highest-voted Stack Overflow code snippets can be found here: [FaCoy website](https://github.com/FalconLK/FaCoY/tree/release-1.0/RQs/onlinequery)

* **Additional Evaluation Results**:
  * **RQ2 Comparison with Code Search Tools**
  Due to limited space, we do not include all the results from using the 10 highest-voted Stack Overflow posts in the paper. We thus include them here.
  * The full search results can be found [here](https://raw.githubusercontent.com/UCL-CREST/Siamese/master/results/results_for_rq2/10_so_snippets_clones_web.csv)
    * **How to read the results** Siamese search results include multiple parts: (1) file path, (2) method name, (3) starting and ending line.

    * For example, a clone pair of ```10_so/299495_0.java_paintComponent#22#26``` and ```mattibal/meshnet/MeshNetBase/src/com/mattibal/meshnet/utils/color/gui/LabChooserJFrame.java_paintComponent#89#95``` means the method ```painComponent``` in the file ```10_so/299495_0.java``` from line number 22 to 26 is a clone of the method ```paintComponent``` in the file ```mattibal/meshnet/MeshNetBase/src/com/mattibal/meshnet/utils/color/gui/LabChooserJFrame.java``` from line 89 to line 95.

---

### Contact:
If you have any questions or find any issues, please contact Chaiyong Ragkhitwetsagul at ```cragkhit [at] gmail [dot] com``` or Jens Krinke at ```j.krinke [at] ucl [dot] ac [dot] uk```.

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
