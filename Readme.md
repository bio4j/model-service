## HTTP-service for exploring the Bio4j domain model

### Usage

First run the server:

```bash
sbt run
```

then you can try somewhere else:

```bash
curl http://localhost:8080/schemas/go
curl http://localhost:8080/schemas/go/properties
curl http://localhost:8080/schemas/go/vertexTypes/GOTerm/properties
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


### Request format

`http://localhost:8080` returns you the top-level JSON object. 
Then you can traverse it by appending identifiers to the path.

* if `foo` is a JSON object, `foo/bar/` means accessing it's field `bar`
* if `foo` is a JSON array, `foo/bar/` means selecting from it an object with `"label": "bar"`


### JSON schema

* top-level object:

    ```json
    {
        "schemas": [<module>]
    }
    ```

* `<module>`:

    ```json
    {
        "label": "...",
        "properties": [<property>],
        "dependencies": [<module.label>],
        "vertexTypes": [<vertexType>],
        "edgeTypes": [<edgeType>]
    }
    ```

* `<property>`:

    ```json
    {
        "label": "...",
        "type": "..."
    }
    ```

* `<vertexType>`:

    ```json
    {
        "label": "...",
        "properties": [<property.label>]
    }
    ```

* `<edgeType>`:

    ```json
    {
        "label": "...",
        "properties": [<property.label>],
        "source": {
            "type": <vertexType.label>,
            "arity": <arity>
        },
        "target": {
            "type": <vertexType.label>,
            "arity": <arity>
        }
    }
    ```

* `<arity>`: `"one"` or `"many"`
