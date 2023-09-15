#!/bin/bash

docker build -t acra_image .

rm AcraTLSCert.crt
rm AcraTLSKey.key