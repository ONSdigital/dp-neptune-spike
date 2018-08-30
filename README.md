# Neptune Spike
Useful notes on getting started including links to tutorials.

## General Info
### Gremlin getting started
Beginner tutorials on using the gremlin console and basic query syntax:

> http://tinkerpop.apache.org/docs/3.3.3/tutorials/getting-started/ 

### Set up gremlin console to use remote server
Example of how to set up the gremlin console to point to a remote server:

> https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-console.html

### Java Gremlin example
Getting started example of using the java library
> https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-java.html

Some AWS specific Gremlin implementation notes:
> https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-differences.html


## Running dp-neptune-spike
Assuming a Neptune instance is running and an EC2 instance is all set up.

### Open an ssh tunnel to the EC2 box:
Curently the EC2 is not exposed publically so to connect to Neptune you need to open an ssh tunnel to forward requests
 to localhost:8182 to EC2 - you will need to keep this open. 
```bash
ssh -i ~/.ssh/ons-web-development.pem -L 8182:<AWS_NEPTUNE_ENDPOINT>:8182 ec2-user@<AWS_EC2_PORT>
```

### Install Gremlin console on the EC2 instance
Gremlin console is a terminal tool which allows you query the database. Follow the AWS steps to set it up. (Including
 the instuctions on how to connect to a remote DB):
 
> https://docs.aws.amazon.com/neptune/latest/userguide/access-graph-gremlin-console.html

### Install Gremlin server on the EC2 instance
From your home directory on the EC2 box run:
```
wget https://archive.apache.org/dist/tinkerpop/3.3.2/apache-tinkerpop-gremlin-server-3.3.2-bin.zip
```
```
unzip apache-tinkerpop-gremlin-server-3.3.2-bin.zip
```
```bash
cd apache-tinkerpop-gremlin-server-3.3.2
```
Start the server and point it at your AWS Neptune instance.
```bash
./bin/gremlin-server.sh conf/gremlin-server.yaml
```
From your machine if you curl `localhost:8182` you should get a response similar to:
````json
{
  "requestId": "38b2c520-d97a-f5ff-414e-7d9a5c6de398",
  "code": "MissingParameterException",
  "detailedMessage": "no gremlin script supplied"
}
````
You are successfull able to hit the Gremlin server running on the EC2 instance (whoop!)

### Run the app
The app is as basic as it comes and simply takes input from the console and uses the gremlin client to send queries 
to the gremlin server. Simply type in your query and hit enter (or enter `exit` to quit).

From the project root

Build it
```bash
mvn clean package -DskipTests=true 
```

Run it
```bash
java -jar target/dp-neptune-spike-1.0-SNAPSHOT.jar -query
```

Execute a query (see the gremlin tutorials for syntax)
```
grem-query> g.addV("person").property("name", "Dave").next()
grem-query> g.V().valueMap(true)
> {id=50b2c521-8a91-457f-4a82-eb4d29c6ef67, label=person, name=[Dave]}
```
The above example runs 2 queries, the first creates a new vertex labelled "person" with a name property "Dave". The 
second queries for all vertices and returns them with all their properties and labels - which in this case returns 
the vertex created in the previous query.
