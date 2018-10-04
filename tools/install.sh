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
        rm -rf ${INSTALL_DIR}
    else
        echo 'Installation canceled.'
        exit 0
    fi
fi

LATEST_RELEASE_URL="https://api.github.com/repos/mbrtargeting/auto-snapshotor/releases/latest"
RELEASE_NAME=$(curl "${LATEST_RELEASE_URL}" | grep "tag_name" | sed -E 's/.*"([^"]+)".*/\1/')
DOWNLOAD_URL="https://github.com/mbrtargeting/auto-snapshotor/releases/download/${RELEASE_NAME}/"

#profile variables
as_bash_profile="${HOME}/.bash_profile"
as_profile="${HOME}/.profile"
as_bashrc="${HOME}/.bashrc"
as_zshrc="${HOME}/.zshrc"

init_snippet=$( cat << EOF
#THIS IS NEEDED FOR AUTO_SNAPSHOTOR TO WORK!!!
export AS_DIR="${INSTALL_DIR}"
if [[ -f "${INSTALL_DIR}${JAR_FILE_NAME}" ]]; then
    alias snapshot="${INSTALL_DIR}${WRAPPER_SCRIPT}"
fi
EOF
)

# OS specific support (must be 'true' or 'false').
cygwin=false;
darwin=false;
solaris=false;
freebsd=false;
case "$(uname)" in
    CYGWIN*)
        cygwin=true
        ;;
    Darwin*)
        darwin=true
        ;;
    SunOS*)
        solaris=true
        ;;
    FreeBSD*)
        freebsd=true
esac


echo "Making installation directory..."
mkdir -p ${INSTALL_DIR}

downloadAsset ${JAR_FILE_NAME}
downloadAsset ${WRAPPER_SCRIPT}

if [[ $darwin == true ]]; then
    touch "${as_bash_profile}"
    echo "Try to update login bash profile for osx."
    if [[ -z $(grep 'AS_DIR' "${as_bash_profile}") ]]; then
        echo -e "\n$init_snippet" >> "${as_bash_profile}"
        echo "Added as init snippet to ${as_bash_profile}"
    fi
else
    echo "Try to update interactive bash profile for linux."
    touch "$as_bashrc"
    if [[ -z $(grep 'AS_DIR' "${as_bashrc}") ]]; then
        echo -e "\n$init_snippet" >> "${as_bashrc}"
        echo "Added as init snippet to ${as_bashrc}"
    fi
fi

echo "Try to update zsh profile"
touch "${as_zshrc}"
if [[ -z $(grep 'AS_DIR' "${as_zshrc}") ]]; then
    echo -e "\n$init_snippet" >> "${as_zshrc}"
    echo "Added as init snippet to ${as_zshrc}"
fi


echo "DONE!"

echo "Please restart your terminal or reload you bash profile."
