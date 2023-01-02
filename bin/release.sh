#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

function log() {
  purple="\033[0;35m"
  bold=$(tput bold)
  normal=$(tput sgr0)
  echo -e "${purple}${bold}>>> ${1}${normal}"
}

current_version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
log "Current version: ${current_version}"

next_version=$(echo "${current_version}" | sed 's/-SNAPSHOT//g')
log "Next version: ${next_version}"

log "Updating README.md"
sed -i '' "s/<version>.*<\/version>/<version>${next_version}<\/version>/" README.md
gradle_prefix='testImplementation "com.mikemybytes:junit5-formatted-source:'
sed -i '' "s/testImplementation.*junit5-formatted-source:.*/${gradle_prefix}${next_version}\"/" README.md
git add README.md
git commit -m "docs: [release] Update artifact coordinates (new version ${next_version})"

log "Building with Maven"
mvn clean verify

log "Preparing Maven release"
mvn --batch-mode release:clean release:prepare

released_version=$(git describe --abbrev=0 --tags)
log "Released version: ${released_version}"

log "Cleaning up after Maven release"
mvn --batch-mode release:clean

log "Pushing to git"
git push origin main
git push origin "${released_version}"

log "Checking out released sourced"
git checkout --quiet "${released_version}"

log "Staging artifacts"
# ensure all artifacts are staged
# https://jreleaser.org/guide/latest/examples/maven/staging-artifacts.html
mvn deploy -DaltDeploymentRepository=local::file:./target/staging-deploy

log "Invoking jreleaser"
mvn jreleaser:full-release

log "Checking out main"
git checkout main

log "Successfully released version ${released_version}"
