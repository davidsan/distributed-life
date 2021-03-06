# Conway's Game of Life using Hadoop MR2
[![Build Status](https://travis-ci.org/davidsan/distributed-life.svg)](https://travis-ci.org/davidsan/distributed-life)

Implementation of Conway's Game of Life using Hadoop 2.6.0 MapReduce (MR2)

The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970.

Examples of patterns to be used as input are included in the samples directory.

### Guide

Build using maven :
```
$ mvn install
```
Create the input file (or copy one from the samples directory) into a directory named `depth_0`:
```
$ mkdir depth_0
$ cp samples/Oscillators/Blinker/file1 depth_0/part-r-00000
```
The input file should be named `part-r-00000` for convenience with the last two commands of this document.

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

or inspect the result visually :

```
$ java -cp target/conway-0.0.1-SNAPSHOT.jar ConwayView
depth_0
................................................................................
.XXX............................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................

depth_1
..X.............................................................................
..X.............................................................................
..X.............................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................

depth_2
................................................................................
.XXX............................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................

depth_3
..X.............................................................................
..X.............................................................................
..X.............................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................

depth_4
................................................................................
.XXX............................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................

depth_5
..X.............................................................................
..X.............................................................................
..X.............................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................


```
