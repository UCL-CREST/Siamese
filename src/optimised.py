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
                        filename='optimised.log',
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


def gen_config_template():
    config = dict()
    config['elasticsearchLoc'] = '/Users/Chaiyong/Documents/phd/2017/Siamese/elasticsearch-2.2.0'
    config['server'] = 'localhost'
    config['cluster'] = 'stackoverflow'
    config['index'] = 'cloplag'
    config['type'] = 'siamese'
    config['inputFolder'] = ''
    config['subInputFolder'] = ''
    config['outputFolder'] = 'exp_results'
    config['normMode'] = 'djkopsvw'
    config['isNgram'] = 'true'
    config['dfs'] = 'true'
    config['writeToFile'] = 'true'
    config['extension'] = 'java'
    config['minCloneSize'] = '1'
    config['command'] = 'index'
    config['isPrint'] = 'false'
    config['outputFormat'] = 'csv'
    config['indexingMode'] = 'bulk'
    config['bulkSize'] = '4000'
    config['computeSimilarity'] = 'false'
    config['simThreshold'] = '80'
    config['deleteField'] = ''
    config['deleteWildcard'] = ''
    config['deleteAmount'] = '1000'
    config['methodParser'] = 'crest.siamese.helpers.JavaMethodParser'
    config['tokenizer'] = 'crest.siamese.helpers.JavaTokenizer'
    config['normalizer'] = 'crest.siamese.helpers.JavaNormalizer'
    config['resultOffset'] = '0'
    config['resultsSize'] = '10'
    config['totalDocuments'] = '100'
    config['recreateIndexIfExists'] = 'false'
    config['printEvery'] = '10000'
    config['rankingFunction'] = 'tfidf'
    config['deleteIndexAfterUse'] = 'true'
    config['multirep'] = 'true'
    config['queryReduction'] = 'true'
    config['ngramSize'] = '11'
    config['t2NgramSize'] = '16'
    config['t1NgramSize'] = '4'
    config['QRPercentileOrig'] = '10'
    config['QRPercentileT2'] = '10'
    config['QRPercentileT1'] = '10'
    config['QRPercentileNorm'] = '10'
    config['normBoost'] = '11'
    config['t2Boost'] = '16'
    config['t1Boost'] = '4'
    config['origBoost'] = '1'
    config['enableRep'] = 'true,true,true,true'
    config['license'] = 'true'
    config['licenseFileDetection'] = 'true'
    config['licenseExtractor'] = 'regexp'
    config["github"] = "true"
    config['parseMode'] = 'file'
    config['errorMeasure'] = 'map'
    config['similarityMode'] = 'tfidf_text_def'
    config['cloneClusterFile'] = 'soco'
    return config


def update_config(config, index, val):
    config[index][1] = val
    return config


def write_config(config):
    config_str = ""
    for key, value in config.items():
        config_str += key + "=" + value + "\n"
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


def update_config(config, key, val):
    config[key] = val
    return config


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
    command = ["mvn", "exec:java", "-Dexec.mainClass=crest.siamese.experiment.Experiment", "-Dexec.args=myconfig.properties"]
    # print(command)
    p = Popen(command, stdin=PIPE, stdout=PIPE, stderr=PIPE)
    output, err = p.communicate()
    output_decoded = output.decode().split('\n')
    for i in output_decoded:
        if i.startswith("MAP"):
            mapstr = i.split(' ')[2]
            writefile('optimised_summary.csv', mapstr + '\n', 'a', False)
            print(mapstr)
    # print(err)
    logging.debug(output.decode())
    logging.debug(err.decode())


def main():
    setup_logger()
    config = gen_config_template()
    update_config(config, 'inputFolder', '/Users/Chaiyong/Documents/phd/2016/cloplag/soco_f/formatted/')
    update_config(config, 'cloneClusterFile', 'soco')

    # update_config(config, 'inputFolder', '/Users/Chaiyong/Documents/phd/2016/cloplag/tests')
    # update_config(config, 'cloneClusterFile', 'cloplag')

    print('ngramSize,t2NgramSize,t1NgramSize,'
          'QRPercentileNorm,QRPercentileT2,QRPercentileT1,QRPercentileOrig,'
          'normBoost,t2Boost,t1Boost,map')
    writefile('optimised_summary.csv', 'ngramSize,t2NgramSize,t1NgramSize,'
                                       'QRPercentileNorm,QRPercentileT2,QRPercentileT1,QRPercentileOrig,'
                                       'normBoost,t2Boost,t1Boost,map\n', 'w', False)
    for i in range(1, 5):
        for j in range(1, 5):
            for k in range(1, 5):
                for l in range(1, 4):
                    # for m in range(1, 3):
                    #     for n in range(1, 3):
                    #         for o in range(1, 3):
                                mul = 10
                                print(str(i * 4) + ',' + str(j * 4) + ',' + str(k * 4) + ',' +
                                str(l * mul) + ',' + str(l * mul) + ',' + str(l * mul) + ',' + str(l * mul) + ',' +
                                str(i * 4) + ',' + str(j * 4) + ',' + str(k * 4) + ',', end='')
                                writefile('optimised_summary.csv',
                                          str(i * 4) + ',' + str(j * 4) + ',' + str(k * 4) + ',' +
                                          str(l * 30) + ',' + str(l * 30) + ',' + str(l * 30) + ',' + str(l * 30) + ',' +
                                          str(i * 4) + ',' + str(j * 4) + ',' + str(k * 4) + ',', 'a', False)
                                update_config(config, 'ngramSize', str(i * 4))
                                update_config(config, 't2NgramSize', str(j * 4))
                                update_config(config, 't1NgramSize', str(k * 4))
                                update_config(config, 'QRPercentileNorm', str(l * 10))
                                update_config(config, 'QRPercentileT2', str(l * 10))
                                update_config(config, 'QRPercentileT1', str(l * 10))
                                update_config(config, 'QRPercentileOrig', str(l * 10))
                                update_config(config, 'normBoost', str(i * 4))
                                update_config(config, 't2Boost', str(j * 4))
                                update_config(config, 't1Boost', str(k * 4))
                                write_config(config)
                                # writefile('optimised_summary.csv', '\n', 'a', False)
                                execute_siamese()


main()