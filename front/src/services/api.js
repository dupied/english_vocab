// Simulation d'un délai réseau
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// Données fictives initiales
let mockWords = [
  { id: 1, word_en: "apple", word_fr: "pomme", example_en: "I eat an apple every day.", note: 1 },
  { id: 2, word_en: "computer", word_fr: "ordinateur", example_en: "My computer is fast.", note: 2 },
  { id: 3, word_en: "house", word_fr: "maison", example_en: "They live in a big house.", note: 1 },
  { id: 4, word_en: "to run", word_fr: "courir", example_en: "I like to run in the park.", note: 3 },
  { id: 5, word_en: "freedom", word_fr: "liberté", example_en: "Freedom is a human right.", note: 1 },
];

export const getRandomWord = async () => {
  await delay(500);
  const randomIndex = Math.floor(Math.random() * mockWords.length);
  return { ...mockWords[randomIndex] };
};

export const getWords = async (noteFilter = null) => {
  await delay(500);
  if (noteFilter) {
    return mockWords.filter(w => w.note === parseInt(noteFilter));
  }
  return [...mockWords];
};

export const updateWordNote = async (id, newNote) => {
  await delay(300);
  mockWords = mockWords.map(w => {
    if (w.id === id) {
      return { ...w, note: newNote };
    }
    return w;
  });
  return mockWords.find(w => w.id === id);
};
