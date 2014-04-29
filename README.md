Ergo [![Build Status](https://travis-ci.org/nhaarman/Ergo.svg)](https://travis-ci.org/nhaarman/Ergo)
===

Ergo is an Android library for easily executing tasks using `IntentServices`.
Feel free to use it all you want in your Android apps provided that you cite this project and include the license in your app.

About
---
Often developers use an `AsyncTask` for executing tasks such as downloading data. Handling state changes can be a real PITA.  
Ergo provides a way of executing these tasks using `IntentService`s and `ResultReceiver`s. State changes are handled by storing the `ResultReceiver` (which is bound to the `IntentService`), and binding proper callbacks again.

Usage
---

Add the following to your `build.gradle`:

```groovy
dependencies {
    compile 'com.nhaarman.ergo:library:0.1.+'
}
```

- Extend `ErgoService`
  - Implement `createTask(Intent)`
- Extend `ErgoActivity`
  - Implement `onCreateReceivers()`
  - Start your `ErgoService` using `ErgoService.startService(Context, Intent, ResultReceiver)`

A code example will be available soon.

License
---

	Copyright 2014 Niek Haarman

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

---
_έργο is Greek for 'task'._
