package fr.pantheonsorbonne.ufr27.miashs.poo;

import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

public final class Item {
  private String titre;

  private ArrayList<String> genre;

  private Integer duree;

  private Integer aneeDeSortie;

  private String real;

  private ArrayList<String> acteurs;

  private Integer nbVotes;

  private Integer gross;

  private Double nbEtoiles;

  public String getTitre() {
    return this.titre;
  }

  public void setTitre(String titre) {
    this.titre=titre;
  }

  public ArrayList<String> getGenre() {
    return this.genre;
  }

  public void setGenre(ArrayList<String> genre) {
    this.genre=genre;
  }

  public Integer getDuree() {
    return this.duree;
  }

  public void setDuree(Integer duree) {
    this.duree=duree;
  }

  public Integer getAneeDeSortie() {
    return this.aneeDeSortie;
  }

  public void setAneeDeSortie(Integer aneeDeSortie) {
    this.aneeDeSortie=aneeDeSortie;
  }

  public String getReal() {
    return this.real;
  }

  public void setReal(String real) {
    this.real=real;
  }

  public ArrayList<String> getActeurs() {
    return this.acteurs;
  }

  public void setActeurs(ArrayList<String> acteurs) {
    this.acteurs=acteurs;
  }

  public Integer getNbVotes() {
    return this.nbVotes;
  }

  public void setNbVotes(Integer nbVotes) {
    this.nbVotes=nbVotes;
  }

  public Integer getGross() {
    return this.gross;
  }

  public void setGross(Integer gross) {
    this.gross=gross;
  }

  public Double getNbEtoiles() {
    return this.nbEtoiles;
  }

  public void setNbEtoiles(Double nbEtoiles) {
    this.nbEtoiles=nbEtoiles;
  }
}
