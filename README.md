# Budget Portfolio Manager

### What is this?
An open source budget and account tracking solution. Its currently in Alpha but as features are added it will continue to get expanded. Feature requests and issues can be reported in GitHub.

### Is it supported?
This software is currently in Alpha, I wouldnt reccomend running it in any form of prod like environment. If you have trouble you can submit an issue here on GitHub and I'll get back to you as soon as possible.

### Why are you building this?
I have tried a plethora of selfhosted budgeting and account tracking tools and have yet to be satisfied with any of them.


## How do I run it?

### Setting up the database
It takes a few commands to setup the MySQL database go ahead and login and run the following:
1:
> CREATE DATABASE budget;
2:
> create table accounts ( id int NOT NULL AUTO_INCREMENT, name varchar(300) NOT NULL, bank varchar(300) NOT NULL, balance double NOT NULL, user int NOT NULL, type varchar(60));
3:
> create table bills ( id int NOT NULL AUTO_INCREMENT, name varchar(300) NOT NULL, amount double NOT NULL, date date NOT NULL, user int NOT NULL, transaction int NOT NULL);
4:
> create table categories ( id int NOT NULL AUTO_INCREMENT, name varchar(300) NOT NULL);
5:
> create table transactions ( id int NOT NULL AUTO_INCREMENT, name varchar(300) NOT NULL, amount double NOT NULL, category int, user int NOT NULL, date date NOT NULL, account int NOT NULL);
6:
> create table users ( id int NOT NULL AUTO_INCREMENT, username varchar(300) NOT NULL, firstname varchar(300) NOT NULL, lastname varchar(300) NOT NULL, password varchar(300) NOT NULL, lastlogin date NOT NULL, logincount int NOT NULL, accountcreated date NOT NULL);

### Running it
Well, if you want to you can compile from source with a simple:
> mvn package

Or you can download the jar file from the [releases](https://github.com/jw6jw6jw6/budget-portfolio-manager/releases/) page. Just make sure you have a config.properties and have configured the path to your MySQL database in the same directory as the jar file when you execute the jar

> java -jar budgetportfoliomanager-0.0.1-alpha.jar