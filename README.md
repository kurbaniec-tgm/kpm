# "*KPM - **K**acper's **Password** **Manager***"

**KPM** is a simple, web-based password manager. The backend is nearly completely written in Kotlin, the web-interface is based on JavaScript with jQuery.

Why? I want to have access to my passwords all the time and be independent, I do not want to share my credentials with someone else.

## Requirements

  *  Java-JDK (*tested with [OpenJDK 1.8.0_211][1]*)
  *  MongoDB-Database (*compatible with [driver 3.9][3]*)

## Releases

If you just want to run the password manager without source code, just grab the latest [release](https://github.com/kurbaniec-tgm/kpm/releases).

## Usage

You can use the `kpm.py` python-script for easy building/running or doing it manually. `kpm.py` needs python 3 (*tested with [python 3.7][4]*), also `kotlinc` and  `java` need to be added to the system path.

* Compile project:    
  `gradle build` 
  
* Run project:   
  `gradle run`

After starting, you should access the password manager on `https://localhost:8080`.

## Configure

Some aspects of the application can be configured in `res\config.toml`. Here are some of the most important ones:

**[Server]:**

* `ip_address`: The ip-address the server is bound to.
*  `https_port`: The port the server listens to https-requests.
  * Note: There is also a `http_port` setting, but you should not use the http-protocol with sensitive data in use.    
    You can disable it with `http = false` .

**[Security]**:

* `timeout`: The time, in which the user is automatically logged out when no request is send in the meantime. In minutes.
* `password_timeout`:  The timeout, in which a user can not log in, because he used a wrong password to often. In minutes.
* `password_fails`: Sets the amount of bad authentication needed to trigger the login timeout.

**[Keystore]**:

* `generate`: Use `true` if you want to automatically create a certificate for the https-encryption. Be aware that this certificate is not signed by any authority and therefore not trusted by most browsers. If you have a valid certificate use `false` and fill the `file`-attribute.
* `file`: If you have a TLS-certificate for your domain, convert it into a java-keystore (`.jks`-file) and put it into the `res` folder.  The `file`-attribute should be the name of this file.
* `password`: The password the generated certificate should use or the password to your own keystore.

**[Mongo]:**

* `uri`: The connection description to your MongoDB-database in the "String URI Format". Click [here][5] for more information.
* `UserRepo`: Name of your collection that will store user-accounts.
* `PasswordRepo`: Name of your collection that will store password-entity data.

## Useful info

**KPM** uses cookies to identify user-sessions, so do not disable them in your browser. Also be aware that at the moment all data is stored in plain text in the MongoDB-database. So make sure to give your database a password and bind it only to `localhost`. 


[1]: https://github.com/ojdkbuild/ojdkbuild
[2]: https://github.com/JetBrains/kotlin/releases/tag/v1.3.50
[3]: https://mongodb.github.io/mongo-java-driver/3.9/
[4]: https://www.python.org/downloads/release/python-370/
[5]: https://docs.mongodb.com/v3.6/reference/connection-string/	"da"
