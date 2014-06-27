## HTTP-service for dealing with the Bio4j domain model

### Usage

First run the server:

```bash
sbt run
```

then you can try somewhere else:

```bash
curl http://localhost:8080/schemas/go
curl http://localhost:8080/schemas/go/dependencies
curl http://localhost:8080/schemas/go/properties
curl http://localhost:8080/schemas/go/vertexTypes/GOTerm/properties
curl http://localhost:8080/schemas/go/edgeTypes
```

or just open one of those links in a browser.

Besides GO module there are some others:

- [x] `enzymedb`
- [x] `go`
- [x] `ncbiTaxonomy`
- [x] `refseq`
- [ ] `uniprot` (not finished)
- [x] `uniprot_enzymedb`
- [x] `uniprot_go`
