# Budget Portfolio Manager

### What is this?
An open source budget and account tracking solution. Its currently in Alpha but as features are added it will continue to get expanded. Feature requests and issues can be reported in GitHub.

### Is it supported?
This software is currently in Alpha, I wouldnt reccomend running it in any form of prod like environment. If you have trouble you can submit an issue here on GitHub and I'll get back to you as soon as possible.

### Why are you building this?
I have tried a plethora of selfhosted budgeting and account tracking tools and have yet to be satisfied with any of them.


## How do I run it?
Well, if you want to you can compile from source with a simple:
> mvn package
Or you can download the jar file. Just make sure you have a config.properties in the same directory as the jar file when you execute the jar
> cat config.properties
> databaseUrl=192.168.1.x
> databasePort=3306
> databaseName=budget
> databaseUsername=budget
> databasePassword=password
>
> java -jar budgetportfoliomanager-0.0.1-alpha.jar