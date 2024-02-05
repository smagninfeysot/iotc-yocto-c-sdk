
#based on https://www.digikey.co.uk/en/maker/projects/intro-to-embedded-linux-part-6-add-custom-application-to-yocto-build/509191cec6c2418d88fd374f93ea5dda
SUMMARY = "Compile and install the basic-sample from the IoT Connect C SDK"
DESCRIPTION = "this recipe pulls the C SDK from the IoT Connect git repos; Sets up the basic-sample as the target to be built using CMake and then installed into the image."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit cmake

DEPENDS += " curl"
DEPENDS += " pkgconfig"
DEPENDS += " openssl"
DEPENDS += " util-linux"
RDEPENDS:${PN} += "systemd"
RDEPENDS:${PN} += "bash"
RDEPENDS:${PN} += "perl"
RDEPENDS:${PN} += "make"
RDEPENDS:${PN} += "ruby"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev"

RDEPENDS:${PN}-staticdev = ""
RDEPENDS:${PN}-dev = ""
RDEPENDS:${PN}-dbg = ""

# Where to keep downloaded source files (in tmp/work/...)
C="${WORKDIR}/git"
S="${C}/iotc-generic-c-sdk"
#S="${C}"

SRCREV_FORMAT="machine_meta"
SRCREV="${AUTOREV}"

# Where to find source files (can be local, GitHub, etc.)

# This points to my branch of my fork of generic c sdk for now. TODO: change back when merged
#SRC_URI = "git://github.com/vputys/iotc-generic-c-sdk.git;\
#protocol=https;\
#branch=testig/vlad;\
#destsuffix=${C};\
#"

SRC_URI = "git://github.com/avnet-iotconnect/iotc-generic-c-sdk.git;\
protocol=https;\
branch=main;\
destsuffix=${C};\
"

SRC_URI += "gitsm://github.com/DaveGamble/cJSON.git;\
protocol=https;\
branch=master;\
destsuffix=${C}/lib/cJSON/;\
"

SRC_URI += "gitsm://github.com/avnet-iotconnect/iotc-c-lib.git;\
protocol=https;\
branch=master;\
destsuffix=${C}/lib/iotc-c-lib/;\
"

SRC_URI += "gitsm://github.com/Azure/azure-iot-sdk-c.git;\
protocol=https;\
branch=main;\
destsuffix=${C}/lib/azure-iot-sdk-c/;\
"

SRC_URI += "gitsm://github.com/eclipse/paho.mqtt.c.git;\
protocol=https;\
branch=master;\
destsuffix=${C}/lib/paho.mqtt.c/;\
"

SRC_URI += "file://0001_CMake_findPackage.patch;\
patchdir=${C};\
"

cmake_do_generate_toolchain_file:append() {
	cat >> ${WORKDIR}/toolchain.cmake <<EOF
$cmake_crosscompiling

set( PC_CURL_LIBRARY_DIRS "${STAGING_LIBDIR}")

#find_package(CURL REQUIRED)
EOF
}

#FILES:${PN} += "/lib/iotc-c-lib/tests/* \
#  /lib/iotc-c-lib/src/* \
#  /lib/iotc-c-lib/include/* \
#  /lib/iotc-c-lib/tools/* \
#  /lib/iotc-generic-c-sdk/* \
#"
FILES:${PN} += "/lib/* \
  /iotc-generic-c-sdk/* \
"
do_populate_sysroot() {
    mkdir -p ${SYSROOT_DESTDIR}/lib
    cp -r ${C}/lib/* ${SYSROOT_DESTDIR}/lib/
    rm -r ${SYSROOT_DESTDIR}/lib/paho.mqtt.c/test/python
    cp -r ${C}/iotc-generic-c-sdk ${SYSROOT_DESTDIR}/
}

# Create /usr/bin in rootfs and copy program to it
do_install() {
#    install -d ${D}${libdir}
#    install -d ${C}/lib/iotc-c-lib ${D}${libdir}/
#    install -d ${D}/lib/iotc-c-lib/tests \
#	${D}/lib/iotc-c-lib/src \
#	${D}/lib/iotc-c-lib/include \
#	${D}/lib/iotc-c-lib/tools

##    mkdir -p ${D}/lib/iotc-c-lib/tests
##    cp -r ${C}/lib/iotc-c-lib/tests/* ${D}/lib/iotc-c-lib/tests/
#    cp -r ${C}/lib/iotc-c-lib/src/* ${D}${libdir}/iotc-c-lib/src/*
##    mkdir -p ${D}/lib/iotc-c-lib/include
##    cp -r ${C}/lib/iotc-c-lib/include/* ${D}/lib/iotc-c-lib/include/
#    cp -r ${C}/lib/iotc-c-lib/tools/* ${D}${libdir}/iotc-c-lib/tools/*

##    mkdir -p ${D}/lib/iotc-generic-c-sdk
##    cp -r ${C}/iotc-generic-c-sdk/* ${D}/lib/iotc-generic-c-sdk/

    mkdir -p ${D}/lib
    cp -r ${C}/lib/* ${D}/lib/
    rm -r ${D}/lib/paho.mqtt.c/test/python
    cp -r ${C}/iotc-generic-c-sdk ${D}/
}
