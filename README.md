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
    implementation 'tv.isshoni:mishima:0.8.15'
}
```

TODO List
---------
- [ ] Add safe shutdown procedure
- [x] Socket Server
  - [x] SSL
- [x] Event
  - Socket server by default publishes `ConnectionEvent`s, this type will keep it that way.
- [ ] Packet
  - Turns server into simple JSON packet protocol consumer, might practice some encapsulation.
  - Still highly in design phase and might hang in permanent limbo. HTTP & event is main focus.
- [ ] HTTP -- in "usable" state currently
  - Hooks in HTTP server management services & annotations to turn into spring-like library.
  - [x] Path annotation
  - [ ] Protocol
    - [x] HTTP/1.1
    - [ ] HTTP/2 -- maybe optional?
    - [ ] HTTP/3 -- maybe optional?
  - [ ] Methods
    - [x] GET
    - [x] POST
    - [x] PUT
    - ~~CONNECT~~
      - Will not implement for now, doesn't seem "related"
    - [x] DELETE
    - [x] HEAD
    - [x] OPTIONS
      - Automated for CORS, but can also be manually done.
    - ~~TRACE~~
      - Will not implement for now, doesn't seem "related"
  - [ ] Websockets -- investigate how these might work, if they work how I suspect they might, allow conversion to Packet handler
  - [x] CORS
    - [ ] Verify that more is not required for proper CORS integration.
  - [ ] Serialization
    - [ ] Responses
      - [x] Basic GSON object serialization
      - [x] Advanced serialization via serializers
      - [x] Special response serialization, i.e. custom headers, response code, etc.
        - [ ] Add parameter annotations that allow for response customization without needing to
          return a response object
  - [ ] Deserialization
    - [x] Body
      - [x] Json
    - [ ] Query Parameter
      - [ ] Special serializers, allow for embedding objects into query params
