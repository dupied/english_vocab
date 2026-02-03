import csv
import requests
import sqlite3
import time

# Configuration de la base de données SQLite
DB_FILE = "english_vocab.db"

# Configuration des fichiers
CSV_FILE_PATH = 'word-freq-top5000.csv'

def get_tatoeba_sentence(word):
    """Récupère une phrase d'exemple en anglais contenant le mot depuis Tatoeba."""
    try:
        url = "https://api.tatoeba.org/unstable/sentences"
        params = {
            'lang': 'eng',
            'q': word,
            'sort': 'relevance',
            'limit': 1
        }
        response = requests.get(url, params=params, timeout=5)
        response.raise_for_status()
        data = response.json()
        
        if data and 'data' in data and len(data['data']) > 0:
            return data['data'][0]['text']
        return None
    except Exception as e:
        print(f"Erreur Tatoeba pour '{word}': {e}")
        return None

def get_french_translation(word):
    """Récupère la traduction française du mot depuis MyMemory."""
    try:
        url = "https://api.mymemory.translated.net/get"
        params = {
            'q': word,
            'langpair': 'en|fr'
        }
        response = requests.get(url, params=params, timeout=5)
        response.raise_for_status()
        data = response.json()
        
        if 'responseData' in data and 'translatedText' in data['responseData']:
            return data['responseData']['translatedText']
        return None
    except Exception as e:
        print(f"Erreur MyMemory pour '{word}': {e}")
        return None

def main():
    print("Démarrage du script d'importation...")
    
    # Connexion à la base de données
    try:
        connection = sqlite3.connect(DB_FILE)
        cursor = connection.cursor()
        print(f"Connexion à la base de données SQLite '{DB_FILE}' réussie.")

        # Création de la table si elle n'existe pas
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS WORDS (
            id            INTEGER PRIMARY KEY AUTOINCREMENT,
            word_en       TEXT NOT NULL,
            word_fr       TEXT NOT NULL,
            example_en    TEXT,
            note          INTEGER CHECK (note IN (1, 2, 3))
        );
        """
        cursor.execute(create_table_sql)
        connection.commit()

    except sqlite3.Error as e:
        print(f"Erreur de connexion SQLite: {e}")
        return

    # Lecture du CSV
    try:
        with open(CSV_FILE_PATH, mode='r', encoding='utf-8') as csvfile:
            reader = csv.DictReader(csvfile)
            
            # Préparation de la requête d'insertion
            insert_sql = """
                INSERT INTO WORDS (word_en, word_fr, example_en, note)
                VALUES (:word_en, :word_fr, :example_en, :note)
            """
            
            count = 0
            for row in reader:
                word_en = row['Word']
                
                # On ignore les mots vides ou trop courts si nécessaire
                if not word_en:
                    continue

                print(f"Traitement du mot: {word_en}...")
                
                # 1. Récupérer l'exemple
                example_en = get_tatoeba_sentence(word_en)
                
                # 2. Récupérer la traduction
                word_fr = get_french_translation(word_en)
                
                # Si la traduction échoue, on peut mettre une valeur par défaut ou skipper
                if not word_fr:
                    word_fr = "[Traduction non trouvée]"
                
                # Nettoyage sommaire
                if example_en and len(example_en) > 500:
                    example_en = example_en[:497] + "..."
                
                # 3. Insertion dans SQLite
                try:
                    cursor.execute(insert_sql, {
                        'word_en': word_en,
                        'word_fr': word_fr,
                        'example_en': example_en,
                        'note': 1
                    })
                    connection.commit()
                    count += 1
                    print(f" -> Inséré (Total: {count})")
                except sqlite3.Error as e:
                    print(f" -> Erreur d'insertion pour '{word_en}': {e}")
                
                # Pause pour respecter les limites des API (Rate Limiting)
                # MyMemory: 1000 mots/jour en gratuit, Tatoeba: pas de limite stricte mais être poli
                time.sleep(0.5)

    except FileNotFoundError:
        print(f"Erreur: Le fichier {CSV_FILE_PATH} est introuvable.")
    except Exception as e:
        print(f"Une erreur inattendue est survenue: {e}")
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'connection' in locals():
            connection.close()
            print("Connexion fermée.")

if __name__ == "__main__":
    main()
