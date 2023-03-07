#!/bin/sh

# step 1
npm run generate

# step 2
NODE_OPTIONS=--openssl-legacy-provider HOST=0.0.0.0 npm run start
