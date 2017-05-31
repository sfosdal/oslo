#!/bin/bash

set -e

cd "$(dirname "$0")/.."

sbt clean coverage test scalastyle coverageReport
