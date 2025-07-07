# Proyecto base de Serenity BDD

1. Realizado en Java 16
2. Dependencia Gradle 8
3. Patron Screenplay
4. Cucumber
5. Se debe realizar la ejecucion mediante el ultimo comando

### Ejecución de test por línea de comandos

#### Gradle

#### Este comando sirve para mirar que URL abrir dependiendo de lo que este configurado en serenity.conf
```
gradlew clean test -Denvironment
```
#### Este comando sirve reconstruir y limpiar el proyecto

```
gradle clean build
```
#### Este comando es para ejecutar por tag y feature en especifico
```
gradle clean test -Dcucumber.filter.tags="@testQAOrange" -Dcucumber.features=src/test/resources/features/template.feature
```
#### Este comando es para ejecutar por tag y feature en especifico
```
gradle clean test -Dcucumber.filter.tags="@testQAOrange"
```

