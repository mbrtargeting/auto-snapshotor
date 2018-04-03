#!/bin/bash

echo "             _                                        _           _"
echo "  __ _ _   _| |_ ___        ___ _ __   __ _ _ __  ___| |__   ___ | |_ ___  _ __"
echo " / _\` | | | | __/ _ \ _____/ __| '_ \ / _\` | '_ \/ __| '_ \ / _ \| __/ _ \| '__|"
echo "| (_| | |_| | || (_) |_____\__ \ | | | (_| | |_) \__ \ | | | (_) | || (_) | |"
echo " \__,_|\__,_|\__\___/      |___/_| |_|\__,_| .__/|___/_| |_|\___/ \__\___/|_|"
echo "                                           |_|"

set -eu

#TODO: Update download url for executable jar
DOWNLOAD_URL="https://github.com/mbrtargeting/auto-snapshotor/releases/download/v0.1-alpha/auto-snapshotor"
DOWNLOAD_FILE_NAME="as"
PLATFORM=$(uname)
INSTALL_DIR="${HOME}/.auto-snapshotor/"

#profile variables
as_bash_profile="${HOME}/.bash_profile"
as_profile="${HOME}/.profile"
as_bashrc="${HOME}/.bashrc"
as_zshrc="${HOME}/.zshrc"

init_snippet=$( cat << EOF
#THIS IS NEEDED FOR AUTO_SNAPSHOTOR TO WORK!!!
export AS_DIR="${INSTALL_DIR}"
[[ -d "${INSTALL_DIR}${DOWNLOAD_FILE_NAME}" ]] && PATH=$PATH:$INSTALL_DIR
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

echo "Making installation directory..."
mkdir -p ${INSTALL_DIR}
cp target/auto-snapshotor "${INSTALL_DIR}."


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
