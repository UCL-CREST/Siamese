#!../python_launcher/python_launcher.sh

"""Pull the most popular github repos in a given language to a given path.

Example usages:

./pull_most_starred_github_repos.py  --number-of-repos=2

monoprodo/research/python_launcher/python_launcher.sh \
    monoprodo/research/scripts/pull_most_starred_github_repos.py \
        -p /mnt/data_volume/data/git_repos/
"""

import argparse
import os
import re
import shutil
import sys
import time

import config_constants
import log_utils

from git import InvalidGitRepositoryError, Repo
from github import Github


class PullMostStarredGithubRepos:  # pylint: disable=too-few-public-methods
    """Use pullRepos with a list of arguments to be processed by parseargs"""

    def __init__(self):
        self.logger = log_utils.get_logger(__name__, type(self).__name__)

    def _parse_arguments(self, argv):
        """Parse arguments from the command line."""
        parser = argparse.ArgumentParser(
            description='Pull the most starred projects from github')
        parser.add_argument('--number-of-repos', '-n', nargs='?', type=int,
                            default=100, help='numbers of repos to be pulled')
        parser.add_argument('--language', '-l', nargs='?', type=str,
                            default='javascript',
                            help='language of the repos to be pulled')
        parser.add_argument('--path-to-git-repos', '-p', nargs='?', type=str,
                            default=config_constants.PATH_TO_GIT_REPOS,
                            help='path where the repos will be pulled')
        parser.add_argument('--logging-level', nargs='?', type=str,
                            default=log_utils.LOG_ALL_MESSAGES,
                            help='verbosity of the messages. Same choices ' +
                            'as for the logging python package')
        arguments = parser.parse_args(argv)

        if arguments.number_of_repos <= 0:
            log_utils.log_and_exit_with_error(
                self.logger, 'The number of repos must be a positive number',
                2)

        if not os.path.isdir(arguments.path_to_git_repos):
            log_utils.log_and_exit_with_error(
                self.logger, 'The first argument must be an absolute ' +
                'directory path', 2)

        log_utils.set_level(self.logger, arguments.logging_level)

        return arguments

    def _get_owner_and_name(self, clone_url):
        """Get the owner and name of a repo from the URL.

        Not getting them from the API object as they differ and the URL is
        likely to be more permanent."""
        owner_and_name = re.match(
            r'https://github\.com/([^/]*)/(.*)\.git', clone_url)
        (owner, name) = (owner_and_name.group(1), owner_and_name.group(2))

        if not owner or not name:
            log_utils.log_and_exit_with_error(
                self.logger, 'Can\'t extract owner and name from URL: ' +
                clone_url)
        return (owner, name)

    def _clone_repo_in_new_dir(self, clone_url, path_to_this_repo):
        """Creates a new directory and clones a repo there"""
        self.logger.info('Cloning into new dir %s', path_to_this_repo)
        os.makedirs(path_to_this_repo)
        Repo.clone_from(clone_url, path_to_this_repo, depth='1')
        self.logger.info('Finished cloning')

    def _pull_local_repo(self, clone_url, path_to_this_repo):
        """Pulls a local repo, or clones anew if there's trouble pulling"""
        try:
            self.logger.info('Trying to pull %s', path_to_this_repo)
            repo = Repo(path_to_this_repo)
            repo.remotes.origin.pull()
            self.logger.info('Finished pulling')
        except InvalidGitRepositoryError:
            shutil.rmtree(path_to_this_repo)
            os.makedirs(path_to_this_repo)
            self.logger.info(
                'Cloning because of invalid dir %s', path_to_this_repo)
            Repo.clone_from(clone_url, path_to_this_repo, depth='1')
            self.logger.info('Finished cloning because of invalid dir')

    def pull_repos(self, argv):
        """Pull git repos according to the arguments."""

        arguments = self._parse_arguments(argv)

        github = Github()

        os.chdir(arguments.path_to_git_repos)

        repos = github.search_repositories(
            query='language:'+arguments.language, sort='stars', order='desc')
        for i in range(arguments.number_of_repos):
            repo = repos[i]
            (repo_owner, repo_name) = self._get_owner_and_name(repo.clone_url)
            self.logger.info('Stars: %d', repo.stargazers_count)
            path_to_this_repo = os.path.join(
                arguments.path_to_git_repos, arguments.language, repo_owner,
                repo_name)
            if not os.path.isdir(path_to_this_repo):
                self._clone_repo_in_new_dir(repo.clone_url, path_to_this_repo)
            else:
                self._pull_local_repo(repo.clone_url, path_to_this_repo)
            time.sleep(1)


if __name__ == "__main__":
    PullMostStarredGithubRepos().pull_repos(sys.argv[1:])
