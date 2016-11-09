# atlanta-scala-linkedin-microservice

This project demonstrates using [Akka HTTP](http://doc.akka.io/docs/akka-stream-and-http-experimental/current/scala.html), the [Google People API](https://developers.google.com/people/), the [Neo4J Graph Database](https://neo4j.org), and [Scala](https://scala-lang.org) to build a microservice. The microservice will pull the connection graph for each user in the room from Google+ and show how all the people who are logged in connected to one another.

### running the services

start local services with sbt:

```
$ sbt
> ~re-start
```

### testing

Execute tests using `test` command:

```
$ sbt
> test
```

### build deployment assembly

```
$ sbt
> clean universal:packageBin
```

### deploy to cloud foundry

```
$ cf push
```

### cloud runtime configuration
#### environment variables
| variable name  | variable value | default |
|----------------|----------------|---------|
| neo4j-host     | host for neo4j server | 0.0.0.0 |
| neo4j-webPort  | REST API port for neo4j | 7474 |
| neo4j-boltPort | binary Bolt port for neo4j | 7687 |
| neo4j-username | neo4j username | neo4j |
| neo4j-password | neo4j password | blank |
| http-host      | http host for microservice | 0.0.0.0 |
| http-port      | http port for microservice | 9000 |

#### setting environment variables in CloudFoundry

`cf set-env atlanta-scala-microservice neo4j-host localhost`
