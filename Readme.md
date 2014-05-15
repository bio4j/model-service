## HTTP-service for dealing with the Bio4j domain model

### Usage

First run the server:

```bash
sbt run
```

then somewhere else:

```bash
curl http://localhost:8080/schema/com.bio4j.model.enzymedb

curl http://localhost:8080/schema/com.bio4j.model.enzymedb/dependencies
curl http://localhost:8080/schema/com.bio4j.model.enzymedb/nodeTypes
curl http://localhost:8080/schema/com.bio4j.model.enzymedb/relationshipTypes
curl http://localhost:8080/schema/com.bio4j.model.enzymedb/propertyTypes
```

or just open those links in a browser.
