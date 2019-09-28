# preferences

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
implementation 'com.github.dron4ik89:preferences:0.0.2'
```

```
Preferences.init(this) - init
```

```
Preferences.save("key", "value") - save
Preferences.load("key", "default value") - load
```
