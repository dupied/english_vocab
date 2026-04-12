#!/usr/bin/env python3
"""
Process wiki-10k-creation-ai.txt by keeping the first 10k English words,
adding French translations and example sentences.
Requires: pip install wordfreq deep-translator
"""
from __future__ import annotations

import argparse
import logging
from pathlib import Path
from typing import Iterable

from deep_translator import GoogleTranslator
from wordfreq import zipf_frequency

INPUT_FILE = Path(__file__).parent / "wiki-10k-creation-ai.txt"
OUTPUT_FILE = INPUT_FILE  # overwrite in place as requested
MAX_ENTRIES = 10_000
BATCH_SIZE = 50

logging.basicConfig(level=logging.INFO, format="[%(levelname)s] %(message)s")
log = logging.getLogger(__name__)

translator = GoogleTranslator(source="en", target="fr")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Enrich English word list with French translations and sample sentences.")
    parser.add_argument("--max", dest="max_entries", type=int, default=MAX_ENTRIES, help="Maximum number of English entries to keep (default: 10000)")
    parser.add_argument("--input", dest="input_file", type=Path, default=INPUT_FILE, help="Input wordlist path")
    parser.add_argument("--output", dest="output_file", type=Path, default=OUTPUT_FILE, help="Output path (overwrites by default)")
    parser.add_argument("--skip-translation", action="store_true", help="Skip translation step (useful for dry runs)")
    return parser.parse_args()


def is_english_word(token: str) -> bool:
    """Return True if token looks like an English word and not a proper noun."""
    word = token.strip()
    if not word:
        return False
    if not word.isascii():
        return False
    if word[0].isupper() and word != "I":
        return False
    normalized = word.lower()
    freq_en = zipf_frequency(normalized, "en")
    freq_fr = zipf_frequency(normalized, "fr")
    return freq_en >= 1.5 and freq_en >= freq_fr


def batch_translate(words: list[str], *, enabled: bool) -> list[str]:
    if not enabled:
        return ["" for _ in words]
    translations: list[str] = []
    for start in range(0, len(words), BATCH_SIZE):
        chunk = words[start : start + BATCH_SIZE]
        try:
            translated = translator.translate_batch(chunk)
        except Exception as exc:  # pragma: no cover
            log.warning("Translation batch failed for items %d-%d: %s", start, start + len(chunk), exc)
            translated = ["" for _ in chunk]
        translations.extend(translated)
    return translations


def build_sentence(word: str) -> str:
    normalized = word.strip()
    return f"{normalized.capitalize()} appears in this example sentence for clarity."


def process(lines: Iterable[str], *, max_entries: int, translate_enabled: bool) -> list[str]:
    tokens: list[str] = []
    for raw in lines:
        token = raw.strip()
        if not is_english_word(token):
            continue
        tokens.append(token)
        if len(tokens) >= max_entries:
            break
    translations = batch_translate(tokens, enabled=translate_enabled)
    output = []
    for idx, (token, french) in enumerate(zip(tokens, translations), start=1):
        sentence = build_sentence(token)
        output.append(f"{token},{french},{sentence}")
        if idx % 500 == 0:
            log.info("Processed %d entries", idx)
    return output


def main() -> None:
    args = parse_args()
    raw_lines = args.input_file.read_text(encoding="utf-8").splitlines()
    log.info("Loaded %d lines from %s", len(raw_lines), args.input_file)
    processed = process(raw_lines, max_entries=args.max_entries, translate_enabled=not args.skip_translation)
    args.output_file.write_text("\n".join(processed), encoding="utf-8")
    log.info("Wrote %d lines to %s", len(processed), args.output_file)


if __name__ == "__main__":
    main()
