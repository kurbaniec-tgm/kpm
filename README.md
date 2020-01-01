<h1 align="center">
  <br>
  <img src="src/main/resources/web/favicon.ico" alt="solar-system" width="250"></a>
  <br>
  <br>
   <i><b>KPM</b> - Kacper's Password Manager</i>
</h1>

**KPM** is a simple, web-based password manager. The backend is nearly completely written in Kotlin with the [Salt framework](https://github.com/kurbaniec-tgm/salt),  the web-interface is based on JavaScript with jQuery.

Why? I want to have access to my passwords all the time and be independent, I do not want to share my credentials with someone else.

## ⬇️ Requirements

Java-JDK (*tested with [OpenJDK 1.8.0_211][https://github.com/ojdkbuild/ojdkbuild ]*)

## 🛠️ Build

`gradle build`  or  `./gradlew build`

## 🚴 Run

`gradle run` or  `./gradlew run`

After starting, you should access the password manager on `https://localhost:8080`.

## Useful info

**KPM** uses cookies to identify user-sessions, so do not disable them in your browser. Also for security reasons, give your Mongo database a password and bind it only to `localhost`. 

## Sources

\[1]: https://github.com/ojdkbuild/ojdkbuild        
\[2]: https://github.com/JetBrains/kotlin/releases/tag/v1.3.50         
\[3]: https://mongodb.github.io/mongo-java-driver/3.9/       
\[4]: https://www.python.org/downloads/release/python-370/       
\[5]: https://docs.mongodb.com/v3.6/reference/connection-string/       
\[6]:  https://stackoverflow.com/questions/2586975/how-to-use-curl-in-java	"Curl in Java"        
\[7]:  https://phauer.com/2018/best-practices-unit-testing-kotlin/  "Test Kotlin"      

## License

MIT

---

> GitHub [kurbaniec](https://github.com/kurbaniec-tgm) &nbsp;&middot;&nbsp;
> Mail [at.kacper.urbaniec@gmail.com](