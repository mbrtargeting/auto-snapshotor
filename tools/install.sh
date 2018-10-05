#!/bin/bash

echo "             _                                        _           _"
echo "  __ _ _   _| |_ ___        ___ _ __   __ _ _ __  ___| |__   ___ | |_ ___  _ __"
echo " / _\` | | | | __/ _ \ _____/ __| '_ \ / _\` | '_ \/ __| '_ \ / _ \| __/ _ \| '__|"
echo "| (_| | |_| | || (_) |_____\__ \ | | | (_| | |_) \__ \ | | | (_) | || (_) | |"
echo " \__,_|\__,_|\__\___/      |___/_| |_|\__,_| .__/|___/_| |_|\___/ \__\___/|_|"
echo "                                           |_|"

set -eu

PLATFORM=$(uname)
INSTALL_DIR="${HOME}/.auto-snapshotor/"
JAR_FILE_NAME="auto-snapshotor"
WRAPPER_SCRIPT="snapshot.sh"

function checkToolInstalled() {
    TOOL_NAME=$1
    echo "Looking for ${TOOL_NAME}..."
    if [ -z $(which ${TOOL_NAME}) ]; then
        echo "Not found."
        echo ""
        echo "======================================================================================================"
        echo " Please install ${TOOL_NAME} on your system using your favourite package manager."
        echo ""
        echo " Restart after installing ${TOOL_NAME}."
        echo "======================================================================================================"
        echo ""
        exit 0
    else
        echo "Found ${TOOL_NAME}."
    fi
}

function downloadAsset() {
    FILE_NAME=$1
    echo "Start downloading file from ${DOWNLOAD_URL}${FILE_NAME}"
    result=$(curl --progress-bar -L -o "${INSTALL_DIR}${FILE_NAME}" ${DOWNLOAD_URL}${FILE_NAME})

    echo "Running chmod +x on ${INSTALL_DIR}${FILE_NAME}"
    chmod +x ${INSTALL_DIR}${FILE_NAME}
}

checkToolInstalled curl
checkToolInstalled grep
checkToolInstalled sed

if [ -d ${INSTALL_DIR} ]; then
    echo "${INSTALL_DIR} already exist. "
    read -p 'Reinstall? [y/N]:' REINSTALL </dev/tty
    if [[ ${REINSTALL}  == 'y' ]] || [[ ${REINSTALL} == 'Y' ]] ; then
        echo "Reinstalling auto-snapshoter..."
    else
        echo 'Installation canceled.'
        exit 0
    fi
else
    echo "Making installation directory..."
    mkdir -p ${INSTALL_DIR}
fi

LATEST_RELEASE_URL="https://api.github.com/repos/mbrtargeting/auto-snapshotor/releases/latest"
RELEASE_NAME=$(curl "${LATEST_RELEASE_URL}" | grep "tag_name" | sed -E 's/.*"([^"]+)".*/\1/')
DOWNLOAD_URL="https://github.com/mbrtargeting/auto-snapshotor/releases/download/${RELEASE_NAME}/"

downloadAsset ${JAR_FILE_NAME}
echo '' > "${INSTALL_DIR}${RELEASE_NAME}"

echo "DONE!"
