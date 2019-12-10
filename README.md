# Tickets_demo

## To test API via curl use 

### Create request

Instead of %VALUE% insert real values

`curl -H "Accept: application/json" -H "Content-type: application/json" \
-X POST -d '{"number":"%VALUE%", "departureTime": "%VALUES" }' http://localhost:8080/apply`

Example 

`curl -H "Accept: application/json" -H "Content-type: application/json" \
-X POST -d '{"number":92, "departureTime": "2014-01-01T23:28:56.782Z"}' http://localhost:8080/apply`

### Chech request 

Instead of %VALUE% insert real values

`curl -H "Accept: application/json" -H "Content-type: application/json" \
-X POST -d '{"id":%VALUE%}' http://localhost:8080/check`

Example 

`curl -H "Accept: application/json" -H "Content-type: application/json" \
-X POST -d '{"id":1}' http://localhost:8080/check`
