# Atelier 3 — Couche Repository (Spring Data JPA)

Package : `tn.esprit.zouitenhamza4iet_i4.Repository` — convention de nommage `I<Entité>Repository`.
Aucune classe d'implémentation n'est écrite : Spring Data génère un proxy (`SimpleJpaRepository`) au démarrage.
Au lancement, les logs affichent : `Found 9 JPA repository interfaces`.

## 1. Choix de l'interface

`IContratRepository` a d'abord été déclaré en `CrudRepository<Contrat, Long>` (étape 1), puis passé en `JpaRepository` comme les huit autres (étape 2).

| Interface | Étend | Justification |
|---|---|---|
| `IContratRepository` | `JpaRepository<Contrat, Long>` | CRUD complet, `findAll` renvoie une `List`, `saveAndFlush` disponible. La suppression passe par le contexte de persistance : la cascade ALL + orphanRemoval supprime aussi les paiements. |
| `IPaiementRepository` | `JpaRepository<Paiement, Long>` | Nécessaire pour **lire** les paiements. Les créations / suppressions passent par le Contrat (composition). |
| `IAgenceRepository` | `JpaRepository<Agence, Long>` | CRUD + listes + tri/pagination pour lister les agences (ex. par ville). |
| `IEmployeRepository` | `JpaRepository<Employe, Long>` | CRUD complet ; `List` plus pratique qu'un `Iterable` pour les traitements. |
| `IVehiculeRepository` | `JpaRepository<Vehicule, Long>` | Catalogue de la flotte : pagination et tri (ex. par `tarifJournalier`) utiles. |
| `IEquipementRepository` | `JpaRepository<Equipement, Long>` | CRUD complet sur un référentiel simple. |
| `IClientRepository` | `JpaRepository<Client, Long>` | CRUD complet, liste des clients paginable. |
| `IReservationRepository` | `JpaRepository<Reservation, Long>` | CRUD complet ; tri par `dateDebut` possible via `Sort`. |
| `IMaintenanceRepository` | `JpaRepository<Maintenance, Long>` | CRUD complet, historique des maintenances. |

**Pourquoi `JpaRepository` partout :** il réunit `ListCrudRepository` (CRUD avec des `List`) et `ListPagingAndSortingRepository` (tri, pagination), et ajoute `flush`, `saveAndFlush`, `getReferenceById`. Depuis Spring Data 3, `PagingAndSortingRepository` n'étend plus `CrudRepository` : `JpaRepository` évite de déclarer les deux.

**Iterable / List :** `CrudRepository.findAll()` renvoie un `Iterable` (pas de `size()`, `get(i)`) ; `ListCrudRepository` / `JpaRepository` renvoient une `List`.

**Suppressions en lot :** `deleteAllInBatch()` / `deleteAllByIdInBatch()` exécutent un seul `DELETE` sans passer par le contexte de persistance : cascade et orphanRemoval ne s'appliquent pas (paiements orphelins ou violation de clé étrangère). On utilise donc `deleteById` / `delete` pour un Contrat.

## 2. Rappel du comportement CRUD

| Méthode | Comportement observé (logs SQL) |
|---|---|
| `save(e)` avec id `null` | `persist` → `INSERT` ; l'id `IDENTITY` est renseigné dans l'objet retourné. |
| `save(e)` avec id renseigné | `merge` → `SELECT` puis `UPDATE` si des champs ont changé. Utiliser l'objet **retourné**. |
| `findById(id)` | `Optional<T>` : vide si la ligne n'existe pas (pas de `null`). |
| `deleteById(id)` | `SELECT` puis `DELETE` ; aucune exception si l'id n'existe pas (Spring Data 3). |

Les identifiants des entités sont des `Long` (et non `long`) : un nouvel objet a un id `null`, ce qui rend explicite la distinction INSERT / UPDATE.

## 3. Anomalies SonarQube for IDE

| Anomalie SonarQube for IDE | Règle / explication | Correction apportée |
|---|---|---|
| Import inutilisé `org.springframework.data.repository.CrudRepository` dans `IVehiculeRepository` | `java:S1128` — *Unnecessary imports should be removed* : un import non utilisé alourdit le code et masque les vraies dépendances. | Import supprimé (l'interface étend `JpaRepository`). |
| Méthode vide `deleteById` dans `VehiculeServiceImpl` | `java:S1186` — *Methods should not be empty* : une méthode vide laisse croire que l'opération est faite alors qu'il ne se passe rien. | Méthode implémentée : vérification `existsById` puis `deleteById`. |
| Classe `VehiculeRestController` (minuscule + faute de frappe) | `java:S101` — *Class names should comply with a naming convention* (`^[A-Z][a-zA-Z0-9]*$`). | Renommée `VehiculeRestController` (Refactor › Rename, Shift+F6). |
