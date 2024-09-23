package fr.pantheonsorbonne.ufr27.miashs.poo;

import java.lang.String;
import java.util.ArrayList;

public final class ItemsScrapper {
  ArrayList<Item> parseSource(String pageSource) {
    ArrayList<Item> itemList = new ArrayList<>();

    // On compte le nombre de films
    int countFilms = 0;
    int countTitres = pageSource.indexOf("lister-item-header");
    while (countTitres != -1){
      countTitres = pageSource.indexOf("lister-item-header", countTitres + 1);
      countFilms += 1;
    }

    // On initialise une delimitation entre chaque film
    int debutItem = pageSource.indexOf("lister-item-header");
    int finItem = pageSource.indexOf("lister-item-header", debutItem + 1); 

    while (debutItem != -1 && finItem != -1) {
      Item item = new Item();

// <h3 class="lister-item-header"><span class="lister-item-index unbold text-primary">1.</span> <a href="/title/tt0343660/?ref_=ttls_li_tt">Amour &amp; amnÃ©sie</a> <span class="lister-item-year text-muted unbold">(2004)</span></h3>
// <p class="text-muted text-small"><span class="certificate">Tous publics</span> <span class="ghost">|</span> <span class="runtime">60 min</span> <span class="ghost">|</span> <span class="genre"> Comedy, Drama, Family </span></p>
// <span class="ipl-rating-star__star"> </span> <span class="ipl-rating-star__rating">5.5</span>
      
      String titre;
      int classTitre = pageSource.indexOf("lister-item-header", debutItem);
      int lienAvantTitre = pageSource.indexOf("<a", classTitre);
      int indDebutTitre = pageSource.indexOf('>', lienAvantTitre);
      titre = pageSource.substring(indDebutTitre+1, pageSource.indexOf("</a>", indDebutTitre));

      Integer anneeDeSortie = null;
      int indClassAnnee = pageSource.indexOf("lister-item-year text-muted unbold", debutItem);
      int indFinAnnee = pageSource.indexOf("</span>", indClassAnnee);
      String stringAnnee = pageSource.substring(indClassAnnee+("lister-item-year text-muted unbold").length()+3, indFinAnnee);
      StringBuilder nombre = new StringBuilder();
      for (int i = 0; i<stringAnnee.length(); i++){
        if (Character.isDigit(stringAnnee.charAt(i))){
          nombre.append(stringAnnee.charAt(i));
        }
        if (nombre.length() == 4){
          break;
        }
      }
      anneeDeSortie = Integer.parseInt(nombre.toString());
      
      //Integer anneeDeSortie = null;
      //int indAnneeSpan = pageSource.indexOf("lister-item-year text-muted unbold", debutItem);
      //int indDebutAnnee = pageSource.indexOf("(", indAnneeSpan);
      //anneeDeSortie = Integer.parseInt(pageSource.substring(indDebutAnnee + 1, indDebutAnnee + 5));

      Integer duree = null;
      int indClassDuree = pageSource.indexOf("runtime", debutItem);
      if (indClassDuree < finItem && indClassDuree != -1){
        int indDebutDuree = pageSource.indexOf('>', indClassDuree);
        duree = Integer.parseInt(pageSource.substring(indDebutDuree+1, pageSource.indexOf("min", indDebutDuree)-1));
      }

      ArrayList<String> genre = new ArrayList<>();
      int indClassGenre = pageSource.indexOf("genre", debutItem);
      int indDebutGenre = pageSource.indexOf('>', indClassGenre);
      int indFinGenre = pageSource.indexOf("</span>",indDebutGenre);
      String stringGenre = pageSource.substring(indDebutGenre+1, indFinGenre);
      String[] listeGenre = stringGenre.split(",");
      for (String word: listeGenre){
        genre.add(word.trim()); //trim efface les espaces 
      }

      Double nbEtoiles = null; 
      int indClassEtoiles = pageSource.indexOf("ipl-rating-star__rating", debutItem);
      if (indClassEtoiles < finItem && indClassEtoiles != -1){ // on fait cela car sinon on va chercher l'index du prochain film
        int indDebutEtoiles = pageSource.indexOf('>', indClassEtoiles);
        String stringNbEtoiles = pageSource.substring(indDebutEtoiles+1, pageSource.indexOf("</span>", indDebutEtoiles)); 
        nbEtoiles = Double.parseDouble(stringNbEtoiles);
      }

// <p class="text-muted text-small">Director: <a href="/name/nm1164861/?ref_=ttls_li_dr_0">Seth Gordon</a> <span class="ghost">|</span> Stars: <a href="/name/nm0425005/?ref_=ttls_li_st_0">Dwayne Johnson</a>, <a href="/name/nm1374980/?ref_=ttls_li_st_1">Zac Efron</a>, <a href="/name/nm1275259/?ref_=ttls_li_st_2">Alexandra Daddario</a>, <a href="/name/nm1231899/?ref_=ttls_li_st_3">Priyanka Chopra Jonas</a></p>
      String real = "realisateur non connu";
      int indTexteDirector = pageSource.indexOf("Director", debutItem);
      if (indTexteDirector < finItem && indTexteDirector != -1){
        int indDebutReal = pageSource.indexOf('>', indTexteDirector);
        real = pageSource.substring(indDebutReal+1, pageSource.indexOf("</a>", indDebutReal));
      }

      ArrayList<String> acteurs = new ArrayList<>();
      int indTexteActeurs = pageSource.indexOf("Stars", debutItem);
      int indDebutActeurs = pageSource.indexOf('>', indTexteActeurs);
      int indFinActeurs = pageSource.indexOf("</p>", indDebutActeurs);
      while (indDebutActeurs < indFinActeurs && indTexteActeurs != -1){
        int indLienAvantActeur = pageSource.indexOf("</a>", indDebutActeurs);
        acteurs.add(pageSource.substring(indDebutActeurs+1, indLienAvantActeur));
        indDebutActeurs = pageSource.indexOf('>', indLienAvantActeur+4); 
      }

// <p class="text-muted text-small"><span class="text-muted">Votes:</span> <span name="nv" data-value="198394">198,394</span> <span class="ghost">|</span> <span class="text-muted">Gross:</span> <span name="nv" data-value="58,060,186">$58.06M</span></p>
      Integer nbVotes = null;
      int indTexteVotes = pageSource.indexOf("Votes", debutItem);
      String data_value = "data-value";
      if (indTexteVotes < finItem && indTexteVotes != -1){
        int indDebutVotes = pageSource.indexOf(data_value, indTexteVotes);
        nbVotes = Integer.parseInt(pageSource.substring(indDebutVotes+data_value.length()+2, pageSource.indexOf(">", indDebutVotes)-1));
      }

      Integer gross = null; 
      int indTexteGross = pageSource.indexOf("Gross", debutItem);
      int indDebutGross = pageSource.indexOf(data_value, indTexteGross);
      if (indDebutGross < finItem && indTexteGross != -1){
        String grossVirgule = pageSource.substring(indDebutGross+data_value.length()+2, pageSource.indexOf(">", indDebutGross)-1);
        StringBuilder grossSansVirgule = new StringBuilder ();
        for (int index = 0; index < grossVirgule.length(); index++){
          char chiffre = grossVirgule.charAt(index);
          if (chiffre != ','){
            grossSansVirgule.append(chiffre);
          }
        }
        grossVirgule = grossSansVirgule.toString();
        gross = Integer.parseInt(grossVirgule);
      }

      item.setTitre(titre);
      item.setGenre(genre);
      item.setDuree(duree);
      item.setAneeDeSortie(anneeDeSortie);
      item.setReal(real);
      item.setActeurs(acteurs);
      item.setNbVotes(nbVotes);
      item.setGross(gross);
      item.setNbEtoiles(nbEtoiles);
      itemList.add(item);
          
      debutItem = pageSource.indexOf("lister-item-header", debutItem + 1);
      finItem = pageSource.indexOf("lister-item-header", debutItem + 1);

      // quand on arrive a la fin, finItem=-1 et la boucle s'arrete a l'avant le dernier film. Alors on doit changer la valeur de finItem
      if (itemList.size() == countFilms-1){
        finItem = pageSource.length();
      }
    }
    return itemList;
  }
}