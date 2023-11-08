# viewer-CDA
1	Introduction

La visualisation des documents CDA directement via les navigateurs internet étant de moins en moins possible pour des raisons de sécurité, l'ANS a développé un Viewer CDA.

Le Viewer CDA de l'ANS permet :
-	de visualiser un document CDA avec une feuille de style (il faut sélectionner le document CDA d'une part et la feuille de style d'autre part).
-	de visualiser un document CDA dans un navigateur internet
-	de générer un document PDF à partir du document CDA : si le document CDA contient une copie PDF dans la section spécifique FR-Document-PDF-copie, celle-ci ne sera pas prise en compte.
-	d'ouvrir un document CDA dans un éditeur (qui permet notamment de visualiser l'arborescence du CDA)
-	d'ouvrir un document CDA dans l’éditeur par défaut des fichiers XML sur le poste (Oxygen par exemple)

2	Utilisation du Viewer CDA de l'ANS

Pour ouvrir le Viewer CDA, double cliquez sur ANS_Viewer-CDA_2023.01.jar.
Vous devez déposer les documents CDA à visualiser dans le répertoire ExemplesCDA de testContenuCDA (testContenuCDA est téléchargeable ici).
Vous devez ensuite :
-	Sélectionner le document CDA (dans le répertoire ExemplesCDA de testContenuCDA)
-	Sélectionner la feuille de style (dans le répertoire FeuilleDeStyle de testContenuCDA)
o	Vous pouvez utiliser la feuille de style CDA-FO.xsl de l'ANS
o	Vous pouvez utiliser une feuille de style personnalisée (dans ce cas, vous devez la déposer dans le répertoire FeuilleDeStyle de testContenuCDA).

3	Prérequis

Java doit être installé sur votre poste utilisateur.
Vous devez disposer de testContenuCDA sur votre poste utilisateur. 
Pour la génération de la DataMatrix il faut s’aligner à notre exemple développer dans la feuille de style « CDA-FO.xsl » (code javascript de la ligne 1136 à la ligne 1150 et code xsl de la ligne 6018 à la ligne 6190). 
Il faut aussi prendre en compte le nom de la classe de style « barcodeStyle » pour la « div » qui contient la DataMatrix et ne pas le changer car l’affichage en mode web sur Firefox ou même sur le Viewer est basé sur ce nom de classe.  

Pour le bon fonctionnement du Viewer CDA de l'ANS, vous devez respecter l’arborescence du dossier "testContenuCDA" avec :
Racine testContenuCDA
  	Dossier FeuilleDeStyle
 


Problème possible de lancement lié à Java
Si vous avez l'exception "JVM Launcher exception", vous devez mettre à jour votre version de Java.
Pour cela, installez une nouvelle version de Java en cliquant sur le lien : https://download.oracle.com/java/20/latest/jdk-20_windows-x64_bin.exe.
Après l’installation de Java, ouvrez l’invite de commande (CMD) et tapez "java -version" pour vous assurer que la nouvelle version de Java est installée.
Vous pouvez alors relancer le Viewer CDA.

