<a href="https://www.etsmtl.ca/"><img src="https://www.etsmtl.ca/getmedia/e5def190-3637-4f1a-8409-84d1c683bdbf/ETS-noir-devise-ecran" title="ETS" alt="ETS"></a>

# PFE-E2020 - Plateforme IPTV - Box Android

> L’objectif du projet est de faire une preuve de concept pour AzurDev. Cette preuve de concept est faite pour démontrer qu’une boîte AndroidTv répond à toutes les demandes de leur système de télévision (NOAN).

//TODO (Insert Gif)

## Table des matières

- [Applications](#applications)
- [Installation](#installation)
- [Tests](#tests)
- [Log](#log)
- [Équipe](#team)
- [FAQ](#faq)
- [License](#license)


## Applications

### Application web
[![Build Status](https://travis-ci.com/alexandresimardforbes/PFE-E2020.svg?token=qFfiYy7DkftyCmyguegr&branch=webapimaster)](https://travis-ci.com/alexandresimardforbes/PFE-E2020)

Notre application web déployé sur la plateforme Heroku contient un site web mockup de NOAN par AzurDev et les requêtes de notre API.

**URL :** <a href="https://pfe-e2020-noan.herokuapp.com/" target="_blank">**Application web**</a> 

## Installation

### Clone

- Cloner le répertoire sur votre machine locale en utilisant ce lien `https://github.com/alexandresimardforbes/PFE-E2020.git`

### configuration web API

> Télécharger et installer Node js

> Mettre à jour et installer les paquets

```shell
$ npm install
```

> Rouler le serveur

```shell
$ npm run dev
```

//TODO (Add other apps) 

## Log

- 2020-07-17 - README.md version 1.0.0
- 2020-07-20 - Web app release 1.0.0 on Heroku

## Tests 

We added unit testings for our HTTP Request via our web API. 

## Team

- <a href="https://github.com/Caribosaurus" target="_blank">**Caribosaurus (Guillaume)**</a>
- <a href="https://github.com/cbrunelle" target="_blank">**cbrunelle (Charles)**</a> 
- <a href="https://github.com/jonathanMenard" target="_blank">**jonathanMenard (Jonathan)**</a>
- <a href="https://github.com/alexandresimardforbes" target="_blank">**alexandresimardforbes (Alexandre)**</a>

## FAQ

- **Comment nous contacter**
    - Via Slack : `pfe021ete2020-cts8981.slack.com`

- **Lien pour le postman d'équipe**
    - **URL** : `https://app.getpostman.com/join-team?invite_code=ad61fc8a1ff2dc997c421f20831bdcf0`
    
- **Lien pour la documentation Swagger**

    Local host : 
    - Activer cette commande en localhost 
    ```shell
    $ npm run dev
    ```
    - Aller sur le lien suivant : `http://localhost:5000/api-docs/#/`
    
    Web :
    - **URL :** <a href="https://pfe-e2020-noan.herokuapp.com/api-docs/#/default" target="_blank">Online swagger docs</a>
    
## License
//TODO

