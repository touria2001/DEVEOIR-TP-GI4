package com.ensa.gi4.controller;

import com.ensa.gi4.listeners.ApplicationPublisher;
import com.ensa.gi4.listeners.EventType;
import com.ensa.gi4.listeners.MyEvent;
import com.ensa.gi4.modele.Chaise;
import com.ensa.gi4.modele.Livre;
import com.ensa.gi4.modele.Materiel;
import com.ensa.gi4.modele.Personne;
import com.ensa.gi4.service.api.GestionMaterielService;
import com.ensa.gi4.service.api.GestionPersonneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component("controllerPricipal")
public class GestionMaterielController {
	String donnee1, donnee2, donnee3;
	int num1, num2, num3;
	Scanner scanner = new Scanner(System.in);

	@Autowired

	ApplicationPublisher publisher;
	@Autowired
	GestionPersonneService gestionPersonneService;
	@Autowired
	@Qualifier("materielService")
	GestionMaterielService gestionMaterielService;

	private void afficherMenuAdmin() {

		System.out.println("------------------admin-----------------");
		System.out.println("pour --  lister  ---- le materiele saisir        ----------------------> 1 ");
		System.out.println("pour --  chercher  -- un materiele saisir        ----------------------> 2 ");
		System.out.println("pour --  allouer  --- un materiel saisir         ----------------------> 3 ");
		System.out.println("pour --  rendre  ---- un matereil saisir         ----------------------> 4 ");
		System.out.println("pour --  afficher  -- le mat�riel allou�s saisir ----------------------> 5 ");
		System.out.println("pour --  ajouter   -- un mat�riel  saisir        ----------------------> 6 ");
		System.out.println("pour --  supprimer -- un mat�riel  saisir        ----------------------> 7 ");
		System.out.println("pour --  modifier  -- un mat�riel  saisir        ----------------------> 8 ");
		System.out.println("pour --  marquer   -- votre mat�riel indisponible saisir --------------> 9 ");
		System.out.println("pour --  afficher  -- les mat�riels allou�s par les utilisateurs saisir> 10 ");
		System.out.println("pour --  cr�er     -- un compte utilisateur saisir --------------------> 11 ");


		System.out.println("-----------------------------------");
	}

	private void afficherMenuEmploye() {
		System.out.println("---------------------employ�-------------");
		System.out.println("pour --  lister  ---- le materiele saisir        -------->  1 ");
		System.out.println("pour --  chercher  -- un materiele saisir        -------->  2 ");
		System.out.println("pour --  allouer  --- un materiel saisir         -------->  3 ");
		System.out.println("pour --  rendre  ---- un matereil saisir         -------->  4 ");
		System.out.println("pour --  afficher  -- le mat�riel allou�s saisir -------->  5 ");
		System.out.println("----------------------------------------");

	}

	public void afficherMenu() {

		if (authentification() != null) {
			if (gestionPersonneService.determinerRole().equals("admin")) {

				// ************************ADMIN***************************

				while (true) {
					afficherMenuAdmin();

					String choix = scanner.next();

					if (choix.equals("1")) {
						listerMateriel();
					} else if (choix.equals("2")) {
						chercherMateriel();
					} else if (choix.equals("3")) {
						allouerMateriel();
					} else if (choix.equals("4")) {
						rendreMateriel();

					} else if (choix.equals("5")) {
						listerMaterielAlloue();
					} else if (choix.equals("6")) {
						ajouterMateriel();

					} else if (choix.equals("7")) {
						supprimerMateriel();

					} else if (choix.equals("8")) {
						modifierMateriel();

					} else if (choix.equals("9")) {
						marquerMaterielIndisponible();

					} else if (choix.equals("10")) {
						afficherMaterielAllouerParUtilisateur();

					}else if (choix.equals("11")) {
						creerCompteAdmin();

					}

					else {
						System.out.println("choix invalid");
					}

				}

			} else {

				// ******************************employe**********************

				while (true) {
					afficherMenuEmploye();
					String choix = scanner.next();
					if (choix.equals("1")) {
						listerMateriel();
					} else if (choix.equals("2")) {

						chercherMateriel();
					} else if (choix.equals("3")) {
						allouerMateriel();
					} else if (choix.equals("4")) {
						rendreMateriel();

					} else if (choix.equals("5")) {
						listerMaterielAlloue();
					} else {
						System.out.println("choix invalid");
					}

				}

			}

		} else {
			System.out.println(" saisir 0 pour sortir de l'application");
			System.out.println(" saisir 1 pour cr�er un compte ");
			System.out.println(" saisir n'import quel caract�re pour r�esayer � nouveau");

			String choix = scanner.next();
			if (choix.equals("0")) {
				sortirDeLApplication();
			} else if (choix.equals("1")) {
				creerCompteEmploye();
				afficherMenu();
			} else {

				afficherMenu();
			}
		}

	}

	private void listerMateriel() {
		gestionMaterielService.listerMateriel();
	}

	private void ajouterMateriel() {
		System.out.println("pour ajouter un livre saisir 1 ");
		System.out.println("pour ajouter une chaise saisir 2 ");
		donnee1 = scanner.next();
		while (!donnee1.equals("1") && !donnee1.equals("2")) {
			System.out.println("choix invalid veuillez saisir 1 pour livre ou bien 2 pour chaise");
			donnee1 = scanner.next();
		}
		if (donnee1.equals("1")) {
			System.out.println("saisir le code du livre");
			donnee2 = scanner.next();
			Materiel materiel = new Livre();
			materiel.setCode(donnee2);
			materiel.setName(donnee1);
			gestionMaterielService.ajouterNouveauMateriel(materiel);
			publisher.publish(new MyEvent<>(materiel, EventType.ADD));

		}
		if (donnee1.equals("2")) {
			System.out.println("saisir le code du chaise");
			donnee2 = scanner.next();
			Materiel materiel = new Chaise();
			materiel.setCode(donnee2);
			materiel.setName(donnee1);
			gestionMaterielService.ajouterNouveauMateriel(materiel);
			publisher.publish(new MyEvent<>(materiel, EventType.ADD));

		}
	}

	private Personne authentification() {
		System.out.println("----------------bonjour----------------");
		System.out.println(" saisir votre nom ");

		donnee1 = scanner.next();
		System.out.println(" saisir votre mot de passe ");
		donnee2 = scanner.next();
		return gestionPersonneService.connecter(donnee1, donnee2);
	}

	private void creerCompteEmploye() {
		System.out.println("---------------cr�ation du compte type employ�(e)--------------");
		do {
			System.out.println("saisir votre nom d'utilisateur ");
			donnee1 = scanner.next();
			System.out.println("saisir votre mot de passe ");
			donnee2 = scanner.next();
		} while (!gestionPersonneService.creerCompte(donnee1, donnee2, "employe"));

	}

	private void creerCompteAdmin() {
		System.out.println("---------------cr�ation du compte--------------");
		System.out.println("saisir le nom d'utilisateur ");
		donnee1 = scanner.next();
		System.out.println("saisir le mot de passe ");
		donnee2 = scanner.next();
		System.out.println("pour �tre un employ� saisir 1 ");
		System.out.println("pour �tre un admin saisir 2 ");
		donnee3 = scanner.next();
		while (!donnee3.equals("1") && !donnee3.equals("2")) {
			System.out.println("choix invalid, veuillez saisir 1 pour �tre employ�, ou 2 pour admin ");
			donnee3 = scanner.next();
		}
		if (donnee3.equals("1")) {
			donnee3 = "employe";
		} else if (donnee3.equals("2")) {
			donnee3 = "admin";
		}
		gestionPersonneService.creerCompte(donnee1, donnee2, donnee3);
	}

	private void supprimerMateriel() {
		System.out.println("Veuillez saisir l'id du mat�riel � supprimer ");
		listerMateriel();
		boolean verification = false;
		while (!verification) {
			donnee1 = scanner.next();

			try {
				num1 = Integer.parseInt(donnee1);
				verification = true;
			} catch (Exception e) {
				System.out.println("Veuillez  saisir un num�ro id convenable  : ");

			}
		}

		gestionMaterielService.supprimerMateriel(num1);
	}

	private void modifierMateriel() {
		System.out.println("Veuillez saisir l'id du mat�riel � modifier ");
		listerMateriel();

		boolean verification = false;
		while (!verification) {
			donnee1 = scanner.next();

			try {
				num1 = Integer.parseInt(donnee1);
				verification = true;
			} catch (Exception e) {
				System.out.println("Veuillez  saisir un num�ro id convenable  : ");

			}
		}

		System.out.println("Veuillez saisir le nouveau nom du mat�riel soit Livre ou Chaise ");
		donnee1 = scanner.next();
		while (!donnee1.equals("Livre") && !donnee1.equals("Chaise")) {
			System.out.println("Veuillez saisir soit Livre soit Chaise");
			donnee1 = scanner.next();
		}
		System.out.println("Veuillez saisir le nouveau code du mat�riel ");
		donnee2 = scanner.next();
		gestionMaterielService.modifierMateriel(num1, donnee1, donnee2);
	}

	private void chercherMateriel() {
		boolean verification = false;
		System.out.println(" saisir id : ");
		donnee1 = scanner.next();
		while (!verification) {

			try {
				Long.parseLong(donnee1);
				verification = true;
			} catch (Exception e) {
				System.out.println("Veuillez  saisir un num�ro id convenable  : ");
				donnee1 = scanner.next();
			}
		}

		gestionMaterielService.findMateriel(Long.parseLong(donnee1));
	}

	private void allouerMateriel() {
		do {
			System.out.println("Pour allouer un livre saisir 1 ");
			System.out.println("Pour allouer un livre saisir 2 ");
			donnee1 = scanner.next();
		} while (!donnee1.equals("1") && !donnee1.equals("2"));
		System.out.println("saisir la duree d'allocation ");
		donnee2 = scanner.next();
		if (donnee1.equals("1")) {
			gestionMaterielService.allouerMateriel("Livre", donnee2);
		} else {
			gestionMaterielService.allouerMateriel("Chaise", donnee2);
		}

	}

	private void rendreMateriel() {
		if(!gestionMaterielService.listerMaterielAlloue()) {
			
		}else {
		boolean verification = false;
		System.out.println("Saisir l'id du mat�riel � rendre");
		while (!verification) {
			donnee1 = scanner.next();
			try {
				num1 = Integer.parseInt(donnee1);
				verification = true;
			} catch (Exception e) {
				System.out.println("Veuillez  saisir un num�ro id convenable  : ");

			}
		}

		gestionMaterielService.rendreMateriel(num1);
	}}

	private void listerMaterielAlloue() {
		gestionMaterielService.listerMaterielAlloue();
	}

	private void marquerMaterielIndisponible() {
		boolean verification = false;
		System.out.println("Veuillez saisir l'id du mat�riel � marquer indisponible");
		while (!verification) {
			donnee1 = scanner.next();
			try {
				num1 = Integer.parseInt(donnee1);
				verification = true;
			} catch (Exception e) {
				System.out.println("Veuillez  saisir un num�ro id convenable  : ");

			}}
		
		gestionMaterielService.marquerMaterielIndisponible(num1);
	}

	private void afficherMaterielAllouerParUtilisateur() {
		gestionMaterielService.afficherMaterielAllouerParUtilisateur();
	}

	private void sortirDeLApplication() {
		System.out.print("Merci pour votre visite");
		System.exit(0);
	}

}
