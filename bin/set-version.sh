#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

fail() {
    echo >&2 "$@"
    exit 1
}

function log() {
  purple="\033[0;35m"
  bold=$(tput bold)
  normal=$(tput sgr0)
  echo -e "${purple}${bold}>>> ${1}${normal}"
}

[ "$#" -eq 1 ] || fail "You must specify a version to run this script"

log "Setting version $1"
mvn versions:set -DgenerateBackupPoms=false -DnewVersion="$1"
log "Project version set to $1"
