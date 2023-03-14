# Preference Center Consent SDK

#### AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```



#### Consents JSON

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
    ...
  }
  
]
```