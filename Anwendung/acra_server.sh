#!/bin/bash

docker run -it --rm --name acra_server --network=mc_questions_nw -p 127.0.0.1:9393:9393 acra_image ./run_acra.sh