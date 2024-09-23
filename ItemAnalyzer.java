package fr.pantheonsorbonne.ufr27.miashs.poo;

import java.lang.Double;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;

public final class ItemAnalyzer {
  private ArrayList<Item> items = new ArrayList<>();

  public ItemAnalyzer(ArrayList<Item> items) {
    this.items=items;
  }

  public Double getDureemoyenne() {
    // code here
    double dureeTotale = 0.0;
    int count = 0;
    for (int i = 0; i < items.size(); i ++){
      Integer duree = items.get(i).getDuree();
      if (duree != null){
         dureeTotale += duree;
         count += 1;
      }
    }
    return dureeTotale/count;
  }

  public HashMap<Integer, ArrayList<String>> getListemeilleurFilmParAnnee() {
    // code here
    HashMap<Integer, ArrayList<String>> listeMeilleurFilmParAnnee = new HashMap<>();
    HashMap<Integer, ArrayList<Item>> listeFilmParAnnee = new HashMap<>();
   
    // on cree HashMap qui contient tout les annees comme les cles et une liste des films asocciee a cette annee
    for (Item item : items){
      Integer annee = item.getAneeDeSortie();
      if (!listeFilmParAnnee.containsKey(annee)){
        listeFilmParAnnee.put(annee, new ArrayList<>()); 
        listeMeilleurFilmParAnnee.put(annee, new ArrayList<>()); 
      }
      listeFilmParAnnee.get(annee).add(item); 
    }

    for (int key : listeFilmParAnnee.keySet()){
      double maxEtoiles = 0.0;
      //on cherche le plus grand nombre d'etoiles de chaque annee
      ArrayList <Item> listeFilms = listeFilmParAnnee.get(key);
      for (Item film : listeFilms){
        Double currNbEtoiles = film.getNbEtoiles();
        if (currNbEtoiles != null && maxEtoiles < currNbEtoiles){
          maxEtoiles = currNbEtoiles;
        }  
      }
      // on parcours les films de cette annee et on trouve ces qui ont le nombre d'etoiles correspondant au maxEtoiles
      ArrayList <String> filmMaxEtoiles = new ArrayList <>();
      for (int k = 0; k < listeFilms.size(); k++){
        Double currNbEtoiles = listeFilms.get(k).getNbEtoiles();
        if (currNbEtoiles != null && currNbEtoiles == maxEtoiles){
            filmMaxEtoiles.add(listeFilms.get(k).getTitre());
        }
      }
      //on change des valeurs d'HashMap avec une liste des meilleurs films
      listeMeilleurFilmParAnnee.put(key, filmMaxEtoiles);   
    }
    return listeMeilleurFilmParAnnee;
  } 

  public String getFilmplusvotereal() {
    // code here
    int maxVotes = 0;
    Item filmMaxVotes = items.get(0);
    //on cherche le plus grand nombre d'etoiles de chaque annee
    for (Item item : items){
      Integer currVotes = item.getNbVotes();
      if(currVotes != null && maxVotes < currVotes){
          maxVotes = currVotes;
          filmMaxVotes = item;
      }  
    }
    return ("Le film ayant le plus de vote est " + filmMaxVotes.getTitre() + " de " + filmMaxVotes.getReal() + " avec " + maxVotes + " votes" ); 
  }

  public HashMap<String, Integer> getNbFilmsParGenre() {
    // code here
    HashMap <String, Integer> nbFilmsParGenre = new HashMap<>();
    for (Item item : items){
      for (String genre : item.getGenre()){
        if (!nbFilmsParGenre.containsKey(genre)){
          nbFilmsParGenre.put(genre, 1);
        }
        else{
          nbFilmsParGenre.put(genre, nbFilmsParGenre.get(genre) + 1);
        }
      }
    }
    return nbFilmsParGenre;
  }

  public Double getRSquareGrossStars() {
    // code here
    // formule: R2 = covariance**2 / variance(stars)*variance(gross)
    // covariance = somme[(star - moyenneStars)*(gross - moyenneGross)] / n
    // variance(stars)= somme[(star - moyenneStars)**2] / n
    // n se simplifie --> pas besoin 

    ArrayList<Double> starsListe = new ArrayList<>();
    ArrayList<Integer> grossListe = new ArrayList<>();

    //on verifie si item a en meme temps nbEtoiles et gross
    for (Item item : items){
      Double nbEtoiles = item.getNbEtoiles();
      Integer gross = item.getGross();
      if(nbEtoiles != null && gross != null){
        starsListe.add(nbEtoiles);
        grossListe.add(gross);
      }
    }
    double sommeStars = 0.0;
    int sommeGross = 0;
    int count = starsListe.size();
    for (int j = 0; j< count; j++){
      sommeStars += starsListe.get(j);
      sommeGross += grossListe.get(j);
    }
    double moyenneStars = sommeStars/count;
    double moyenneGross = sommeGross/count; 

    double covariance = 0.0; 
    double sommeSquareStars = 0.0;
    double sommeSquareGross = 0.0;
    for (int k = 0; k < count; k++){
      double equationStar = starsListe.get(k)-moyenneStars;
      double equationGross = grossListe.get(k) - moyenneGross;

      covariance += (equationStar*equationGross);

      sommeSquareStars += Math.pow(equationStar, 2);
      sommeSquareGross += Math.pow(equationGross, 2);
    }

    double rSquareGrossStars = (Math.pow(covariance,2)) / (sommeSquareStars * sommeSquareGross);
    return rSquareGrossStars;
  }


  public HashMap<String, ArrayList<String>> getActeursFrequantsGenre() {
    // code here
    HashMap <String, ArrayList<String>> acteursFrequentsGenre = new HashMap<>();
    HashMap <String, Integer> nbFilmsParActeur = new HashMap<>();
    //on verifie la frequence de chaque acteur
    for (Item item : items){
      if(item.getActeurs() != null){
        for (String acteur : item.getActeurs()){
          if (!nbFilmsParActeur.containsKey(acteur)){
            nbFilmsParActeur.put(acteur, 1);
          }
          else{
            nbFilmsParActeur.put(acteur, nbFilmsParActeur.get(acteur) + 1);
          }
        }
      }  
    }

    for (String key : nbFilmsParActeur.keySet()){
      // on veut prendre des genres des acteurs plus frequant que 1
      if(nbFilmsParActeur.get(key) > 1){
        for (Item item : items){
          for (String acteur : item.getActeurs()){
            if (acteur.equals(key)){
              // si acteur n'est pas encore dans l'HashMap comme le cle, on l'ajoute avec toutes les genres de cet item
              if (!acteursFrequentsGenre.containsKey(acteur)){
                acteursFrequentsGenre.put(acteur, new ArrayList<> (item.getGenre()));
              }
              // si acteur est deja dans l'HashMap comme le cle, on n'ajoute que des nouvelles genres 
              else{
                ArrayList <String> acteurGenre = acteursFrequentsGenre.get(acteur);
                for (String  genreItem : item.getGenre()){
                  if(!acteurGenre.contains(genreItem)){
                    acteurGenre.add(genreItem);
                  }
                }
                acteursFrequentsGenre.put(acteur, acteurGenre);
              }
            } 
          }
        }
      }
    }
    return acteursFrequentsGenre;
  }

  public ArrayList<Item> getFilmsPreferes(Preferences preferences) {
    ArrayList<Item> filmsPreferes = new ArrayList<>();

    for (Item item : items) {
      ArrayList<String> listGenre = item.getGenre();
      Double nbEtoiles = item.getNbEtoiles();
      Integer duree = item.getDuree();
      Integer annee = item.getAneeDeSortie();
      //on cherche des films qui correspondent aux preferences 
      for (String genre : listGenre) {
        for (String genrePrefere : preferences.getGenres()) {
          if (genre.equalsIgnoreCase(genrePrefere)) {  
              if (preferences.getDureemin() == null || duree == null || (duree != null && preferences.getDureemin() != null && duree >= preferences.getDureemin())) {
                if (preferences.getDureemax() == null || duree == null || (duree != null && preferences.getDureemax() != null && duree <= preferences.getDureemax() )) {
                  if (preferences.getNbetoilesp() == null || nbEtoiles == null || (nbEtoiles != null && preferences.getNbetoilesp() != null && preferences.getNbetoilesp() <= nbEtoiles)) {
                    if (preferences.getAnneemin() == null || annee == null || (annee != null && preferences.getAnneemin() != null && annee >= preferences.getAnneemin())) {
                      if (preferences.getAnneemax() == null || annee == null || (annee != null && preferences.getAnneemax() != null && annee <= preferences.getAnneemax())) {
                        if(!(nbEtoiles == null && duree == null && annee == null) && !filmsPreferes.contains(item)){
                            filmsPreferes.add(item);
                        }
                      }
                    }
                  }
                }
              }
            
          }
        }
      }
    }
    return filmsPreferes;
  }

  private Preferences[] getCouple () {
    //on cree un tableau des preferences de deux personnes pour apres trouver des films qui sont les meilleurs pour leur date
    Preferences personneA = new Preferences(new String[]{"horror", "comedy"}, 60, 180, 6.0, 2000, null);
    Preferences personneB = new Preferences(new String[]{"Drama", "Fantasy"}, 100, null, 7.0, null, 2010);

    Preferences[] couple = {personneA, personneB};

    return couple;
  }

  public ArrayList<String> getBestMovieForADate() {

    ArrayList<String> BestMovie = new ArrayList<>();

    Preferences[] couple = getCouple();
    Preferences personneA = couple[0];
    Preferences personneB = couple[1];

    ArrayList<Item> listeA = getFilmsPreferes(personneA);
    ArrayList<Item> listeB = getFilmsPreferes(personneB);

    //on ajoute des films qui s'apparaissent dans la liste des films preferes de chaque personne
    for(Item a : listeA){
      for (Item b : listeB){
        if(a.equals(b)){
          BestMovie.add(a.getTitre());
        }
      }
    }

    //s'il n'y a pas des memes films, on agrandissent des preferences 
    if(BestMovie.isEmpty()){
      // on actualise les preferences de personneA
      if(personneA.getDureemin() != null && personneA.getDureemax() != null){
        personneA.setDureemin(personneA.getDureemin() - 10);
        personneA.setDureemax(personneA.getDureemax() + 10);
      }
      if(personneA.getNbetoilesp() != null){
        personneA.setNbetoilesp(personneA.getNbetoilesp() - 1);
      }
      if(personneA.getAnneemin() != null && personneA.getAnneemax() != null){
        personneA.setDureemin(personneA.getAnneemin() - 5);
        personneA.setDureemax(personneA.getAnneemax() + 5);
      }

      // on actualise les preferences de personneB
      if(personneB.getDureemin() != null && personneB.getDureemax() != null){
        personneB.setDureemin(personneB.getDureemin() - 10);
        personneB.setDureemax(personneB.getDureemax() + 10);
      }
      if(personneB.getNbetoilesp() != null){
        personneB.setNbetoilesp(personneB.getNbetoilesp() - 1);
      }
      if(personneB.getAnneemin() != null && personneB.getAnneemax() != null){
        personneB.setDureemin(personneB.getAnneemin() - 5);
        personneB.setDureemax(personneB.getAnneemax() + 5);
      }

      listeA = getFilmsPreferes(personneA);
      listeB = getFilmsPreferes(personneB);
      for(Item a : listeA){
        for (Item b : listeB){
          if(a.equals(b)){
            BestMovie.add(a.getTitre());
          }
        }
      }
    }
    
    if(BestMovie.isEmpty()){
      BestMovie.add("Il est mieux de choisir l'autre activité pour votre date :/ ");
    }

    return BestMovie;
  } 

}
