SUMMARY = "Autostart"
LICENSE = "CLOSED"

FILEEXTRAPATHS:prepend = "${THISDIR}/files:"

SRC_URI += "file://basic-sample.service"
SRC_URI += "file://iotc-manager.sh"
SRC_URI += "file://iotc-c-sdk_last-image-str.log"
RDEPENDS:${PN} =+ " bash"

do_install () {

	install -d ${D}/${systemd_system_unitdir}
	install -d ${D}${bindir}
	install -d ${D}/home
	install -m 0644 ${WORKDIR}/basic-sample.service ${D}/${systemd_system_unitdir}
    install -m 0777 ${WORKDIR}/iotc-manager.sh ${D}${bindir}
	install -m 0644 ${WORKDIR}/iotc-c-sdk_last-image-str.log ${D}/home/iotc-c-sdk_last-image-str.log
	#chmod -R 775 ${D}${bindir}
}

FILES:${PN} += " ${systemd_system_unitdir}/system/basic-sample.service \
${bindir}/iotc-manager.sh \
/home/iotc-c-sdk_last-image-str.log"

inherit systemd
SYSTEMD_PACKAGES = "${PN}"
INITSCRIPT_PACKAGES = "${PN}"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "basic-sample.service"

