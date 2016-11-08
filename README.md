# atlanta-scala-linkedin-microservice

This project demonstrates using [Akka HTTP](http://doc.akka.io/docs/akka-stream-and-http-experimental/current/scala.html), the LinkedIn API, the Neo4J Graph Database, and Scala to build a microservice. The microservice will pull the connection graph for each user in the room from LinkedIn and show how we are all connected to one another.

## Running the services

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

### Build an executable JAR

```
$ sbt
> assembly
```

## cloud runtime
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

`cf set-env atlanta-scala-microservice neo4j.host localhost`
