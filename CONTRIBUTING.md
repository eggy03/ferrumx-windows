# Contributing to ferrumx-windows

Thank you for taking the time to contribute :)

There are many ways to contribute to `ferrumx-windows` and each of them are encouraged and valued.
Before contributing, please make sure you read the relevant docs and wiki and check out the existing issues and examples
as it would help both you and the maintainers to keep the experience smooth.

## Table of Contents

- [Pre-requisites](#Pre-requisites)
- [Types of Contributions](#Types-Of-Contribution)
- [How to Contribute](#How-To-Contribute)


## Pre-requisites

Before contributing, please make sure you have checked out the following:

- [Project Readme](/README.md)
- [Code of Conduct](/CODE_OF_CONDUCT.md)
- [Javadocs](https://eggy03.github.io/ferrumx-windows-documentation/)
- [Wiki](https://github.com/eggy03/ferrumx-windows/wiki)
- [Examples](https://github.com/eggy03/ferrumx-windows-examples)
- [Existing Issues](https://github.com/eggy03/ferrumx-windows/issues)

## Types Of Contribution

- [Contributing to the Documentation](#Contributing-to-the-Documentation)
- [Contributing to the Examples](#contributing-to-the-examples)
- [Submitting an Issue (Questions / Bug Reports / Suggestions / Feature Requests)](#submitting-an-issue-questions--bug-reports--suggestions--feature-requests)
- [Testing the library on different hardware](#testing-the-library-on-different-hardware)
- [Reporting Vulnerabilities](#reporting-a-vulnerability)
- [Contributing to the code](#contributing-to-the-code)

## How To Contribute

### Contributing to the Documentation

You can always help contributing to the documentation by suggesting changes in the javadocs by either checking out the
hosted javadoc page or reading the ones in the source code itself. To suggest a change, create an issue
using the `suggestions` template.

The current wiki is sparse, and you can always help improve it by adding more explanations or making the existing
ones easier to understand and or less verbose. To suggest a change, create an issue
using the `suggestions` template.

### Contributing to the Examples

The [ferrumx-windows-examples repository](https://github.com/eggy03/ferrumx-windows-examples) 
could always use more examples and or cleaner, easier versions of examples than what is already there. You can either create a PR with your additions/changes
or suggest changes by raising an issue either here or in the example repository.

### Submitting an Issue (Questions / Bug Reports / Suggestions / Feature Requests)

If you have a question, or want to suggest changes or a feature or report bugs, you can easily create
an issue using one of the given templates. Before raising an issue, make sure you have checked the
existing ones and read the docs.

For questions and or suggestions, you can also post your message in [Discussions](https://github.com/eggy03/ferrumx-windows/discussions)

### Testing the library on different hardware

The current tests mock real-hardware to check whether the code works as expected. That being said, real world
hardware testing is the best way to detect and solve issues. You can run the examples on various hardware and windows
distributions with different versions of PowerShell and or different locales to find issues that my limited development
environment may have missed.

If you find an anomaly, you can report your findings by raising an issue on this repository's issue page.

### Reporting a vulnerability

Vulnerabilities should be reported in accordance with the [Security Policy](/SECURITY.md)


### Contributing to the code

Before contributing to the code, please make sure you have checked the existing open and closed [Pull Requests](https://github.com/eggy03/ferrumx-windows/pulls)
and are familiar with the docs, wiki and examples

#### Getting Started

To get started, ensure you have `git` and `maven` installed on your machine.
You can either install Maven or use the Maven wrapper by typing `./mvnw` instead of `mvn`.
Most IDEs have an embedded Maven with them so you don't need to install it manually.

If you are uncomfortable with using CLI, you can use GitHub Desktop for Windows or one of the tools in your IDE.
The steps below will remain the same for GUI editions as well.

#### Fork the Project

1. Fork the project on GitHub by clicking the "Fork" button in the top right corner of the project page.
2. Clone your fork to your local machine and set up a [triangle workflow](https://github.com/forwards/first-contributions/blob/master/additional-material/git_workflow_scenarios/keeping-your-fork-synced-with-this-repository.md) with these commands:

```shell
git clone https://github.com/yourusername/ferrumx-windows.git
cd ferrumx-windows
git remote add upstream https://github.com/eggy03/ferrumx-windows.git
```

#### Create a Branch

Make sure your fork is up-to-date and create a branch for your feature or bug fix.
The name `bugfix-branch` is just an example. You may choose a descriptive name of your liking.

```shell
git checkout main
git pull upstream main
git checkout -b bugfix-branch
```

#### Build and Test

Make sure you can build the project and run tests.

```shell
mvn clean test
```

- All tests must pass.
- If tests fail, and it’s not caused by your changes, note it and consider raising an issue.
- Avoid introducing new test failures.

#### Write Tests

- For bug fixes, try to write a test that reproduces the issue you are trying to fix (even if it fails).
- For new features, write tests to ensure the feature works as intended.
- Even if you do not have a fix, you can create a PR with a test that reproduces the issue.

#### Write Code

Write your feature or bug-fix. You may ask for help from any of the current project maintainer(s).
Run tests frequently to ensure your code does not fail the existing tests.


#### Update Changelog

The Changelog lets users know what's changed.
Edit [CHANGELOG](/CHANGELOG.md) to include your contribution under Next Release.
Follow the format of other entries, including your name and a link to your GitHub account:

` - Fixed skipping serialization of null fields - [#123](https://github.com/eggy03/ferrumx-windows/pulls)[@contributor](https://github.com/contributor).`

You can guess your pull request number as the next available number after issues and pull requests on the project.


#### Commit Changes

Make sure Git knows your name and email:

```shell
git config --global user.name "Your Name"
git config --global user.email "contributor@example.com"
```

Add your changed files to the index using [git add](https://git-scm.com/docs/git-add).
Most IDEs provide an easy way to do this.
Write [good commit messages](https://chris.beams.io/posts/git-commit/).
A commit message should describe what changed and why:

```shell
git add changedService.java
git commit -m "Fixed skipping serialization of null fields"
```

#### Push to Your GitHub Repository

```shell
git push origin bugfix-branch
```

#### Open a Pull Request (PR)

1. Go to https://github.com/yourusername/ferrumx-windows and select your feature branch.
2. Click the 'Pull Request' button and complete the form.
3. Pull requests are usually reviewed within a few days.

If code review requests changes (which often happens), simply `git push` the changes to your repository on the same branch,
and the pull request will be updated automatically.

#### Rebase

If you've been working on your change for a while and other commits have been made to the project, rebase with `upstream/main`:

```shell
git fetch upstream
git rebase upstream/main
git push origin bugfix-branch
```

#### Update Changelog Again
If you didn't guess the PR number right, update [CHANGELOG](/CHANGELOG.md) with the correct pull request number.

You can either amend your previous commit and force push the changes or create a new commit. The maintainers can squash them later.

```shell
git commit --amend
git push origin bugfix-branch -f
```

#### Check on Your Pull Request

After a few minutes, return to your pull request and see if it passed the CI tests.
Everything should look green. If not, read the failed test logs to identify issues, fix them, and commit as described above.
If the review succeeds, your changes will be merged.

Thank you for your contribution :)
