# newsapp
This is a  newsapp based on Springboot and Vue.
This repository is backend modules.

This app is deployed at http://trojanvps.site:3000 (Recommand using mobile phone)

Feel free to try it!

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
Backend is deployed in 2 machines to form a distributed system.

## Main Functionality

* Using crawler to get raw articles from csdn.com
* Using Kafka Stream to send message to article module to save processed articles
* Recommand hot article based on the number of view and like.
* Design Behavior Module to record behaviors of user(the duration of reading a passage, the reaction to it(like, dislike or comment))
* Simple Login Module

## Developing Features

* Register User
* Loading Media and discussion
* User Profile ...

## Snapshots

* Login in page:

![](https://i.ibb.co/zn5r09c/IMG-1566.png)

* home page

![](https://i.ibb.co/PzxkwLw/IMG-1567.png)

* reading page

<a href="https://ibb.co/t3yMzLt"><img src="https://i.ibb.co/TrzB1WS/RPReplay-Final1595193137.gif" alt="RPReplay-Final1595193137" border="0"></a>

* asscociate word searching

![](https://i.ibb.co/HV6rDCx/IMG-1569.png)

