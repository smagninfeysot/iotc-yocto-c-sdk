#!/bin/bash

sudo chown user:user /src/ -R
cd /src/
ssh-keyscan -p 9418 -t rsa msc-git02.msc-ge.com >> ~/.ssh/known_hosts
git clone ssh://gitolite@msc-git02.msc-ge.com:9418/msc_ol99/msc-ldk
cd msc-ldk/
git checkout v1.11.0
pip3 install sphinx
./setup.py --bsp=01047
cp -r /src/yocto-layers/meta-iotconnect /src/msc-ldk/sources/
cp -r /src/yocto-layers/meta-myExampleIotconnectLayer /src/msc-ldk/sources/
source sources/yocto.git/oe-init-build-env build/01047
bitbake-layers add-layer ../../sources/meta-iotconnect/
bitbake-layers add-layer ../../sources/meta-myExampleIotconnectLayer/
echo -e '\nIMAGE_INSTALL += " iotc-c-sdk"' >> conf/local.conf
bitbake core-image-minimal
