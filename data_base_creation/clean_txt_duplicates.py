import os

# Configuration des fichiers
INPUT_FILE = "wiki-100k.txt"
OUTPUT_FILE = "wiki-100k-clean.txt"

def clean_text_file():
    """
    Lit le fichier INPUT_FILE, supprime les doublons (insensible à la casse),
    et écrit le résultat dans OUTPUT_FILE.
    Les lignes commençant par '#' sont préservées telles quelles.
    """
    if not os.path.exists(INPUT_FILE):
        print(f"Erreur : Le fichier '{INPUT_FILE}' n'existe pas.")
        return

    print(f"Traitement du fichier '{INPUT_FILE}'...")
    
    seen_words = set()
    lines_kept = 0
    lines_removed = 0

    try:
        with open(INPUT_FILE, 'r', encoding='utf-8') as f_in, \
             open(OUTPUT_FILE, 'w', encoding='utf-8') as f_out:
            
            for line in f_in:
                stripped_line = line.strip()
                
                # Ignorer les lignes vides
                if not stripped_line:
                    continue

                # Conserver les commentaires
                if stripped_line.startswith('#'):
                    f_out.write(line)
                    continue

                # Supprimer l'apostrophe initiale si elle existe (ex: 'Give -> Give)
                if stripped_line.startswith("'") or stripped_line.startswith("’") or stripped_line.startswith(".") or stripped_line.startswith(","):
                    stripped_line = stripped_line[1:]
                    # Mettre à jour la ligne à écrire (on garde le saut de ligne original si possible, sinon on en ajoute un)
                    line = stripped_line + '\n'
                
                # Supprimer l'apostrophe finale si elle existe (ex: Give' -> Give)
                if stripped_line.endswith("'") or stripped_line.endswith("’") or stripped_line.endswith(".") or stripped_line.endswith(","):
                    stripped_line = stripped_line[:-1]
                    # Mettre à jour la ligne à écrire (on garde le saut de ligne original si possible, sinon on en ajoute un)
                    line = stripped_line + '\n'

                # Supprimer les lignes comportant un caractère non anglais (ex: café, naïve, etc.)
                if any(char.isalpha() and char not in "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" for char in stripped_line):
                    continue
                
                # supprimer les lignes qui comporte un . ou '
                if '.' in stripped_line or "'" in stripped_line:
                    continue

                # Gestion des doublons (insensible à la casse)
                word_lower = stripped_line.lower()
                
                if word_lower in seen_words:
                    lines_removed += 1
                else:
                    seen_words.add(word_lower)
                    f_out.write(line)
                    lines_kept += 1

        print(f"Traitement terminé.")
        print(f"Lignes conservées (hors commentaires) : {lines_kept}")
        print(f"Doublons supprimés : {lines_removed}")
        print(f"Fichier nettoyé créé : '{OUTPUT_FILE}'")
        
        # Optionnel : Remplacer l'original
        # os.replace(OUTPUT_FILE, INPUT_FILE)
        # print(f"Le fichier original '{INPUT_FILE}' a été remplacé.")

    except Exception as e:
        print(f"Une erreur est survenue : {e}")

if __name__ == "__main__":
    clean_text_file()
