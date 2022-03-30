### Badges:
[![Actions Status](https://github.com/silentlyexisting/java-project-lvl5/workflows/hexlet-check/badge.svg)](https://github.com/silentlyexisting/java-project-lvl5/actions)
![example workflow](https://github.com/silentlyexisting/java-project-lvl5/actions/workflows/java-ci.yml/badge.svg)
<a href="https://codeclimate.com/github/silentlyexisting/java-project-lvl5/maintainability"><img src="https://api.codeclimate.com/v1/badges/c14850b34d5cf8378bdd/maintainability" /></a>
<a href="https://codeclimate.com/github/silentlyexisting/java-project-lvl5/test_coverage"><img src="https://api.codeclimate.com/v1/badges/c14850b34d5cf8378bdd/test_coverage" /></a>

## Task Manager
###Project demo: [Click here](https://fierce-tor-82525.herokuapp.com)
###<b>API: [Click here](https://fierce-tor-82525.herokuapp.com/swagger-ui.html) </b>

### To run project in development environment:
```shell
git clone https://github.com/silentlyexisting/java-project-lvl5
cd java-project-lvl5
make start-dev
http://localhost:7000/
```
### To run project in production environment:
```shell
git clone https://github.com/silentlyexisting/java-project-lvl5
cd java-project-lvl5
make start-prod
```

### Configuration

###application.properties
`server.port` - where `${PORT:7000}`default port
`spring.profiles.active` -  where`${APP_ENV}` application environment 

###application-dev.properties
`spring.datasource.url` - development database url

`spring.datasource.username` - database username

`spring.datasource.password` - database password

`spring.h2.console.path` - database h2 console path

###application-prod.properties
`spring.datasource.url` - where `${JDBC_DATABASE_URL}` production Heroku Postgres database url

`springdoc.swagger-ui.path` - swagger api path
