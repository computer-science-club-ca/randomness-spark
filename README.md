# randomness-spark
Exercice: Générer des échantillons de valeurs aléatoires à partir de différents générateurs puis analyser les données avec Spark.

Ce projet Java compare quelques générateurs de nombres aléatoires en évaluant le nombres d’occurrences qui se répètent parmi un échantillon de 250 000. On utilise Spark pour le décompte rapide des occurrences des valeurs. Puis on écrit les résultats dans des fichiers. 

Les générateurs sont : le générateur de valeurs aléatoires générique de Java, l’algorithme de L’Écuyer basé sur le temps actuel, un générateur linéaire congruentiel « LCG », un générateur congruentiel multiplicatif de Park et Miller et le générateur basé sur la radioactivité HotBits. L’implémentation de ces générateurs provient du package RandomX.

(Selon mes données, les valeurs générées par l’algorithme de L’Écuyer sont aussi diversifiées que celles obtenues par le service d’HotBits, les valeurs aléatoires produites à partir de la radioactivité.)

Un peu de théorie sur les générateurs de nombres aléatoires… Il y a deux familles de générateurs : pseudo-aléatoire et les véritables :

## Générateurs de nombres pseudo-aléatoires "Pseudo-Random Number Generators (PRNGs)"

Comme le suggère le « pseudo », les nombres pseudo-aléatoires ne sont pas aléatoires, du moins pas si vous avez l'habitude des dés ou des tickets de loterie. Essentiellement, les PRNGs sont des algorithmes qui utilisent des formules mathématiques ou simplement des tables « précalculées » pour produire des séquences de nombres qui semblent aléatoires. Beaucoup de recherches ont été menées sur la théorie des nombres pseudo-aléatoires, et les algorithmes modernes pour générer des nombres pseudo-aléatoires sont si bons que les chiffres semblent exactement comme s'ils étaient vraiment aléatoires.

## Véritables générateurs de nombres aléatoires "True Random Number Generators (TRNGs)"

En comparaison avec les PRNG, les TRNG extraient l'aléatoire des phénomènes physiques et l'introduisent dans un ordinateur. Vous pouvez l'imaginer comme un dé connecté à un ordinateur, mais généralement les gens utilisent un phénomène physique qui est plus facile de se connecter à un ordinateur qu'un dé. Le phénomène physique peut être très simple, comme les petites variations dans les mouvements de la souris de quelqu'un ou dans la quantité de temps entre les frappes. En pratique, cependant, vous devez faire attention à la source que vous choisissez. Un très bon phénomène physique à utiliser est une source radioactive. Les moments où une source radioactive se désintègre sont complètement imprévisibles, et ils peuvent facilement être détectés et introduits dans un ordinateur, évitant ainsi tout mécanisme de mise en mémoire tampon dans le système d'exploitation. Le service HotBits de Fourmilab en Suisse est un excellent exemple de générateur de nombres aléatoires utilisant cette technique. Un autre phénomène physique approprié est le bruit atmosphérique, qui est assez facile à détecter avec une radio normale. C'est l'approche utilisée par RANDOM.ORG.

## Comparaison des PRNG et des TRNG

Les TRNG conviennent à peu près à l'ensemble des applications pour lesquelles les PRNG ne conviennent pas, comme le cryptage des données, les jeux et les jeux de hasard. Inversement, la faible efficacité et la nature non déterministe des TRNG les rendent moins adaptés aux applications de simulation et de modélisation, qui nécessitent souvent plus de données qu'il n'est possible de générer avec un TRNG. Le tableau suivant résume quel type de générateur est le mieux approprié par type d’applications.

Application | Générateur le plus approprié

Loteries et tirages | TRNG

Jeux et jeux de hasard | TRNG

Échantillonnage aléatoire | TRNG

Simulation et modélisation | PRNG

Sécurité (par exemple, génération de clés de chiffrement de données) | TRNG

Les Arts | Varie

Pour en savoir plus, lisez https://www.random.org/randomness/


# Téléchargement

Vous pouvez téléchargez l'archives .zip du code ou bien utilisez git : $ git clone https://github.com/computer-science-club-ca/randomness-spark.git

## Installez Java 8 (JDK)

http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 

## Installez Eclipse Oxygen 4.7 IDE for Java EE Developers 

https://www.eclipse.org/downloads/eclipse-packages/

## Installez Gradle 

https://gradle.org/install/

## Installez Hadoop

* Téléchargez la version binaire du programme pour votre plateforme : https://hadoop.apache.org/releases.html
* Enregistrez le fichier binaire dans un répertoire de votre choix, par exemple :  c:\hadoop\bin
* Définissez les variables d’environnement HADOOP_HOME et PATH dans le panneau de configuration afin que tout programme Windows puisse les utiliser (Instructions : https://www.computerhope.com/issues/ch000549.htm) 
** Définissez HADOOP_HOME pour refléter le répertoire avec winutils.exe (sans le répertoire bin).
** Ajoutez à la variable d'environnement PATH pour inclure %HADOOP_HOME%\bin
