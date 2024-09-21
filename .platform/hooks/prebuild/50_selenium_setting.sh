#!/bin/bash

yum update -y
yum install -y wget unzip java-1.8.0-openjdk-devel libX11 libXext libXrender libXi libXtst libXrandr libgconf2
CHROME_VERSION="127.0.6533.72"
wget https://dl.google.com/linux/chrome/rpm/stable/x86_64/google-chrome-stable-${CHROME_VERSION}-1.x86_64.rpm
yum localinstall -y google-chrome-stable-${CHROME_VERSION}-1.x86_64.rpm
wget https://storage.googleapis.com/chrome-for-testing-public/$CHROME_VERSION/linux64/chromedriver-linux64.zip
unzip chromedriver_linux64.zip
mv chromedriver /usr/local/bin/
chmod +x /usr/local/bin/chromedriver