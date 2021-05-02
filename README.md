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
