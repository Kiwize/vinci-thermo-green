IMPORTANT :

Afin de faire fonctionner l'application sous forme d'archive JAR exécutable, il est obligatoire
de créer un répertoire "data" contenant le fichier de configuration "db.env". 

Le fichier "db.env" sera formatté tel qu-il suit :

jdbc:mysql://[IP]:3306/[DB_NAME]
[USERNAME]
[PASSWORD]


Une mauvaise configuration empêchera le fonctionnement de l'application et génèrera un fichier log résultant
du crash de l'application. Référez-vous à celui-ci pour plus d'informations concernant la nature de l'erreur.

Si une erreur de démarrage persiste, envoyez un ticket sous le dépôt GitHub en détaillant toute les étapes de configuration
réalisées au préalable.