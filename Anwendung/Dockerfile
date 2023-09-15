FROM ubuntu:20.04

ADD AcraTLSKey.key /certificates/
ADD AcraTLSCert.crt /certificates/
ADD config.yml /
ADD encryptor_config.yml /
ADD run_acra.sh /

RUN apt update
RUN apt install -y wget gnupg
RUN wget -qO - https://pkgs.cossacklabs.com/gpg | apt-key add -
RUN echo deb https://pkgs.cossacklabs.com/stable/ubuntu focal main > /etc/apt/sources.list.d/cossacklabs.list
RUN apt update
RUN apt install -y acra

RUN acra-keymaker --keystore=v1 --generate_master_key=master.key
RUN export ACRA_MASTER_KEY=$(cat master.key | base64) && acra-keymaker --client_id=mc_questions --keystore=v1 --generate_acrawriter_keys --generate_symmetric_storage_key --generate_hmac_key --generate_log_key