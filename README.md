# newsapp
This is a  newsapp based on Springboot and Vue.
This repository is backend modules.

Tech stack:

## Front-end:
* Vue
* Weex
  
## Backend:
* SpringBoot
* MyBaits
* zookeeper
* Kafka
* Redis
* Elastic Search
* Jwt
* Crawler

## Deployment
Frontend is deployed using Nginx.
Backend is deployed in 2 machines to form a distributed system.(This app is developed in maven multi-modules. It can be deployed in a cluster.)

## Main Functionality

* Using crawler to get raw articles from csdn.com
* Using Kafka Stream to send message to article module to save processed articles
* Recommand hot article based on the number of view and like.
* Design Behavior Module to record behaviors of user(the duration of reading a passage, the reaction to it(like, dislike or comment))
* Simple Login Module

## Sample
