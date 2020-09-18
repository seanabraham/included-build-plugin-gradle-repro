## Included Plugin Repro
This is a simple repro of a problem with a problem with plugin application (specifically in this case when the plugin is defined in an included build) and configure on demand resulting in some odd behavior.

In particular, it looks like if the same plugin is (validly) applied to multiple projects and there are dependencies between these projects, a failure can result depending on what tasks are requested.


## Repro

The projects build find independently:

```
❯ ./gradlew :projects:foo:classes

Configuration on demand is an incubating feature.

> Configure project :projects:foo
Hello, world! Applying to foo

BUILD SUCCESSFUL in 625ms
```

```
❯ ./gradlew :projects:foo:bar:classes
Configuration on demand is an incubating feature.

> Configure project :projects:foo:bar
Hello, world! Applying to bar

BUILD SUCCESSFUL in 619ms
```

```
❯ ./gradlew :projects:baz:classes
Configuration on demand is an incubating feature.

> Configure project :projects:foo
Hello, world! Applying to foo

BUILD SUCCESSFUL in 635ms
```

```
❯ ./gradlew :projects:foo:classes :projects:foo:bar:classes
Configuration on demand is an incubating feature.

> Configure project :projects:foo
Hello, world! Applying to foo

> Configure project :projects:foo:bar
Hello, world! Applying to bar

BUILD SUCCESSFUL in 634ms
```

```
❯ ./gradlew :projects:baz:classes :projects:foo:classes
Configuration on demand is an incubating feature.

> Configure project :projects:foo
Hello, world! Applying to foo

BUILD SUCCESSFUL in 679ms
```

## Bug

However, when building :projects:baz (which depends on :projects:foo) and :projects:foo:bar with configurationondemand enabled, the following confusing failure results:

```
❯ ./gradlew :projects:baz:classes :projects:foo:bar:classes
Configuration on demand is an incubating feature.

> Configure project :projects:foo:bar
Hello, world! Applying to bar

FAILURE: Build failed with an exception.

* Where:
Build file '/Users/sean_abraham/projects/personal/gradle-repros/included-build-plugin-application/projects/foo/build.gradle' line: 4

* What went wrong:
Could not apply requested plugin [id: 'com.example.buildplugin'] as it does not provide a plugin with id 'com.example.buildplugin'. This is caused by an incorrect plugin implementation. Please contact the plugin author(s).
> Plugin with id 'com.example.buildplugin' not found.

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 669ms
```
