# Arctor
Android MVP Library

There are many like it, but this one is mine.

Heavily inspired by [Mosby](https://github.com/sockeqwe/mosby), [Nucleus](https://github.com/konmik/nucleus) and [series](http://blog.bradcampbell.nz/rxjava-handling-configuration-changes-with-request-observables/) of posts by Brad Campbell

# Installation
Add maven repository
```grovy
respositories {
    jcenter()
    // ...
    maven { url 'https://dl.bintray.com/alapshin/maven/'}
    // ...
}
```

Add dependencies
```
compile 'com.alapshin.arctor:arctor:x.y.z
// Optional RxJava-based presenter
compile 'com.alapshin.arctor:arctor-rxjava:x.y.z'
// Optional RxJava2-based presenter
compile 'com.alapshin.arctor:arctor-rxjava2:x.y.z'
// Optional annotation proccessor to generate viewstate commands
provided 'com.google.auto.value:auto-value:1.3'
apt 'com.google.auto.value:auto-value:1.3'
compile 'com.alapshin.arctor:arctor-annotation:x.y.z'
apt 'com.alapshin.arctor-processor:x.y.z'
```
