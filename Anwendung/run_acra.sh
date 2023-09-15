#!/bin/bash

export ACRA_MASTER_KEY=$(cat master.key | base64)

acra-server --config_file=/config.yml --encryptor_config_file=/encryptor_config.yml