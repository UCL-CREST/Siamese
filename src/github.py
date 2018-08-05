import sys
from subprocess import Popen, PIPE
import matplotlib.pyplot as plt
import logging
logger = logging.getLogger(__name__)


def setup_logger():
    """
    Setting up a logger
    :return: N/A
    """
    logging.basicConfig(level=logging.DEBUG,
                        format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s',
                        datefmt='%y-%m-%d %H:%M',
                        filename='github_indexing.log',
                        filemode='a')
    # define a Handler which writes INFO messages or higher to the sys.stderr
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    # set a format which is simpler for console use
    formatter = logging.Formatter('%(name)-5s: %(levelname)-5s %(message)s')
    # tell the handler to use this format
    console.setFormatter(formatter)
    # add the handler to the root logger
    logging.getLogger('').addHandler(console)


def filter_proj_by_stars(file):
    file = open(file, 'r')
    stars_map = {}
    projs = []
    sum = 0
    stars = 0

    for line in file:
        if "Stars:" in line:
            stars = int(line.split("Stars:")[1].strip())
            if stars >= 0:
                sum += 1
                # print(stars, end=',')
                # if stars in stars_map:
                #     stars_map[stars] += 1
                # else:
                #     stars_map[stars] = 1
        if "Clone url: " in line and stars >= 0:
            repo = line.split("Clone url: ")[1].strip().replace("https://github.com/", "").replace(".git", "")
            # print(repo)
            projs.append([stars, repo])

    # print(stars_map)
    # print(sum)
    return projs


def gen_config_template():
    config = list()
    config.append(['elasticsearchLoc', '/Users/Chaiyong/Documents/phd/2017/Siamese/elasticsearch-2.2.0'])
    config.append(['server', 'localhost'])
    config.append(['cluster', 'stackoverflow'])
    config.append(['index', 'github_license'])
    config.append(['type', 'siamese'])
    config.append(['inputFolder', ''])
    config.append(['subInputFolder', ''])
    config.append(['outputFolder', 'exp_results'])
    config.append(['normMode', 'djkopsvw'])
    config.append(['isNgram', 'true'])
    config.append(['dfs', 'true'])
    config.append(['writeToFile', 'true'])
    config.append(['extension', 'java'])
    config.append(['minCloneSize', '1'])
    config.append(['command', 'index'])
    config.append(['isPrint', 'false'])
    config.append(['outputFormat', 'csv'])
    config.append(['indexingMode', 'bulk'])
    config.append(['bulkSize', '4000'])
    config.append(['computeSimilarity', 'none'])
    config.append(['simThreshold', '10%,10%,10%,10%'])
    config.append(['deleteField', ''])
    config.append(['deleteWildcard', ''])
    config.append(['deleteAmount', '1000'])
    config.append(['methodParser', 'crest.siamese.helpers.JavaMethodParser'])
    config.append(['tokenizer', 'crest.siamese.helpers.JavaTokenizer'])
    config.append(['normalizer', 'crest.siamese.helpers.JavaNormalizer'])
    config.append(['resultOffset', '0'])
    config.append(['resultsSize', '10'])
    config.append(['totalDocuments', '100'])
    config.append(['recreateIndexIfExists', 'false'])
    config.append(['printEvery', '10000'])
    config.append(['rankingFunction', 'tfidf'])
    config.append(['deleteIndexAfterUse', 'true'])
    config.append(['multirep', 'true'])
    config.append(['queryReduction', 'true'])
    config.append(['ngramSize', '11'])
    config.append(['t2NgramSize', '16'])
    config.append(['t1NgramSize', '4'])
    config.append(['QRPercentileOrig', '10'])
    config.append(['QRPercentileT2', '10'])
    config.append(['QRPercentileT1', '10'])
    config.append(['QRPercentileNorm', '10'])
    config.append(['normBoost', '11'])
    config.append(['t2Boost', '16'])
    config.append(['t1Boost', '4'])
    config.append(['origBoost', '1'])
    config.append(['enableRep', 'true,true,true,true'])
    config.append(['license', 'true'])
    config.append(['licenseFileDetection', 'true'])
    config.append(['licenseExtractor', 'regexp'])
    config.append(["github", "true"])
    config.append(['parseMode', 'method'])
    config.append(['errorMeasure', 'arp'])
    return config


def update_config(config, index, val):
    config[index][1] = val
    return config


def write_config(config):
    config_str = ""
    for line in config:
        config_str += line[0] + "=" + line[1] + "\n"
    writefile("myconfig.properties", config_str, 'w', False)


def writefile(filename, fcontent, mode, isprint):
    """
    Write the string content to a file
    copied from
    http://www.pythonforbeginners.com/files/reading-and-writing-files-in-python
    :param filename: name of the file
    :param fcontent: string content to put into the file
    :param mode: writing mode, 'w' overwrite every time, 'a' append to an existing file
    :return: N/A
    """
    # try:
    file = open(filename, mode)
    file.write(fcontent)
    file.close()


def update_config(config, index, val):
    config[index][1] = val
    return config


def write_config(config):
    config_str = ""
    for line in config:
        config_str += line[0] + "=" + line[1] + "\n"
    writefile("myconfig.properties", config_str, 'w', False)


def writefile(filename, fcontent, mode, isprint):
    """
    Write the string content to a file
    copied from
    http://www.pythonforbeginners.com/files/reading-and-writing-files-in-python
    :param filename: name of the file
    :param fcontent: string content to put into the file
    :param mode: writing mode, 'w' overwrite every time, 'a' append to an existing file
    :return: N/A
    """
    # try:
    file = open(filename, mode)
    file.write(fcontent)
    file.close()


def execute_siamese():
    command = ["java", "-jar", "-Xss8g", "siamese-0.0.6-SNAPSHOT.jar", "-cf", "myconfig.properties"]
    # print(command)
    p = Popen(command, stdin=PIPE, stdout=PIPE, stderr=PIPE)
    output, err = p.communicate()
    logging.debug(output.decode())
    logging.debug(err.decode())


def plot_hist(list):
    f = plt.figure()
    plt.hist(list, bins=1000,log=True)
    # plt.boxplot(list)
    plt.title("Distribution of GitHub project stars: 29465 - 1")
    plt.xlabel("Stars")
    plt.ylabel("Frequency (log)")
    # plt.show()
    f.savefig("stars.pdf", bbox_inches='tight')


def analyse_projects(projs, start, end):
    ###
    # for printing histogram
    ###
    print('max: ' + str(projs[0][0]))
    stars_list = list()
    count = 0

    for proj in projs:
        if start >= proj[0] >= end:
            stars_list.append(proj[0])
            count += 1

    print('amount:', count)
    # plot_hist(stars_list)


def main():
    setup_logger()
    projs = filter_proj_by_stars(sys.argv[1])
    logging.info('total:' + str(len(projs)))
    start = 29465
    end = 10
    # start = 0
    # end = 0
    # analyse_projects(projs, 2, 1)

    count = 0
    for idx, proj in enumerate(projs):
        if start >= proj[0] >= end:
            count += 1
            logging.info('No. ' + str(idx) + ',' + str(proj[0]) + ',' + str(proj[1]))
            config = gen_config_template()
            config = update_config(config, 5, sys.argv[2])
            config = update_config(config, 6, proj[1])
            if idx == 0: # first project, recreate the index
                config = update_config(config, 30, "true")
            write_config(config)
            execute_siamese()
        # if idx > 1:
        exit(0)
    logging.debug('Total: ' + str(count))


main()
