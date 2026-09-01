# Vocab

Vocab est une app web permetant d'améliorer son vocabulaire en anglais.

## Lancer le back

```bash
./gradlew bootRun
```

## Lancer le front 

```bash
npm run dev
```

## Architecture pour déployer l'application

- Déployer le front avec Vercel
- Déployer le back avec Render au début puis on passera sur VPS si il commence à y avoir pas mal d'utilisateurs
- BDD: Supabase
- Authentification: Supabase Auth avec des JWT

## Architecture de la base de données

- words:
    - id
    - word_en
    - word_fr
    - example_en
- users:
    - id
    - email
- user_word_progress:
    - user_id
    - word_id
    - note