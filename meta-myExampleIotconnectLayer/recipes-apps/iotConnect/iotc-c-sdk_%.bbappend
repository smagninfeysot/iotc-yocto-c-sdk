FILESEXTRAPATHS:prepend := "${THISDIR}:"

RDEPENDS:${PN} += " iotc-c-sdk-autostart"

SRC_URI += "file://app_config.h;\
subdir=${S}/config;\
"
