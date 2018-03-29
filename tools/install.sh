#!/bin/bash

set -eu

DEV_DIR="${HOME}/development/"
#TODO: Update download url for executable jar
DOWNLOAD_URL="https://github.com/mbrtargeting/auto-snapshotor/archive/master.zip"
DOWNLOAD_FILE_NAME="as"
PLATFORM=$(uname)
INSTALL_DIR="${HOME}/"

#profile variables
as_bash_profile="${HOME}/.bash_profile"
as_profile="${HOME}/.profile"
as_bashrc="${HOME}/.bashrc"
as_zshrc="${HOME}/.zshrc"

if [ -d $DEV_DIR ]; then
    echo "found development folder, will install snapshotor to ${DEV_DIR}"
    INSTALL_DIR=$DEV_DIR
else
    echo "could not find development dir, will install to user home directory ${INSTALL_DIR}"
fi

init_snippet=$( cat << EOF
#THIS IS NEEDED FOR AUTO_SNAPSHOTOR TO WORK!!!
export AS_DIR="${INSTALL_DIR}"
[[ -d "${INSTALL_DIR}${DOWNLOAD_FILE_NAME}" ]] && PATH="$PATH:${INSTALL_DIR}"
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

echo "Looking for curl..."
if [ -z $(which curl) ]; then
	echo "Not found."
	echo ""
	echo "======================================================================================================"
	echo " Please install curl on your system using your favourite package manager."
	echo ""
	echo " Restart after installing curl."
	echo "======================================================================================================"
	echo ""
	exit 0
else
    echo "Found curl."
fi

echo "Start downloading auto-snapshotor executable jar from ${DOWNLOAD_URL}"
curl --progress-bar -o "${INSTALL_DIR}${DOWNLOAD_FILE_NAME}" ${DOWNLOAD_URL}

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

echo "Please restart your terminal of reload you bash profile."
