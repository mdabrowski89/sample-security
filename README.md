# sample-security
Android app for demonstrating Android security encryptiopn features. It is written with MVI pattern
<br><br>App consist of three sections:
#### 1. SecretKey
User can generate secretKey and encrypt/decrypt message using it.
#### 2. Fingerprint
User can generate secretKey which is secured with his Fingerprint and can use it for encryption/decryption of provided message. On each decryption/encryption action needs to confirmed with user fingerprint.
#### 3. Pin
User can generate keyPair and encrypt/decrypt message using public/private key from this keyPair. Private key requires user authentication so on decryption action user needs to authenticate with device pin (or pattern). Authentication is valid for 10 seconds and in that period user can decrypt messages without another authentication.
