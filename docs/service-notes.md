# Atelier 4 — IoC, injection de dépendances et couche Service

Packages : `Services` (interfaces `I<Entité>Service`), `Services.Impls` (implémentations `<Entité>ServiceImpl`), `exception` (`ResourceNotFoundException`).
Chaque implémentation est annotée `@Service` + `@RequiredArgsConstructor`, avec **un champ `final`** typé par l'interface du repository : injection par constructeur, aucun `new`, aucune injection par attribut, aucun attribut modifiable (service sans état).

## A. Modes d'injection comparés (étape 2)

Test réalisé dans `ContratServiceImpl` : `@RequiredArgsConstructor` + `final` remplacés temporairement par `@Autowired private IContratRepository contratRepository;`, puis restaurés.

| Critère | Constructeur (`@RequiredArgsConstructor` + `final`) | Attribut (`@Autowired` sur le champ) |
|---|---|---|
| Champ `final` possible ? | Oui : affecté une seule fois dans le constructeur. | Non : Spring affecte le champ **après** la construction, par réflexion. |
| Dépendance visible dans la signature de la classe ? | Oui : paramètre du constructeur. | Non : cachée dans un champ privé. |
| Utilisable avec `new` hors conteneur sans dépendance `null` ? | Oui : impossible d'écrire `new ContratServiceImpl()` sans fournir le repository. | Non : `new ContratServiceImpl()` compile, `contratRepository` reste `null` → `NullPointerException` au premier appel. |
| Signalé par SonarQube for IDE ? | Non. | Oui : `java:S6813` — *Field dependency injection should be avoided*. |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| `ContratServiceImpl` | `IContratRepository` | Constructeur | Dépend de l'abstraction (proxy Spring Data). Règle : montant total négatif refusé ; la suppression supprime les paiements par cascade (ALL + orphanRemoval). |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur | Lecture seule (`findById`, `findAll`) : les paiements sont gérés via le Contrat (composition). |
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur | Champ `final`, service immuable et sans état. Règle : nom obligatoire. |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur | Règle : nom, prénom et rôle obligatoires. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur | Règle : tarif journalier négatif refusé. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur | Règle : libellé obligatoire (le modèle n'a pas d'attribut quantité). |
| `ClientServiceImpl` | `IClientRepository` | Constructeur | Règle : nom et e-mail obligatoires. |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur | Règle : date de fin antérieure à la date de début refusée. |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur | Règle : date de fin antérieure à la date de début refusée (le modèle n'a pas d'attribut coût). |

Règles communes à tous les services CRUD :
- `create` refuse un identifiant non `null` (sinon `save` ferait un `merge` et pourrait écraser une ligne existante) ;
- `findById` lève `ResourceNotFoundException` au lieu de renvoyer un `Optional` vide ou `null` ;
- `update` charge l'existant, recopie uniquement les attributs simples, puis `save` (identifiant et associations inchangés) ;
- `deleteById` vérifie `existsById`, car `deleteById` du repository ne lève rien si l'id n'existe pas ;
- les règles métier lèvent une `IllegalArgumentException` avec un message clair.

## C. Messages d'erreur du conteneur (étape 3)

**Message 1** — `UnsatisfiedDependencyException` causée par `NoSuchBeanDefinitionException` : le constructeur de `ContratController` demande un bean de type `IContratService` et le conteneur n'en trouve aucun.
Causes probables côté `ContratServiceImpl` :
1. l'annotation `@Service` a été oubliée : la classe n'est pas détectée par le scan, aucun bean n'est créé ;
2. la classe est placée **hors** du package racine (`tn.esprit.zouitenhamza4iet_i4` et ses sous-packages) : le scan lancé par `@SpringBootApplication` ne la voit pas.
(À vérifier aussi : la classe doit bien `implements IContratService`.)
Correction : ajouter `@Service` et/ou déplacer la classe dans un sous-package du package racine.

**Message 2** — `NoUniqueBeanDefinitionException` : `NotificationController` attend **un** bean d'un type, mais deux candidats existent (`emailNotificateur`, `smsNotificateur`).
Deux corrections :
1. `@Primary` **sur la classe** d'une implémentation (ex. `EmailNotificateur`) : elle devient le candidat par défaut partout ;
2. `@Qualifier("smsNotificateur")` **sur le paramètre du constructeur** de `NotificationController` : choix explicite à ce point d'injection (avec Lombok, écrire le constructeur à la main ou configurer `lombok.copyableAnnotations` pour recopier le `@Qualifier` du champ).
(Autre possibilité : injecter `List<Notificateur>` pour recevoir tous les beans.)

**Message 3** — Dépendance circulaire (`BeanCurrentlyInCreationException`) : `clientServiceImpl` dépend de `reservationServiceImpl`, qui dépend de `clientServiceImpl`.
Avec l'injection par constructeur, pour construire A il faut B déjà construit, et pour construire B il faut A : aucun objet partiellement construit ne peut être fourni, le cycle est impossible à résoudre (et Spring Boot interdit les références circulaires par défaut).
Piste de conception : supprimer le cycle en extrayant la responsabilité commune — par exemple faire dépendre `ClientServiceImpl` de `IReservationRepository` (et non du service), ou créer un troisième service (ex. `ReservationClientService`) qui dépend des deux repositories.

## D. Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| `@Autowired private IContratRepository contratRepository;` dans `ContratServiceImpl` (étape 2) | `java:S6813` — injection par attribut : dépendance cachée, champ non `final`, objet utilisable avec un champ `null` hors conteneur. | Retour à `@RequiredArgsConstructor` + `private final IContratRepository contratRepository;`. |
| `throw new RuntimeException(...)` dans `ContratServiceImpl.findById` (expérience étape 4) | `java:S112` — *Generic exceptions should never be thrown* : l'appelant ne peut pas distinguer cette erreur des autres. | Remplacée par `ResourceNotFoundException("Contrat", id)` (exception métier). |
| Imports inutilisés dans `VehiculeServiceImpl` (`Autowired`, import statique `AbstractPersistable_.id`) et dans le contrôleur véhicule (`AllArgsConstructor`, `Autowired`) | `java:S1128` — *Unnecessary imports should be removed*. | Imports supprimés. |

## E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` et qui l'exécute ?**
   Nulle part dans notre code : c'est le conteneur Spring (`ApplicationContext`, créé par `SpringApplication.run`) qui détecte la classe grâce à `@Service`, puis appelle par réflexion le constructeur généré par Lombok en lui passant le proxy de `IContratRepository`.

2. **Pourquoi un contrôleur dépend-il de `IContratService` et non de `ContratServiceImpl` ?**
   Pour le couplage faible : le contrôleur ne connaît que le contrat (l'interface). On peut changer d'implémentation ou fournir une implémentation de test sans modifier le contrôleur, et Spring peut injecter un proxy (transactions, sécurité) qui implémente la même interface.

3. **Pourquoi un service singleton doit-il rester sans état ? Exemple de bug.**
   Une seule instance est partagée par tous les threads (toutes les requêtes HTTP en parallèle). Exemple : un attribut `private Contrat contratCourant;` affecté dans `create()` puis utilisé pour calculer le montant — deux requêtes simultanées écrasent mutuellement `contratCourant`, et le client A enregistre le contrat du client B.

4. **Pourquoi `update` charge-t-il l'entité existante avant `save` ?**
   Passer directement l'objet reçu à `save` déclenche un `merge` qui recopie **toutes** les colonnes, y compris celles non envoyées (écrasées par `null`) ; avec `orphanRemoval`, la liste `paiements` vide de l'objet reçu supprimerait tous les paiements du contrat. Charger puis recopier seulement les attributs modifiables protège l'identifiant et les collections, et lève `ResourceNotFoundException` si l'id n'existe pas (au lieu d'insérer une nouvelle ligne).

5. **`@Component` / `@Bean`, `@Primary` / `@Qualifier`, et `IPaiementService` ?**
   - `@Component` se pose sur une classe qu'on écrit : elle est détectée par le scan. `@Bean` se pose sur une méthode d'une classe `@Configuration` qui crée et renvoie l'objet explicitement : pour une classe qu'on ne peut pas annoter (bibliothèque, JDK) ou une construction particulière.
   - `@Primary` se pose sur la définition du bean : candidat par défaut quand plusieurs beans ont le même type. `@Qualifier("nom")` se pose au point d'injection : choix explicite, prioritaire sur `@Primary`.
   - `IPaiementService` n'expose ni création, ni modification, ni suppression car Paiement est en **composition** avec Contrat (cascade ALL + orphanRemoval) : son cycle de vie est géré par le Contrat (`addPaiement` / `removePaiement`). Le modifier directement contournerait l'agrégat et désynchroniserait la collection du contrat.
