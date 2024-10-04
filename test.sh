#!/bin/bash 

gradle cleanTest test  
gradle test jacocoTestReport 

