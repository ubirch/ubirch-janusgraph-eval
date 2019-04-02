# flowershop #

## Build & Run ##

```sh
$ cd flowershop
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

adding swagger UI :
```
docker pull swaggerapi/swagger-ui
docker run -p 80:8080 -e SWAGGER_JSON=http://0.0.0.0:8080/api-docs/swagger.json  swaggerapi/swagger-ui
```
