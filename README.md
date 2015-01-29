# Conway's Game of Life using Hadoop MR2
[![Build Status](https://magnum.travis-ci.com/davidsan/distributed-life.svg?token=gNHewQpaNm3oE2H2fyKW&branch=master)](https://magnum.travis-ci.com/davidsan/distributed-life)

Implementation of Conway's Game of Life using Hadoop 2.6.0 MapReduce (MR2)

The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970.

Examples of patterns to be used as input are included in the samples directory.

### Guide

Build using maven :
```
$ mvn install
```
Create the input file (or copy one from the samples directory) :
```
$ cp samples/Oscillators/Blinker/file1 depth_0/part-r-00000
```
Launch the jobs :
```
$ hadoop jar target/conway-0.0.1-SNAPSHOT.jar Conway 5
```
Inspect the result :
```
$ tail -n +1 depth_*/part-r-*

==> depth_0/part-r-00000 <==
1,1
1,2
1,3

==> depth_1/part-r-00000 <==
0,2
1,2
2,2

==> depth_2/part-r-00000 <==
1,1
1,2
1,3

==> depth_3/part-r-00000 <==
0,2
1,2
2,2

==> depth_4/part-r-00000 <==
1,1
1,2
1,3

==> depth_5/part-r-00000 <==
0,2
1,2
2,2
```
