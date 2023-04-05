Mishima Webserver
=================
Using Winry, Mishima is a spring-like java webserver host.

Usage
-----
I have a maven repository where I publish builds, here is how you would import it as a dependency with gradle:
```groovy
repositories {
    maven {
        url = 'https://repo.isshoni.institute'
    }
}

dependencies {
    implementation 'tv.isshoni:mishima:0.5.0'
}
```

TODO List
---------
- [x] Socket Server
  - [x] SSL
- [x] Event
  - Socket server by default publishes ConnectionEvents, this type will keep it that way.
- [ ] Packet
  - Turns server into simple JSON packet protocol consumer, might practice some encapsulation.
  - Still highly in design phase and might hang in permanent limbo. HTTP & event is main focus.
- [ ] HTTP -- in "usable" state currently
  - Hooks in HTTP server management services & annotations to turn into spring-like library.
  - [ ] Protocol
    - [x] HTTP/1.1
    - [ ] HTTP/2
    - [ ] HTTP/3
  - [ ] Methods
    - [x] GET
    - [x] POST
    - [x] PUT
    - [ ] CONNECT
    - [ ] DELETE
    - [ ] HEAD
      - This will be fully automated, only works for GET requests.
    - [x] OPTIONS
      - Automated for CORS, but can also be manually done.
    - [ ] TRACE
  - [x] CORS
    - [ ] Verify that more is not required for proper CORS integration.
  - [x] Request body serialization
