#!/bin/sh
cd $1
echo 'Processing String len 20'
./dart /Users/chani/dart/dart_bench/InverseBenchmarkRope.dart 20 default > results.txt
./dart /Users/chani/dart/dart_bench/InverseBenchmarkRope.dart 20 rope >> results.txt
echo 'Processing String len 100'
./dart /Users/chani/dart/dart_bench/InverseBenchmarkRope.dart 100 default >> results.txt
./dart /Users/chani/dart/dart_bench/InverseBenchmarkRope.dart 100 rope >> results.txt


cat results.txt
