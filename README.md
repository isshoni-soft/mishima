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
  - [ ] Abstract Protocol System
    - [ ] Custom protocols via Protocol annotation..?
  - [x] HTTP Protocol
    - [x] HTTP/1.1
    - [ ] HTTP/2
    - [ ] HTTP/3
- [ ] HTTP Methods
  - [x] GET
  - [x] POST
  - [ ] PUT
  - [ ] CONNECT
  - [ ] DELETE
  - [ ] HEAD
  - [x] OPTIONS
    - [x] Automated
  - [ ] TRACE
- [x] HTTPS functionality
- [x] CORS..?
  - [ ] Verify that more is not required for proper CORS integration.
- [x] Request body serialization
