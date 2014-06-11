## HTTP-service for dealing with the Bio4j domain model

### Usage

First run the server:

```bash
sbt run
```

then you will see the port on which it is running and you can try somewhere else:

```bash
curl http://localhost:<port number>/schema/go

curl http://localhost:<port number>/schema/go/dependencies
curl http://localhost:<port number>/schema/go/propertyTypes
curl http://localhost:<port number>/schema/go/vertexTypes
curl http://localhost:<port number>/schema/go/edgeTypes
```

or just open one of those links in a browser.
