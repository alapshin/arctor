# Arctor
Android MVP Library

There are many like it, but this one is mine.

Heavily inspired by [Mosby](https://github.com/sockeqwe/mosby), [Nucleus](https://github.com/konmik/nucleus) and [series](http://blog.bradcampbell.nz/rxjava-handling-configuration-changes-with-request-observables/) of posts by Brad Campbell

## Installation
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

## Usage
First you need to create view interface `MvpView`
```java
public interface FooView extends MvpView {
    onData(int data);
    onError(Throwable error);
}
```
After that you need to create presenter interface
```java
public interface FooPresenter extends Presenter<FooView> {
    int getData();
}
```

Using interfaces for declaring relationships between view and presenter
we can later test view and presenter separately. When testing view we can
have mock presenter and vice versa when testing presenter we can have
mock implementation of view interface.

### View implementation
After declaring view and presenter interfaces next step is implement them.
For views library provides abstract classes `MvpActivity`, '`MvpFragment`,
`MvpFrameLayout` (also `MvpLinearLayout` and `MvpRelativeLayout`) that
implements methods necessary to facilitate interaction between view and
presenter.
```java
public class FooActivity extends MvpActivity<FooView, FooPresenter>
    implements FooView {
    @Override
    public void onData(int data) {
    // show data
    }

    @Override
    public void onError(Throwable error) {
    // show error
    }
}
```
All abstract classes contains `presenter` field of respective presenter
type annotated with `@Inject` so you can inject you presenter using Dagger.
This way you presenter will survive orientation change.

If you don't use dependency injection you could override method
`getPresenter()` and provide presenter instance through other means.

### Presenter implementation
Library provides base presenter implementation in `BasePresenter`
```java
public class FooPresenter extends BasePresenter<FooView>
    implements FooPresenter {
    @Override
    public void getData() {
        int data = someStore.getData();
        if (isViewAttached()) {
            getView().onData(data);
        }
    }
}
```
When calling view methods  from presenter you have to remember that
there are can be moments when presenter doesn't have corresponding view
attached yet or view was already detached from presenter so you should
always check for this situation.

### Presenter with RxJava support
Alternatively library provides implementation of RxJava based presenter
`RxPresenter` in separate modules `arctor-rxjava` and `arctor-rxjava2`
(for RxJava1 and RxJava2 respectively).
```java
public class FooPresenter extends RxPresenter<FooView>
    implements FooPresenter {
    @Override
    public void getData() {
        Subscription subscription = someReactiveStore.getData()
            .compose(deliverLatest())
            .subscribe(
                data -> getView().onData(data),
                error -> getView().onError(error)
            );
        addSubscription(subscription);
    }
}
```

Both RxJava1 and RxJav2 modules provide two transformers
`WaitViewLatestTransformer` and `WaitViewReplayTransformer` that
can be used to handle propagation of values from source observable to
view depending on current view status (attached or not).

Also both RxJava1 and RxJava2 based presenter provide basic support
for canceling running task when presenter is destroyed using
`addSubscription` and `addDisposable` methods respectively.

### ViewState and command pattern for view interaction
When sending data from presenter to view you could call view methods
directly. But what happens when your view is destroyed and recreated?
All view state is lost. You could implement you presenter in such ways
that result of every operation is cached and after new view
is attached to presenter it simply executes same series of steps and
receive all cached data immediately. But sometimes this is not possible.

To solve this problem this library provides simple implementation
of command pattern for view interaction. Main idea is borrowed from
[Moxie](https://github.com/Arello-Mobile/Moxy) library but implementation
is more trivial and doesn't rely heavily on annotation processing.

To use this feature you should define different commands for different
view interactions
```java
public class FooDataCommand implements ViewStateCommand<FooView> {
    final int data;

    public FooDataCommand(int data) {
        this.data = data;
    }

    @Override
    public int type() {
        return 0; // command type to differentiate between different commands
    }

    @Override
    public void execute(FooView view) {
        view.onData(data);
    }
}

```
```java
public class FooErrorCommand implements ViewStateCommand<FooView> {
    final Throwable error;

    public FooErrorCommand(Throwable error) {
        this.error = error;
    }

    @Override
    public int type() {
        return -1; // command type to differentiate between different commands
    }

    @Override
    public void execute(FooView view) {
        view.onError(error);
    }
}
```
And then in presenter use `executeCommand` method
```java
public class FooPresenter extends BasePresenter<FooView>
    implements FooPresenter {
    @Override
    public void getData() {
        int data = someStore.getData();
        FooDataCommand command = new FooDataCommand(data);
        if (isViewAttached()) {
            executeCommand(command, CommandStoreStrategy.ADD);
        }
    }
}
```
This method applies command to currently attached view but also stores
it in command queue. After new view is attached to presenter all commands
stored in command queue are applied to attached view.

### Using annotation processor to generate command implementations
View could have multiple methods and writing boilerplate command
implementation for every one of them is not fun. So this library provides
annotation processor that can simplify this step.

Include dependencies
```
provided 'com.google.auto.value:auto-value:1.3'
apt 'com.google.auto.value:auto-value:1.3'
compile 'com.alapshin.arctor:arctor-annotation:x.y.z'
apt 'com.alapshin.arctor-processor:x.y.z'
```

After that add following annotation to your view interface
```java
public interface FooView extends MvpView {
    @GenerateCommand(0)
    onData(int data);
    @GenerateCommand(-1)
    onError(Throwable error);
}
```
For every method annotated with `@GenerateCommand` annotation processor
would generate command implementation
```java
@AutoValue
public abstract class OnDataCommand implements ViewStateCommand<FooView> {
    @Override
    public int type() {
        return 0;
    }

    public abstract int data();

    @Override
    public void execute(FooView view) {
        view.onData(data());
    }

    public static SetDataCommand create(int data) {
        return new AutoValue_OnDataCommand(data);
    }
}
```
Generated class is using [AutoValue](https://github.com/google/auto/blob/master/value/userguide/index.md)
so generated commands are immutable.
