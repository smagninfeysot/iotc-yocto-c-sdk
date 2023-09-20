SUMMARY = "Autostart"
LICENSE = "CLOSED"

FILEEXTRAPATHS:prepend = "${THISDIR}/files:"

SRC_URI += "file://basic-sample.service"

do_install () {

	install -d ${D}/${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/basic-sample.service ${D}/${systemd_system_unitdir}
}

FILES:${PN} += " ${systemd_system_unitdir}/system/basic-sample.service"

inherit systemd
SYSTEMD_PACKAGES = "${PN}"
INITSCRIPT_PACKAGES = "${PN}"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "basic-sample.service"

