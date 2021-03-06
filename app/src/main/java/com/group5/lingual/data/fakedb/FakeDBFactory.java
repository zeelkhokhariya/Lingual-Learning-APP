package com.group5.lingual.data.fakedb;

import com.group5.lingual.data.DBManager;
import com.group5.lingual.data.IFlashcardDB;
import com.group5.lingual.data.ILanguageDB;
import com.group5.lingual.dso.flashcards.FrenchPrepositionFlashcard;
import com.group5.lingual.dso.flashcards.SimpleFlashcard;
import com.group5.lingual.dso.languages.Language;
import com.group5.lingual.dso.lessons.ItemsListLessonModule;
import com.group5.lingual.dso.lessons.Lesson;
import com.group5.lingual.dso.flashcards.AlphabetFlashcard;
import com.group5.lingual.dso.flashcards.GrammarFlashcard;
import com.group5.lingual.dso.flashcards.IFlashcard;
import com.group5.lingual.dso.flashcards.LogogramFlashcard;
import com.group5.lingual.dso.flashcards.VocabularyFlashcard;
import com.group5.lingual.dso.lessons.ParagraphLessonModule;
import com.group5.lingual.dso.lessons.QuizLessonModule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//Creates fake databases with default contents
public class FakeDBFactory
{
    //Create a language database with the default languages
    public static ILanguageDB createFakeLanguageDB()
    {
        FakeLanguageDB languageDB = new FakeLanguageDB();

        languageDB.addLanguage(new Language(1, "French", "ic_language_french",
                "https://www.collinsdictionary.com/dictionary/french-english/" + Language.getURLPlaceholder(),
                "Pronunciations from Wiktionary.org"));
        languageDB.addLanguage(new Language(2,"German", "ic_language_german"));
        languageDB.addLanguage(new Language(3,"Hindi", "ic_language_hindi"));
        languageDB.addLanguage(new Language(4, "Japanese", "ic_language_japanese",
                "https://jisho.org/search/" + Language.getURLPlaceholder(),
                "Readings and definitions from Jisho.org\nPronunciations from Wiktionary.org\nReading sample from Wikipedia.org"));
        languageDB.addLanguage(new Language(5,"Spanish", "ic_language_spanish"));

        return languageDB;
    }

    //Create a flashcard database with the default flashcards
    public static IFlashcardDB createFakeFlashcardDB()
    {
        FakeFlashcardDB flashcardDB = new FakeFlashcardDB();
        List<IFlashcard> unlockedCards = new ArrayList<IFlashcard>();   //Temporary list of created cards to be unlocked immediately
        List<IFlashcard> lockedCards = new ArrayList<IFlashcard>();     //Temporary list of created cards to be locked at the start
        int nextFID;                                                    //Next ID to assign when creating cards
        int lID;                                                        //Language ID to assign when creating cards

        //*** START OF FRENCH CONTENT ***
        lID = 1;

        //UNLOCKED CARDS
        //Family vocabulary cards
        nextFID = 10000;
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La famille", "fa.mij", "Family"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La m??re", "m????", "Mother"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "Le p??re ", "p????(??)", "Father"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La s??ur  ", "s????", "Sister"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "Le fr??re ", "f??????", "Brother"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La fille", "fille", "Daughter"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "Le fils ", "??fi??s", "Son"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "Le petit ami", "p??.ti.t???a.mi", "Boyfriend"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La petite amie", "p??.ti.t???a.mi", "Girlfriend"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "Le mari", "ma.??i", "Husband"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La femme", "fam", "Wife"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "L???oncle", "????kl", "Uncle"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La tante ", "t????t", "Aunt"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La femme", "fam", "Woman"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "L???homme", "??m", "Man"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La fille", "fij", "Girl"));
        unlockedCards.add(new VocabularyFlashcard(nextFID++, lID, "La gar??on", "??a??.s????", "Boy"));

        //LOCKED CARDS
        //Preposition Cards
        nextFID = 11000;
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "aimer", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "entendre", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "pr??f??rer  ", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "aimer mieux", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "esp??rer", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "savoir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "aller", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "faire", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "sembler", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "compter", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "falloir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "venir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "croire", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "laisser", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "voir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "d??sirer", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "oser", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "vouloir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "devoir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "pouvoir", "aucun"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "avoir peur", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "dire", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "permettre", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "cesser", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "??crire", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "prier", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "craindre", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "emp??cher", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "promettre", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "d??cider", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "essayer", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "refuser", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "d??fendre", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "finir", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "regretter", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "demander", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "ordonner", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "remercier", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "se d??p??cher", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "oublier", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "t??cher", "de"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "aider", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "continuer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "inciter", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "s'amuser", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "se d??cider", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "inviter", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "apprendre", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "demander", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "recommencer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "arriver", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "??chouer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "renoncer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "s'attendre", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "enseigner", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "r??ussir", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "avoir", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "s'habituer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "songer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "commencer", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "h??siter", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "tarder", "??"));
        lockedCards.add(new FrenchPrepositionFlashcard(nextFID++, lID, "consentir", "??"));

        //Expression cards
        nextFID = 12000;
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Hello in French is?", "Bonjour"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Goodbye in French is?", "Au revoir"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Please in French is?", "S???il vous pla??t"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Thank you in French is?", "Merci"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "You???re welcome in French is?", "Bienvenue"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "I???m sorry in French is?", "Je suis desol??(e)"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Yes in French is?", "Oui"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "No in French is?", "Non"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Who in French is?", "Qui"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Where in French is?", "O??"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "When in French is?", "Quand"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "Why in French is?", "Pourquoi"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "How in French is?", "Comment"));
        lockedCards.add(new SimpleFlashcard(nextFID++, lID, "How many/How much in French is?", "Combien"));

        //*** END OF FRENCH CONTENT ***

        //*** START OF JAPANESE CONTENT ***

        lID = 4;

        //Vocabulary Cards
        nextFID = 40000;
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "kazok??", "family"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "k????ai", "dark"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "???", "kage", "shadow"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "?????????", "sabi??i", "lonely"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "???", "otoko", "man"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "???", "on??a", "woman"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "?????????", "ta??s????ka????", "help, save"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "ha??i????", "run"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "kata????", "talk, tell"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "kek??ko??", "splendid, quite"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "d??zet??s??bo??", "despair"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "to??kjo??", "Tokyo"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "niho??", "Japan"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "kitanai", "unclean"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "hanas??", "speak"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "tob??", "fly, jump"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "??i??izok??", "retreat, withdraw"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "dak??kjak??", "ridding oneself"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "??????", "t??s??joi", "strong"));
        lockedCards.add(new VocabularyFlashcard(nextFID++, lID, "???", "mido??i", "green"));

        //Alphabet (Kana) Cards
        nextFID = 41000;
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "ka"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "a"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "pi"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "sa"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "??????", "??a"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "??i"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "go"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "ke"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "na"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "????"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "??i"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "ha"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "??????", "????"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "ju"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "k??"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "??"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "??????", "??a"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "??????", "??o"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "???", "n"));
        lockedCards.add(new AlphabetFlashcard(nextFID++, lID, "??????", "kj??"));

        //Logogram (Kanji) Cards
        nextFID = 42000;
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "oo, dai", "large"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "hi, nichi", "sun, day"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "hito, ichi", "one"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "fusa.gu, utsu", "melancholy"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "hibiki, kyou", "echo, sound"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "kama, kan", "can"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "hako, sou", "box"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "maru, en", "circle, round"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "mi, san", "three"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "kuni, koku", "country"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "deko, totsu", "convex"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "boko, ou", "concave"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "shiro, haku", "white"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "ao, sei", "blue, green"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "haru, shun", "spring"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "aki, shuu", "autumn"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "atsu.i, netsu", "hot"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "samu.i, kan", "cold"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "fuka.i, shin", "deep"));
        lockedCards.add(new LogogramFlashcard(nextFID++, lID, "???", "kobo.su, rei", "zero, overflow"));

        //Grammar flashcards
        nextFID = 43000;
        lockedCards.add(new GrammarFlashcard(nextFID++, lID, "What is the basic word order in Japanese?", "Subject-Object-Verb (SOV)"));
        lockedCards.add(new GrammarFlashcard(nextFID++, lID, "What is the meaning of the ??? particle?", "It is a topic marker."));
        lockedCards.add(new GrammarFlashcard(nextFID++, lID, "What is the difference between ????????? and ?????????????", "The latter is formal speech."));

        //*** END OF JAPANESE CONTENT ***

        //Add all unlocked created cards to the database, due
        long nextDue = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
        for(IFlashcard f : unlockedCards)
            flashcardDB.addCard(f, nextDue++, 0);

        //Add all locked created cards to the database, locked
        for(IFlashcard f : lockedCards)
            flashcardDB.addCard(f,DBManager.getDateMax(), 0);

        return flashcardDB;
    }

    //Create a lesson database with the default lessons
    public static FakeLessonDB createFakeLessonDB()
    {
        FakeLessonDB lessonDB = new FakeLessonDB();

        //*** START OF FRENCH CONTENT ***

        //Flashcard lists (for unlocking)
        List<Integer> prepositionCards = new ArrayList<Integer>();
        for(int i = 11000; i <= 11062; i++)
            prepositionCards.add(i);
        List<Integer> phraseCards = new ArrayList<Integer>();
        for(int i = 12000; i <= 12013; i++)
            prepositionCards.add(i);

        //Prepositions lesson
        Lesson prepositionsLesson = new Lesson(10000, 1, "Prepositions with Verbs and an Infinitive", 3, "ic_lesson_grammar", 10001, prepositionCards);
        prepositionsLesson.addModule(new ParagraphLessonModule("Introduction", "Often in French and English, we compose a sentence where an action consists of two verbs: ex. Marie wanted to have a cookie. Here the two verbs are: ???to want???, conjugated in a past tense, and ???to have???, left as its infinitive tense. In English, the preposition we use for the infinitive verb is always the same, ???to???.\n\nHowever, in French when we have this structure some verbs are followed directly by an infinitive without a preposition ex. Marie voulait avoir un biscuit = Marie wanted/would have liked to have a cookie. Here vouloir is the main verb, and avoir is the infinitive. Any infinitive verbs after the verb vouloir do not have a preposition.\n\nOther verbs use one of the prepositions de or ??.\n\nUnfortunately, there are no hard rules to learning which verbs use what, this has to just be memorized through practice."));
        prepositionsLesson.addModule(new ParagraphLessonModule("Examples", "Elle demande ?? avoir un biscuit = She asks to have a cookie.\n\nElle d??cide d???avoir un biscuit = She decides to have a cookie."));

        //Conjugation lesson
        Lesson conjugationLesson = new Lesson(10001, 1, "Conjugation in Present Tense", 5, "ic_lesson_grammar", -1, phraseCards);
        conjugationLesson.addModule(new ParagraphLessonModule("Introduction", "The first tense we are going to learn how to conjugate in French is the present tense. The present tense in French is equivalent to the same meaning of the English present tense.\n\nFor example, je mange is equivalent to I eat/I am eating in French.\n\nThere are additional locutions that can be used to convey more specific meanings in the present tense but that will not be covered in this chapter."));
        conjugationLesson.addModule(new ParagraphLessonModule("Types of Verbs", "In French, there are three different types of verbs, distinguished by their endings. Every single verb in French either ends in -er, -ir, or -re.\n\nFor each of these different endings, there are different conjugation rules for the present tense that each correspond to one of the French pronouns:"));
        conjugationLesson.addModule(new ItemsListLessonModule("Select a type to expand", Arrays.asList("Je", "Tu", "Elle/Il/On", "Nous", "Vous", "Ils/Elles"), Arrays.asList("-er", "-ir", "-re"), Arrays.asList(Arrays.asList("-e\nje pr??pare", "-es\ntu manges", "-e\nil, elle, on t??l??phone", "-ons\nous ??tudions", "-ez\nvous proposez", "-ent\nils, elles voyagent"), Arrays.asList("-is\nje r??ussis", "-is\ntu finis", "-it\nil, elle, on d??sob??it", "-issons\nnous applaudissons", "-issez\nvous d??molissez", "-issent\nils, elles agissent"), Arrays.asList("-s\nje d??fends", "-s\ntu rends", "-\nil, elle, on entend", "-ons\nnous descendons", "-ez\nvous r??pondez", "-ent\nils, elles pr??tendent"))));
        conjugationLesson.addModule(new ParagraphLessonModule("Irregular Verbs", "There are many verbs in French that do not follow these rules and are irregular. These will have to be memorized overtime as you practice the French language, however a few important ones are shared below:\n\nThe verb ??tre = to be -> je suis, tu es, on est, nous sommes, vous ??tes, ils sont\nThe verb avoir = to have -> j'ai, tu as, on a, nous avons, vous avez, ils ont\nThe verb aller = to go -> je vais, tu vas, on va, nous allons, vous allez, ils vont"));

        //Negative construction lesson
        Lesson negativeLesson = new Lesson(10002, 1, "Negative Constructions", 8, "ic_lesson_grammar", -1, Collections.emptyList());
        negativeLesson.addModule(new ParagraphLessonModule("Introduction", "In French, in order to make the action in a sentence negative, we need a negative adverb. See the following examples below:\n\n*ne??? pas (not)\n\n*ne??? plus/encore/toujours (no longer, not anymore)\n\n*ne??? nulle part / quelque part/partout (only partly)\n\n*ne??? jamais (never)\n\n*ne??? pas encore/ d??j?? (not yet)\n\nne??? pas??? non plus /aussi (anymore)\n\nne??? pas de tout (not at all)\n\nne??? que (nothing but, only)\n\nne??? toujours pas (still not)\n\nne??? gu??re/ beaucoup/tr??s (hardly/barely/scarcely) (langue soutenue) "));
        negativeLesson.addModule(new ParagraphLessonModule("Usage", "Now that we know about some negative adverbs, let???s learn how/where they are placed in a sentence.\n\n1. If we have the simple verb form, the ne and pas hug the main verb. A pronoun stays beside the verb.\nEx. Je ne te regarde jamais jouer au basketball = I never watch you play basketball.\nEx. Je ne t???ai pas regard?? jouer au basketball = I don???t watch you play basketball.\n\n2. If we have an interrogative sentence, the ne and pas hug the verb and subject. A pronoun stays beside the verb.\nEx. Ne la regardes-tu pas? = You didn???t see it?\nEx. Ne se d??cidera-t-il pas ?? venir? = Will he not make up his mind to come?\n\n3. If we have an infinitive, the two elements of negation come before it\nEx. Ne pas aller au-del?? de cette porte = Do not go past this door.\n*In the construction ne??? pas??? non plus, non plus is placed after the infinitive verb or after the past participle.\nEx. Il n'a pas pos?? poser de question non plus = She did not ask anymore questions.\n\n4. Ne??? que indicates a restriction more than a negation. Ne comes before the verb and que comes before the thing that is limited.\nEx. Il n'y a que deux restaurants dans la rue = There are only two restaurants on the street."));
        negativeLesson.addModule(new ParagraphLessonModule("The Omission of ne", "Note that in spoken French only, the ne part of negation is often not said as a short-hand of speech, however this is not acceptable in writing."));

        //Add created lessons to the database
        lessonDB.addLesson(prepositionsLesson, false);
        lessonDB.addLesson(conjugationLesson, false);
        lessonDB.addLesson(negativeLesson, false);

        //*** END OF FRENCH CONTENT ***

        //*** START OF JAPANESE CONTENT ***

        //Flashcard lists (for unlocking)
        List<Integer> jpVocabCards = new ArrayList<Integer>();
        for(int i = 40000; i <= 40019; i++)
            jpVocabCards.add(i);
        List<Integer> jpKanaCards = new ArrayList<Integer>();
        for(int i = 41000; i <= 41019; i++)
            jpKanaCards.add(i);
        List<Integer> jpKanjiCards = new ArrayList<Integer>();
        for(int i = 42000; i <= 42019; i++)
            jpKanjiCards.add(i);
        List<Integer> jpGrammarCards = new ArrayList<Integer>();
        for(int i = 43000; i <= 43002; i++)
            jpGrammarCards.add(i);

        //Vocabulary lesson
        Lesson jpVocab = new Lesson(40000, 4, "Vocabulary: Sample Assortment", 5, "ic_lesson_vocabulary", -1, jpVocabCards);
        jpVocab.addModule(new ParagraphLessonModule("Introduction", "This lesson will simply provide an overview of some samples of Japanese vocabulary, to give you some sense of what Japanese words look and sound like. Each word will have a reading given both in kana and in romaji (romanized letters), and a basic meaning."));
        jpVocab.addModule(new ItemsListLessonModule("Select a Word", Arrays.asList("Reading (Kana)", "Reading (Romaji)", "Meaning"), Arrays.asList("??????", "???", "???", "??????", "??????", "??????", "??????", "??????", "??????"), Arrays.asList(Arrays.asList("?????????", "kazoku", "family"), Arrays.asList("?????????", "otoko", "man"), Arrays.asList("?????????", "onna", "woman"), Arrays.asList("?????????", "hashiru", "run"), Arrays.asList("?????????", "nihon", "Japan"), Arrays.asList("???????????????", "toukyou", "Tokyo"), Arrays.asList("?????????", "kataru", "talk, tell"), Arrays.asList("?????????", "hanasu", "speak"), Arrays.asList("?????????", "tsuyoi", "strong"))));

        //Writing lesson
        Lesson jpWriting = new Lesson(40001, 4, "Writing: Introduction to Kanji", 8, "ic_lesson_scripts", 40002, jpKanjiCards);
        jpWriting.addModule(new ParagraphLessonModule("Introduction", "The Japanese writing system may be the most challenging initial hurdle for English-speakers learning Japanese, because rather than having 26 letters, it uses, in addition to its native syllabary, thousands of logograms derived from Chinese, which in Japanese are known as kanji.\n\nLogograms, rather than representing particular sounds, are usually used to indicate meaning. Every kanji has at least one reading (a way of pronouncing it), but most have at least two, and many have several.\n\nThere is no shortcut or way around learning kanji, unless one is solely interested in speaking and listening. Memorizing the approximately 2000 so-called common use kanji is a major and important step in learning the language. Once you have learned them however, they will often provide an invaluable tool in deciphering unfamiliar words.\n\nListed below is a sampling of some common kanji, each with a meaing and some of their readings."));
        jpWriting.addModule(new ItemsListLessonModule("Select a Kanji", Arrays.asList("Readings", "Meaning"), Arrays.asList("???", "???", "???", "???", "???", "???", "???", "???", "???"), Arrays.asList(Arrays.asList("oo, dai", "large"), Arrays.asList("hi, nichi", "sun, day"), Arrays.asList("hito, ichi", "one"), Arrays.asList("kama, kan", "can"), Arrays.asList("hako, sou", "box"), Arrays.asList("maru, en", "circle, round"), Arrays.asList("deko, totsu", "convex"), Arrays.asList("boko, ou", "concave"), Arrays.asList("haru, shun", "spring"))));

        //Pronunciation lesson
        Lesson jpPronunciation = new Lesson(40002, 4, "Pronunciation: Differences from English", 10, "ic_lesson_pronunciation", -1, jpKanaCards);
        jpPronunciation.addModule(new ParagraphLessonModule("Introduction", "Compared to other aspects of the Japanese language, pronunciation will probably be surprisingly familiar to English speakers. In terms of the sounds themselves, most of them are used in English as well. In this lesson, we will outline a few of the exceptions."));
        jpPronunciation.addModule(new ParagraphLessonModule("The Japanese R", "The Japanese R sound (???, ???, ???, ???, ???) is actually found in English, but most native English speakers would not recognize it as its own sound. The most common realization of the Japanese R is what is known in linguistics as an alveolar tap.\n\nThough difficult to conceptualize, from the perspective of an English speaker, it lies somewhere between the English R, L, and D sounds. In reality, it is actually most similar to a D that is pronounced very quickly.\n\nIn English, this sound can be observed in the word water. When said quickly and without care, the t in water is pronounced with an alveolar tap, rather than the traditional t sound. Try pronouncing water with an explicit t and see if you can hear the difference."));
        jpPronunciation.addModule(new ParagraphLessonModule("The Japanese F", "The Japanese F sound (???) has no directly analogue in English, but is not too hard to conceptualize. Compared to the English F, the vocal tract is less constricted, particularly at the lips. Rather than putting your upper teeth to your lower lip, simply keep your lips together, as if pronouncing a B or P, and blow. That is approximation of the Japanese F."));
        jpPronunciation.addModule(new ParagraphLessonModule("Pitch Accent", "One aspect of Japanese pronunciation that is more difficult to grasp is pitch accent. While Japanese is not a tonal language, where the tone determines the meaning of a word, Japanese words still have determined pitch contours, where one part of a word is pronounced at a higher or lower pitch than another.\n\nThis contrasts with English, which uses a stress accent system. In English, for a word to, in some sense, sound correct, one part of the word must be louder (more stressed) than another, and which part that is varies from word to word. This is analogous to Japanese pitch accent, except with tone rather than loudness."));

        //Reading lesson
        Lesson jpReading = new Lesson(40003, 4, "Reading: ????????? on Wikipedia", 15, "ic_lesson_reading", 40004, Collections.emptyList());
        jpReading.addModule(new ParagraphLessonModule("Passage", "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????\n\n????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???3??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????10????????????????????????????????????\n\n???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????\n\n2019???4?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????/???????????????????????????????????????????????????8??????????????????"));
        jpReading.addModule(new QuizLessonModule("Reading Comprehension Quiz", Arrays.asList("How many speakers of Japanese are there in the world today?", "What is the official language of Japan?", "What is the 5th most-used language on the internet?", "Which other language is reflective of the syntax and phonology of Japanese?"), Arrays.asList("Approximately 130 million people.", "Japan technically has no official language, but practically uses Japanese for all official purposes.", "Portuguese", "Japanese Sign Language")));

        //Grammar lesson
        Lesson jpGrammar = new Lesson(40004, 4, "Grammar: Basic Particles", 8, "ic_lesson_grammar", -1, jpGrammarCards);
        jpGrammar.addModule(new ParagraphLessonModule("Introduction", "Perhaps the single most essential concept to understand in Japanese grammar is the particle. Particles are essentially suffixes, short words attached to the ends of other words or phrases, which indicate the role of the word or phrase they are attached to.\n\nIn English, a word's role in a sentence is determined by its position within the sentence. In Japanese, particles are used to indicate the role, which allows more freedom in where to position the words.\n\nIn this lesson, we will introduce a few common particles."));
        jpGrammar.addModule(new ParagraphLessonModule("The ??? (wa) Particle", "This may be the most common and basic of Japanese particles, yet is one of the more difficult ones for an English speaker to understand. The ??? particle marks the topic of a sentence, which sounds simple, but has no direct analogue in English.\n\nThe particle indicates the thing that the speaker is talking about, regardless of whether it is doing an action, having an action done to it, or something else entirely. Note that this is distinct from more familiar concepts like the subject or object of a sentence.\n\nIt is also important to note that, while the kana used in this particle is pronounced ha, when used as a particle, this is pronounced wa."));
        jpGrammar.addModule(new ParagraphLessonModule("The ??? (ga) Particle", "This particle marks the subject of a sentence. This would be a fairly simple and familiar concept for English speakers, if not for the existence of the aforementioned ??? particle, because the roles of these two particles can be easily confused. Clearly delineating between these two will require further discussion, but for the time being, it is sufficient to treat ??? as an explicit subject marker, and treat ??? as a marker of the grammatically vaguer concept of topic."));
        jpGrammar.addModule(new ParagraphLessonModule("The ??? (o) Particle", "This is the object-marking particle. The object is a familiar concept to English, so there isn't much more to say about this one. It simply marks the thing that the action is being done to.\n\nAs with ??? above, this particle has a different pronunciation from its kana alone. While it looks like it would be pronounced wo, it is in fact pronounced o."));

        //Add created lessons to the database
        lessonDB.addLesson(jpVocab, false);
        lessonDB.addLesson(jpWriting, false);
        lessonDB.addLesson(jpPronunciation, false);
        lessonDB.addLesson(jpReading, false);
        lessonDB.addLesson(jpGrammar, false);

        //*** END OF JAPANESE CONTENT ***

        return lessonDB;
    }
}