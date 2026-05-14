#!/usr/bin/env python3
"""Crée une base SQLite à partir de last_result.txt.

Format attendu par ligne: word_en/word_fr/example_en
Si un champ manque, il devient vide. La colonne `note` vaut 1.
"""
from pathlib import Path
import sqlite3
import sys


def parse_line(line):
    s = line.strip()
    if not s:
        return None
    first = s.find('/')
    if first == -1:
        return (s, '', '', 1)
    second = s.find('/', first + 1)
    if second == -1:
        word_en = s[:first].strip()
        word_fr = s[first + 1 :].strip()
        return (word_en, word_fr, '', 1)
    word_en = s[:first].strip()
    word_fr = s[first + 1 : second].strip()
    example_en = s[second + 1 :].strip()
    return (word_en, word_fr, example_en, 1)


def create_db(db_path: Path):
    conn = sqlite3.connect(str(db_path))
    cur = conn.cursor()
    cur.execute(
        """
        CREATE TABLE IF NOT EXISTS words (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            word_en TEXT,
            word_fr TEXT,
            example_en TEXT,
            note INTEGER
        )
        """
    )
    conn.commit()
    return conn


def main(input_file: Path, db_file: Path):
    if not input_file.exists():
        print(f"Fichier introuvable: {input_file}")
        sys.exit(1)

    conn = create_db(db_file)
    cur = conn.cursor()
    inserted = 0
    with input_file.open(encoding='utf-8') as fh:
        for line in fh:
            parsed = parse_line(line)
            if parsed is None:
                continue
            word_en, word_fr, example_en, note = parsed
            cur.execute(
                "INSERT INTO words (word_en, word_fr, example_en, note) VALUES (?, ?, ?, ?)",
                (word_en, word_fr, example_en, note),
            )
            inserted += 1
    conn.commit()
    conn.close()
    print(f"Inserted {inserted} rows into {db_file}")


if __name__ == '__main__':
    script_dir = Path(__file__).parent
    default_input = script_dir / 'last_result.txt'
    default_db = script_dir / 'english_vocab.db'

    input_path = Path(sys.argv[1]) if len(sys.argv) > 1 else default_input
    db_path = Path(sys.argv[2]) if len(sys.argv) > 2 else default_db

    main(input_path, db_path)
