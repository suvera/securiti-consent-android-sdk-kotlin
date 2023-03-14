# Preference Center Consent SDK




## Gradle

Add repository to settings.gradle of your application

```groovy

repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```

Add dependency to build.gradle of your application

```groovy
dependencies {
    implementation 'com.github.suvera:securiti-consent-android-sdk-kotlin:main-SNAPSHOT'
}
```

## Usage

### 1. Update AndroidManifest.xml , make sure below permission is set
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 2. SDK Code Integration
On a button click or menu item click, add code like below.

```kotlin
binding.btnShowPrefCenter.setOnClickListener {
    val conf = Configuration(
        prefCenterId = "a3196671-ca93-46f2-9ed8-7690f2cebbe5",
        prefCenterToken = "qa~b2bc21e0-0db5-4947-8208-a05436ccea72"
    )
    val sdk = PreferenceCenterSDK(conf, context as Context)
    
    sdk.showPreferenceCenter(parentFragmentManager, PreferenceCenterSDK.UI_SINGLE_COLUMN)
}
```

There multiple ways preference center can be rendered.

1. WebView
2. Frgament
3. DialogFragment
4. Activity
5. Dialog

```kotlin
sdk.showPreferenceCenter(parentFragmentManager, PreferenceCenterSDK.UI_WEB_VIEW)
```

#### Event Listener

Mobile App can listen to Preference Center events

```kotlin
sdk.showPreferenceCenter(parentFragmentManager, PreferenceCenterSDK.UI_SINGLE_COLUMN, object : ConsentActivityListener {
    override fun onPreferenceCenterLoaded(purposes: List<Consent>) {
        // do something or ignore
    }

    override fun onPreferenceCenterLoadFailed(ex: Exception) {
        // do something or ignore
    }

    override fun onConsentsSaved(consents: List<Consent>) {
        // do something or ignore
    }

    override fun onConsentsSaveFailed(consents: List<Consent>, ex: Exception) {
        // do something or ignore
    }

    override fun onConsentsCancelled() {
        // do something or ignore
    }
})
```

### 3. Consents JSON

```json
[
  {
    "processingPurposeId": 123,
    "processingPurposeName": "Online Marketing",
    "consentPurposeId": 345,
    "consentPurposeName": "Subscribe to Emails",
    "granted": true
  },
  
  {
  }
  
]
```