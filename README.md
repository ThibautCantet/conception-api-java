# Super hero Training Application

## Avant de commencer 
Il est essentiel de vérifier que l'existant fonctionne sur votre poste.

### Déploiement de l'application

* Depuis votre terminal, executez la commande :  

        mvn spring-boot:run

- [x] Vérifiez bien que vous lancez la commande depuis la racine du projet.

Vous devriez avoir le message ci-dessous dans le terminal:


    ... superhero.SuperHeroApplication   : Started SuperHeroApplication in 8.19 seconds (JVM running for 8.888) 


* Depuis votre navigateur, allez à l'addresse: `` http://localhost:8080/``

Vous devez avoir une page comme ci-dessous: 
![image info](src/main/resources/assets/welcome_page_it_works.JPG)

### Vérification des données initiales
Au démarrage, nous initialisons la table des super héros grâce à [liquibase](https://www.liquibase.org/).

Nous utilisons [H2](https://h2database.com/html/main.html) pour la persistance ( éphémère) des données en mémoire.
* Vérifiez que les données initiales ont bien été chargées:

    * Aller à l'addresse: `` http://localhost:8080/h2-console``
    
    * Connectez-vous à la base de données: 
    ![image info](src/main/resources/assets/super-heroes-h2.JPG)
- [x] Vérifiez bien que la valeur du champ JDBC URL. Elle doit être égale à:  *jdbc:h2:mem:super-hero-app*
   
    * Lister les entrées de la table super_hero:     `` select * from super_hero``
       ![image info](src/main/resources/assets/listing_heroes.JPG)

- [x] Tout marche bien ? Chouette ! Tous les tests sont-ils au vert ?

### Execution des tests
 
* Depuis votre terminal, executez la commande :  

        mvn clean test

- [x] Vérifiez bien que vous lancez la commande depuis la racine du projet.

Vous devriez avoir le message ci-dessous dans le terminal:

    
    ...
    [INFO] Results:
    [INFO]
    [INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    ...
    
Si vous choisissez de lancer les tests depuis votre IDE ( clic droit > run all tests), vous verrez la barre verte \o/ 
![image info](src/main/resources/assets/all_green.JPG)

## TP 1 : Définir vos APIs

Avec votre équipe métier, vous avez défini vos endpoints. Il vous faut désormais les implementer !

Pour rappel, vous avez défini les choix suivant :

Tout vos services seront exposé en V1 sur `/api/v1`

* `/super-heros` :
  * `GET` : Liste tous les super héros dans la base de données
  * `POST` : Créé un super héros
* `/super-heros/{uuid}`
  * `GET` : Récupère un super héros
  * `PUT` : Met à jour un super héros
* `/missions` :
  * `GET` : Liste toutes les missions
  * `POST` : Créé une nouvelle mission (une mission doit être rattaché à un super heros)
* `/missions/{uuid}`
  * `GET` : Récupére une mission
* `/missions/{uuid}/history-events`
  * `GET` : Liste l'historique des événements d'une mission
  * `POST` : Rajoute une nouvelle évènement dans l'historique d'une mission
  
## TP 2 : Implémentation des APIs 
  
Pour cette partie, nous utiliserons [Spring Web MVC](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html) mais, vous pouvez utiliser [Apache CXF](http://cxf.apache.org/) 
si vous êtes plus à l'aise.
Spring Web MVC est déjà reférencée dans ce projet.

    
    <dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-web</artifactId>
	</dependency>

### Etape 1: Ajouter un provider pour Jackson 
  
  Afin de pouvoir sérialiser et désérialiser vos POJOS, vous aurez besoin d'un provider implémentant JAX-RS (JSR-331).

    <dependency>
      	<groupId>com.fasterxml.jackson.jaxrs</groupId>
       	<artifactId>jackson-jaxrs-json-provider</artifactId>
    </dependency>

Qui dit implémentation dit Tests car le Test est votre premier client/ consommateur d'API.  

### Etape 2 :Ajouter de quoi tester vos APIs !
   
Nous avons choisi restAssured pour l'implémentation des tests. Et comme nous avions opté pour du Spring MVC, 
nous pouvons directement utiliser *RestAssured pour Spring Mock MVC*.
   
    <dependency>
      	<groupId>io.rest-assured</groupId>
      	<artifactId>spring-mock-mvc</artifactId>
      	<scope>test</scope>
    </dependency>
    
### Etape 3 : Créez votre API
Créer les endpoints défini lors du TP1 dans 2 classes :
  * `SuperHeroApi`
  * `MissionApi`
    
Penser bien à annoter vos classes & vos méthodes des annotations suivantes :

* `@GetMapping`
* `@PostMapping`
* Et surtout ... `@RestController` du package `org.springframework.web.bind.annotation.*` au dessus de la classe;
 
   
### Etape 4: Test driven everything ! ( Je vous accompagne)
    
Voici un petit exemple de test qui vérifie vérifie qu'une API renvoie HTTP 200 lorsque l'adresse `/api/v1//super-heroes/` est implémentée.
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuperHeroApiShould {

    @LocalServerPort
    private int port;

    protected RequestSpecification given() {
        return RestAssured.given()
                .port(port)
                .basePath("/api/v1/");
    }

   @Test
    void respond_with_a_200_status_when_getting_all_the_super_heroes_endpoint_exists() {

        this.given()
                .when()
                .get("/super-heroes/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
```
Le test est vert ? On peut passer en test du code de production !!! 

### Etape 5: Appelez votre API "Live"  (avec un client API) 
    
Votre test est vert? Félicitations, vous pouvez être sûrs/sûres que votre API répond.
 
#### Pour vérifier: 

Commencez par déployer votre application via spring boot: `mvn spring-boot:run`     
Puis **LE** test!
* Pour les plus pressés : ` curl localhost:8080/api/v1/super-heroes/`
 * Sinon, faire le même test avec Postman ( pour la posterité ou portabilité c'est selon :) ! 
    ![image info](src/main/resources/assets/get-all-super-heroes-via-postman.JPG)
 
### Etape bonus : Monitorez vos APIs comme en prod
Spring boot nous permet de monitorer des API très simplement via "actuator".
Cette dépendance est déjà présente dans ce projet:
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
- Modifier  la valeur de la proprieté : `management.endpoints.web.exposure.include` de `application.yaml` tel que: 
```yaml
management.endpoints.web.exposure.include: '*'
```
- [x] Dans la "vraie vie", il est très dangereux d'exposer tous les endpoints de l'actuator. Gros risques de sécurité. 
L'astérisque ici est pour les besoins de la formation.

#### Visualiser vos métriques : 
- Health check : http://localhost:8080/actuator/health
- Changement liquibase: http://localhost:8080/actuator/liquibase
- Loggers: http://localhost:8080/actuator/loggers

- [x] Curieux du reste ? Voir la [documentation officielle](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-enabling) de Spring boot.

## TP3 - Validation des données et gestion des erreurs fonctionnelles 
Ici, il s'agit de d'appliquer le conseil de "Ne jamais faire confiance à un utilisateur".
Ainsi, on veut pouvoir vérifier : 

 `Lors de la création d'un super héro :`  
 - [x] Le nom du super hero doit être renseigné

 `Lors de la création d'une mission :`  
- [x] Le nom de la mission doit être renseigné
- [x] le nom du super héro assigné à une mission doit être renseigné
- [x] Le super héro référencé pour une mission  doit exister 
- [x] Si le héro et la mission à créer existent déjà, ne rien faire.

`Lors la récupération de tous les événements historiques liée à une mission :`  
- [x] La mission doit exister

`Lors la création d'un événement historique liée à une mission :` 
- [x] La mission doit exister
- [x] La description de l'événement doit être renseignée

### Validation des données utilisateur 
- Ajouter l'implémentation officielle de  JSR 380 au projet : 
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
- Trouvez les valeurs à valider et ajoutez les contraintes grâce aux annotations appropriées de la JSR 380
S'inspirer de l'exemple de la [documentation officielle](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-gettingstarted-createmodel)

- Dans les API, annotez les endpoints cibles avec la contrainte `@Valid` comme dans l'exemple ci-dessous: 
```java
import org.springframework.web.bind.annotation.PostMapping;
public ResponseEntity<?> create(@Valid @RequestBody final CreateRequest request){}
```
## TP 4 : documentation 

Il est possible de générer automatiquement une documentation respectant la spec OpenApi 3.
Le voir en action, ajouter la dépendance `springdoc-openapi-ui`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.2.32</version>
</dependency>
``` 
Rien que l'ajout de cette dépendance, aidée des annotations sur nos API et des contraintes de validation de beans, nous permet 
d'avoir une documentation respectant spec OpenAPI 3. Cette dernière est accessible par défaut sur : 
`http://localhost:8080/v3/api-docs/` 

- [x] La documentation par défaut est au format YAML.

Vous préférez du swagger-ui ? Pas de problème, allez sur le  
`http://localhost:8080/swagger-ui.html`

## Exerice : Modifier le chemin de la documentation 

Modifier le chemin de génération de la documentation afin que les deux versions soient disponibles comme suit:
- Yaml : `http://localhost:8080/api/v1/docs`
- Swagger-ui :  `http://localhost:8080/api/v1/docs/swagger-ui.html`

Pour y arriver, allez dans le fichier application.yaml et ajoutez la configuration ci-dessous:

- OpenAPI 3.0
```yaml
springdoc:
  api-docs:
    path: /api/v1/docs
```

- Swagger
 
```yaml
springdoc:
  swagger-ui:
    path: api/v1/docs/swagger-ui.html
```
