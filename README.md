# akka-vs-nodejs

Simple implementation of http server responding on one resource '/factorial?param=x'

One implementation is in node.js and another one in scala/akka and using socko web server

This is used for comparison of code, productivity and performance. Please note this was done for my own interest and for a reliable (and fair) comparison of performance, much more thorough study would have to be done including different use cases.

## Prerequisites

- *nix
- make
- docker
- docker-compose
- Internet

## Setup

Run:

```bash
make build ping
```

Should output:

```
NodeJS 9.332621544394418e+157 165580141
Akka 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000 165580141
```

## Tests

To run both environments:
```bash
make up
```

To run akka environment:
```bash
make up-akka
```

To run node environment:
```bash
make up-node
```

To close running environments:
```bash
make down
```

### Test mostly concurrency and networking
Run 20000 concurrent requests for factorial(1):
(**needs both env` on**)

```bash
make test-low-compute
```

Example output:

```
NodeJS
Completed 2000 requests
Completed 4000 requests
Completed 6000 requests
Completed 8000 requests
Completed 10000 requests
Completed 12000 requests
Completed 14000 requests
Completed 16000 requests
Completed 18000 requests
Completed 20000 requests
Finished 20000 requests
Requests per second:    8774.77 [#/sec] (mean)




Akka
Completed 2000 requests
Completed 4000 requests
Completed 6000 requests
Completed 8000 requests
Completed 10000 requests
Completed 12000 requests
Completed 14000 requests
Completed 16000 requests
Completed 18000 requests
Completed 20000 requests
Finished 20000 requests
Requests per second:    8360.75 [#/sec] (mean)
```

### Test high cpu usage mostly
Run 10 concurrent requests for fibonacci(40):
(**needs both env` on**)

```bash
make test-high-compute
```

Example output:

```
NodeJS
Requests per second:    1.70 [#/sec] (mean)




Akka
Requests per second:    0.17 [#/sec] (mean)
```

### Test long low cpu high RPS
Run 1000000 requests (with 20000 concurrency) for factorial(1):

#### Node env`
(**needs only node env` on**)

```bash
make test-long-node
```

Example output:

```
ab -r -s 10000 -n 1000000 -c 20000 http://127.0.0.1:8080/factorial?param=1
This is ApacheBench, Version 2.3 <$Revision: 1706008 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 127.0.0.1 (be patient)
Completed 100000 requests
Completed 200000 requests
Completed 300000 requests
Completed 400000 requests
Completed 500000 requests
Completed 600000 requests
Completed 700000 requests
Completed 800000 requests
Completed 900000 requests
Completed 1000000 requests
Finished 1000000 requests


Server Software:
Server Hostname:        127.0.0.1
Server Port:            8080

Document Path:          /factorial?param=1
Document Length:        0 bytes

Concurrency Level:      20000
Time taken for tests:   94.659 seconds
Complete requests:      1000000
Failed requests:        23791
   (Connect: 0, Receive: 0, Length: 23791, Exceptions: 0)
Total transferred:      2760916 bytes
HTML transferred:       23801 bytes
Requests per second:    10564.24 [#/sec] (mean)
Time per request:       1893.179 [ms] (mean)
Time per request:       0.095 [ms] (mean, across all concurrent requests)
Transfer rate:          28.48 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0 1095 1178.8    790   15906
Processing:   157  775 168.7    773   13526
Waiting:        0   23 177.2      0   13359
Total:        721 1870 1216.4   1584   17130

Percentage of the requests served within a certain time (ms)
  50%   1584
  66%   1636
  75%   1675
  80%   1706
  90%   2550
  95%   4386
  98%   4897
  99%   8599
 100%  17130 (longest request)
 ```

#### Akka env`
(**needs only akka env` on**)

```bash
make test-long-akka
```

Example output:

```
ab -r -s 10000 -n 1000000 -c 20000 http://127.0.0.1:8081/factorial?param=1
This is ApacheBench, Version 2.3 <$Revision: 1706008 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 127.0.0.1 (be patient)
Completed 100000 requests
Completed 200000 requests
Completed 300000 requests
Completed 400000 requests
Completed 500000 requests
Completed 600000 requests
Completed 700000 requests
Completed 800000 requests
Completed 900000 requests
Completed 1000000 requests
Finished 1000000 requests


Server Software:
Server Hostname:        127.0.0.1
Server Port:            8081

Document Path:          /factorial?param=1
Document Length:        0 bytes

Concurrency Level:      20000
Time taken for tests:   870.208 seconds
Complete requests:      1000000
Failed requests:        905128
   (Connect: 0, Receive: 35959, Length: 833207, Exceptions: 35962)
Non-2xx responses:      437974
Total transferred:      204788544 bytes
HTML transferred:       76164855 bytes
Requests per second:    1149.15 [#/sec] (mean)
Time per request:       17404.151 [ms] (mean)
Time per request:       0.870 [ms] (mean, across all concurrent requests)
Transfer rate:          229.82 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0  225 1344.4      1   31098
Processing:    14 15906 36461.7   1228  272648
Waiting:        0 5274 12789.0   1064  166087
Total:         16 16131 36599.3   1356  272648

Percentage of the requests served within a certain time (ms)
  50%   1356
  66%   3282
  75%   7245
  80%  13956
  90%  63350
  95%  117771
  98%  127346
  99%  133774
 100%  272648 (longest request)
 ```
