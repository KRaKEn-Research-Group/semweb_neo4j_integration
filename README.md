# semweb_neo4j_integration
TODO

## Installation

TODO
```
TODO
```

## Usage

Sample query presented below:

```neo4j
WITH 'jdbc:virtuoso://localhost:1111/UID=dba/PWD=wshop' AS c, "SELECT * FROM demo.dbpedia_wikidata.query" AS q
CALL apoc.load.jdbc(c,q) YIELD row
MERGE (n:Person {uri:row.actor}) ON CREATE  SET n.name = row.name, n.sameAs = row.same
MERGE (u:Partner {uri:row.unmarriedPartner, name:row.unmarriedPartnerName})
MERGE (s:Spouse {uri:row.spouse, name:row.spouseName})
MERGE (a:Award {uri:row.award, name:row.awardName})
MERGE (n)-[:UNMARRIED_PARTNER {uri:row.partnerProp,startDate:COALESCE(row.startDate,'unknown'),endDate:COALESCE(row.endDate,'unknown')}]->(u)
MERGE (n)-[p:AWARD_RECEIVED {uri:row.awardProp}]->(a) ON CREATE SET p.year = row.year
MERGE (n)-[:SPOUSE {uri:row.spouseProp}]->(s)
RETURN *
```

### APOC JSONPARAMS

```
CALL apoc.load.jsonParams(
    'http://host.docker.internal:8080',
    {method: 'POST', `Content-Type`: "text/plain"},
    "SELECT * FROM demo.dbpedia_wikidata.query7"
) YIELD value
MERGE (n:Person {uri:value.actor}) ON CREATE  SET n.name = value.name, n.sameAs = value.same
MERGE (u:Partner {uri:value.unmarriedPartner, name:value.unmarriedPartnerName})
MERGE (s:Spouse {uri:value.spouse, name:value.spouseName})
MERGE (a:Award {uri:value.award, name:value.awardName})
MERGE (n)-[:UNMARRIED_PARTNER {uri:value.partnerProp,startDate:COALESCE(value.startDate,'unknown'),endDate:COALESCE(value.endDate,'unknown')}]->(u)
MERGE (n)-[p:AWARD_RECEIVED {uri:value.awardProp}]->(a) ON CREATE SET p.year = value.year
MERGE (n)-[:SPOUSE {uri:value.spouseProp}]->(s)
RETURN *
```

## Process

### 1. Run docker containers
Get into the directory of the project and run the following command:
```
docker-compose up
```

### 2. Add the SQL views
TODO: Check if some more permissions need to be added before that step.

In order to add some exemplery views, login to `localhost:8890` **Conductor** panel with the data defined in `docker-compose.yaml` (default: `dba/wshop`). Go to **Database > Interactive SQL** and create a view there, e.g.:

```
CREATE VIEW demo.actors.test
AS
    (
        SPARQL
		PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
		PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
		PREFIX dct:	<http://purl.org/dc/terms/>

		SELECT *
		WHERE
		{
			SERVICE <http://dbpedia.org/sparql/> {
				?concept rdf:type skos:Concept ;
				rdfs:label  "Best Actor Academy Award winners"@en .
				?actor dct:subject ?concept.
			}
		}
	);
                           
GRANT SELECT ON demo.actors.test TO dba;
```

### 3. Transfer the data into Neo4j
Go to `localhost:7474`, connect to the database (with the environment variables set by default, empty login and password input fields should work. There you can fetch the data from Virtuoso and process it, e.g.:

```
CALL apoc.load.jsonParams(
    'http://host.docker.internal:8080',
    {method: 'POST', `Content-Type`: "text/plain"},
    "SELECT * FROM demo.actors.test"
) YIELD value
MERGE (n:Person {uri:value.actor})
MERGE (c:Concept {uri:value.concept})
MERGE (n)-[:AWARD_RECEIVED]->(c)
RETURN *
```

### 4. Display the data in Neo4j
Data should be now saved in the Neo4j database. In order to display all data stored in the database, use the following Cypher query:

```
MATCH (n) RETURN n
```