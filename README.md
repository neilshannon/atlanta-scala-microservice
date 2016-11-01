# atlanta-scala-linkedin-microservice

This project demonstrates using [Akka HTTP](http://doc.akka.io/docs/akka-stream-and-http-experimental/current/scala.html), the LinkedIn API, the Neo4J Graph Database, and Scala to build a microservice. The microservice will pull the connection graph for each user in the room from LinkedIn and show how we are all connected to one another.

## to com.ntsdev.run

Start services with sbt:

```
$ sbt
> ~re-start
```

### Testing

Execute tests using `test` command:

```
$ sbt
> test
```

## cloud runtime
#### environment variables
| variable name  | variable value |
|----------------|----------------|
| neo4j.host     | host for neo4j server |
| neo4j.webPort  | REST API port for neo4j |
| neo4j.boltPort | binary Bolt port for neo4j |
| neo4j.username | neo4j username |
| neo4j.password | neo4j password |
| http.host      | http host for microservice |
| http.port      | http port for microservice |
